import java.util.Map;

/**
 * Created by david on 23.05.2016.
 */
public class UserObserver implements MessageObserver {
    private Map<String, String> payload;
    public void setUser() {
        System.out.println("User set to: ");
    }
    public void updateMO(Map<String, Object> jsonObject) {
        if(jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if(this.payload.get("task_category").equals("user")) {
                setUser();
            }
        }
    }
    public UserObserver(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("UserObserver registered!");
    }
}
