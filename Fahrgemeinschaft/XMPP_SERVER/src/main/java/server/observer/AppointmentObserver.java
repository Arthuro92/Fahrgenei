package server.observer;

import com.example.dataobjects.Appointment;
import com.example.dataobjects.JsonCollection;
import com.example.dataobjects.User;
import com.example.dataobjects.UserInAppointment;
import com.example.dataobjects.UserInGroup;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.Algorithm.Algorithmus;
import server.errors.ErrorMessages;
import server.smackccsclient.SmackCcsClient;

/**
 * Created by david on 24.05.2016.
 */
public class AppointmentObserver extends RepositorieConnector implements MessageObserver {
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
                    case "participantchange":
                        logger.log(Level.INFO, "Changing Participant");
                        changeParticipant();
                    default:
                        break;
                }
            }
        }
    }

    private void changeParticipant() {
        try {
            UserInAppointment userInAppointment = JsonCollection.userInAppointment(this.payload.get("content"));
            userInAppointmentRepository.save(userInAppointment);
            sendParticipantChangeSuccess();
            calculateNewDrivers();
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "NullPointerException");
            e.printStackTrace();
        }
    }

    private void calculateNewDrivers() {
        Algorithmus algorithmus = new Algorithmus();
        algorithmus.calculateDrivers(JsonCollection.userInAppointment(this.payload.get("content")));
    }

    private void sendParticipantChangeSuccess() {
        try {
            SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
            smackCcsClient.sendDownstreamMessage("appointment", "updatingparticipantsuccess", (String) jsonObject.get("from"), JsonCollection.userInAppointment(this.payload.get("content")));
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }


    private boolean createAppointment() {
        try {
            Appointment appointment = JsonCollection.jsonToAppointment(this.payload.get("content"));

            appointmentRepository.save(appointment);
            updateUserInAppointment();

            sendInsertAppointmentSucess();
            return true;
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "NullPointerException");
            e.printStackTrace();
            sendAppointmentError(ErrorMessages.MYSQL_ERROR);
            return false;
        }
    }

    private boolean updateUserInAppointment() {
        try {
            Appointment appointment = JsonCollection.jsonToAppointment(this.payload.get("content"));
            ArrayList<UserInGroup> userInGroupArrayList = userInGroupRepository.findByGid(appointment.getGid());

            for (UserInGroup userInGroup : userInGroupArrayList) {
                UserInAppointment userInAppointment = new UserInAppointment(appointment.getAid(), appointment.getGid(), userInGroup.getUid(), 0);
                userInAppointmentRepository.save(userInAppointment);
            }

            String string = (String) this.jsonObject.get("from");
            User user = userRepository.findByToken(string);
            UserInAppointment userInAppointment = new UserInAppointment(appointment.getAid(), appointment.getGid(), user.getId(), 1);
            userInAppointmentRepository.save(userInAppointment);
            return true;
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "NullPointerException");
            e.printStackTrace();
            return false;
        }
    }


    @SuppressWarnings("unchecked")
    private boolean sendInsertAppointmentSucess() {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        try {
            Appointment appointment = JsonCollection.jsonToAppointment(this.payload.get("content"));
            smackclient.sendDownstreamMessage("appointment", "appointmentinsertsuccess", (String) jsonObject.get("from"), appointment);
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


    /**
     * Sending Error back to Client, because an Error occured
     *
     * @return true when sending sucess / false when fail
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
     * calling initRepositories
     *
     * @param messageSubject a MessageSubject to register to
     */
    public AppointmentObserver(MessageSubject messageSubject) {
        messageSubject.registerMessageObserver(this);
        initRepositories();
        logger.log(Level.INFO, "Appointmentobserver registered");
    }
}
