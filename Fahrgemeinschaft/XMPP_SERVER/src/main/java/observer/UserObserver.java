package observer;

import com.dataobjects.User;
import com.google.gson.Gson;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.Databaseoperator;

/**
 * Created by david on 23.05.2016.
 */
public class UserObserver implements MessageObserver {
    //new
    private Map<String, String> payload;
    private static final Logger logger = Logger.getLogger("UserObserver ");


    /**
     * Updates the Map payload for this object to the jsonObject. Also calls the setUser method so long as the task_category key of payload equals user
     *
     * @param jsonObject a Map the payload for this object is updated to
     */
    public void updateMessageObserver(Map<String, Object> jsonObject) {
        if (jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if (this.payload.get("task_category").equals("user")) {
                switch (this.payload.get("task")) {
                    case "registration":
                        logger.log(Level.INFO, "register new user: " + registration());
                        break;
                    default:
                        logger.log(Level.INFO, "default case");
                }
            }
        }
    }

    private boolean registration() {
        logger.log(Level.INFO, "first switch task = registration");

        Gson gson = new Gson();
        User user = gson.fromJson(this.payload.get("content"), User.class);

        if (Databaseoperator.insertNewUser(user.getId(), user.getToken(), this.payload.get("content"), user.getEmail())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Constructs a new UserObserver and registers it to a MessageSubject
     *
     * @param messageSubject a MessageSubject to register to
     */
    public UserObserver(MessageSubject messageSubject) {
        messageSubject.registerMessageObserver(this);
        logger.log(Level.INFO, "Userobserver registered");
    }
}
