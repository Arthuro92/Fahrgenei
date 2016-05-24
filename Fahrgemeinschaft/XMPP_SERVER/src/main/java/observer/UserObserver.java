package observer;

import java.util.Map;

/**
 * Created by david on 23.05.2016.
 */
public class UserObserver implements MessageObserver {
    private Map<String, String> payload;

    /**
     *
     */
    public void setUser() {
        System.out.println("USER SET TO: ");
    }

    /**
     * Updates the Map payload for this object to the jsonObject. Also calls the setUser method so long as the task_category key of payload equals user
     * @param jsonObject a Map the payload for this object is updated to
     */
    public void updateMO(Map<String, Object> jsonObject) {
        if(jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if(this.payload.get("task_category").equals("user")) {
                setUser();
            }
        }
    }

    /**
     * Constructs a new UserObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public UserObserver(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("USEROBSERVER REGISTERED");
    }
}
