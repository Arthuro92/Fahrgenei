package server.observer;

import com.example.dataobjects.JsonCollection;
import com.example.dataobjects.User;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//import de.dataobjects.User;

/**
 * Created by david on 23.05.2016.
 * Handle all Messages with task_category = user
 */
public class UserObserver extends RepositorieConnector implements MessageObserver {
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

    /**
     * Register new User
      * @return true when success, false when failed
     */
    private boolean registration() {
        try {
            logger.log(Level.INFO, "first switch task = registration");
            User user = JsonCollection.jsonToUser(this.payload.get("content"));
            userRepository.save(user);
            return true;
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "NullPointerException in registration");
            return false;
        }
    }

    /**
     * Constructs a new UserObserver and registers it to a MessageSubject
     *
     * @param messageSubject a MessageSubject to register to
     */
    public UserObserver(server.observer.MessageSubject messageSubject) {
        messageSubject.registerMessageObserver(this);
        initRepositories();
        logger.log(Level.INFO, "Userobserver registered");
    }
}
