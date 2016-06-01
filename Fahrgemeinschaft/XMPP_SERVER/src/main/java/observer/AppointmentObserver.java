package observer;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by david on 24.05.2016.
 */
public class AppointmentObserver implements MessageObserver {
    private Map<String, String> payload;
    private static final Logger logger = Logger.getLogger("AppointmentObserver");
    /**
     *
     */
    public void setAppointment() {
        System.out.println("APPOINTMENT SET TO: " + this.payload.toString());
    }

    /**
     * Updates the Map payload for this object to the jsonObject. Also calls the setAppointment method so long as the task_category key of payload equals appointment
     * @param jsonObject a Map the payload for this object is updated to
     */
    public void updateMO(Map<String, Object> jsonObject) {
        if(jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if(this.payload.get("task_category").equals("appointment")) {
                setAppointment();
            }
        }
    }

    /**
     * Constructs a new AppointmentObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public AppointmentObserver(MessageSubject ms) {
        ms.registerMO(this);
        logger.log(Level.INFO, "Appointmentobserver registered");
    }
}
