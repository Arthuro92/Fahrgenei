package observer;

import com.dataobjects.Appointment;
import com.google.gson.Gson;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import SmackCcsClient.SmackCcsClient;
import database.Databaseoperator;
import errors.ErrorMessages;

/**
 * Created by david on 24.05.2016.
 */
public class AppointmentObserver implements MessageObserver {
    //new
    private Map<String, String> payload;
    private Map<String, Object> jsonObject;
    private static final Logger logger = Logger.getLogger("AppointmentObserver");


    /**
     * Updates the Map payload for this object to the jsonObject. Also calls the setAppointment method so long as the task_category key of payload equals appointment
     *
     * @param jsonObject a Map the payload for this object is updated to
     */
    public void updateMessageObserver(Map<String, Object> jsonObject) {
        if (jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            this.jsonObject = jsonObject;

            if (this.payload.get("task_category").equals("appointment")) {
                switch (this.payload.get("task")) {

                    case "getsingleappointment":
                        logger.log(Level.INFO, "In getSingleAppointment");
                        break;

                    case "getallappointments":
                        logger.log(Level.INFO, "In getAllAppointments");
                        break;
                    case "insertappointment":
                        logger.log(Level.INFO, "In insertappointment");
                        createAppointment();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private boolean createAppointment() {
        Gson gson = new Gson();
        Appointment appointment = gson.fromJson(this.payload.get("content"), Appointment.class);

        if (Databaseoperator.insertNewAppointment(appointment.getAid(), appointment.getGid(), this.payload.get("content"))) {
            if (Databaseoperator.userIsInAppointment(appointment.getAid(),appointment.getGid(), Databaseoperator.getUserIdByToken( (String) this.jsonObject.get("from")), 1)) {
                sendInsertAppointmentSucess();
                return true;
            } else {
                Databaseoperator.deleteAppointment(appointment.getAid(), appointment.getGid());
                sendAppointmentError(ErrorMessages.MYSQL_ERROR);
                return false;
            }
        } else {
            sendAppointmentError(ErrorMessages.MYSQL_ERROR);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private boolean sendInsertAppointmentSucess() {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        try {

            Gson gson = new Gson();
            Appointment   appointment = gson.fromJson(this.payload.get("content"), Appointment.class);
            smackclient.sendDownstreamMessage("appointment", "appointmentinsertsuccess", (String) jsonObject.get("from"),appointment);
            appointment.setIsParticipant(0);
            smackclient.sendDownstreamMessage("appointment", "newappointment", "/topics/" + appointment.getGid(), appointment);
            return true;
        } catch (SmackException.NotConnectedException e) {
            //todo what now XD? retry or something
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendAppointmentError(String errortype) {
        SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
        try {
            smackCcsClient.sendDownstreamMessage("appointment", errortype, (String) jsonObject.get("from"), null);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            //todo maybe retry?
            return false;
        }
    }

    //todo use this method and update datebaseperator for syncing all getAppointments
    private boolean syncAppointments() {

        //Fehler falls Informationen fehlen
        if (!payload.containsKey("extra0") || !payload.containsKey("extra1")) {
            logger.log(Level.INFO, "Information invalid!");
            System.out.println(payload.containsKey("extra0"));
            System.out.println(payload.containsKey("extra1"));
            sendSingleAppointmentError(ErrorMessages.INVALID_INFORMTION);
            return false;
        }

        ArrayList<String> result = Databaseoperator.getAppointments(payload.get("extra0"), payload.get("extra1"));

        if (result.get(0).equals("error:NO_ACCESS")) {
            logger.log(Level.INFO, "Access for getting Appointment denied!");
            sendSingleAppointmentError(ErrorMessages.NO_ACCESS);
            return false;
        }

        if (result.get(0).equals("error:sqlexception")) {
            logger.log(Level.INFO, "SQL Exception!");
            sendSingleAppointmentError(ErrorMessages.MYSQL_ERROR);
            return false;
        }

        ArrayList<Appointment> appointmentlist = new ArrayList<>();
        Gson gson = new Gson();

        int i = 0;
        while (i < result.size()) {
            Appointment app = gson.fromJson(result.get(i), Appointment.class);
            appointmentlist.add(app);
            i++;
        }

        SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
        try {
            smackCcsClient.sendDownstreamMessage("appointment", "singleAppointment", (String) jsonObject.get("from"), appointmentlist);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * Sending Error back to Client, because an Error occured
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private boolean sendSingleAppointmentError(String errortype) {
        SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
        try {
            smackCcsClient.sendDownstreamMessage("appointment", errortype, (String) jsonObject.get("from"), null);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            // todo maybe retry?
            return false;
        }
    }

    /**
     * Constructs a new AppointmentObserver and registers it to a MessageSubject
     *
     * @param messageSubject a MessageSubject to register to
     */
    public AppointmentObserver(MessageSubject messageSubject) {
        messageSubject.registerMessageObserver(this);
        logger.log(Level.INFO, "Appointmentobserver registered");
    }
}
