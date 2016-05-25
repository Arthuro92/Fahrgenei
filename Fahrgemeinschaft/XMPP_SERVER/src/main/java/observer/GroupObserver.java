package observer;

import java.util.Map;

import database.databaseoperator;

/**
 * Created by david on 23.05.2016.
 */
public class GroupObserver implements MessageObserver {
    private Map<String, String> payload;

    /**
     *
     */
    public void setGroup() {
        System.out.println("GROUP SET TO: " + this.payload.toString());
        databaseoperator.insertnewgroup("10", this.payload.get("content"));
    }

    /**
     * Updates the Map payload for this object to the jsonObject. Also calls the setGroup method so long as the task_category key of payload equals group
     * @param jsonObject a Map the payload for this object is updated to
     */
    public void updateMO(Map<String, Object> jsonObject) {
        if(jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if(this.payload.get("task_category").equals("group")) {
                setGroup();
            }
        }
    }

    /**
     * Constructs a new GroupObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public GroupObserver(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("GROUPOBSERVER REGISTERED");
    }
}
