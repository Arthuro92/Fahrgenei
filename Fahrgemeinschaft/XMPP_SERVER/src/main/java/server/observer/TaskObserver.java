package server.observer;

import com.example.dataobjects.JsonCollection;
import com.example.dataobjects.Task;

import org.jivesoftware.smack.SmackException;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.errors.ErrorMessages;
import server.smackccsclient.SmackCcsClient;

/**
 * Created by Lennart on 30.06.2016.
 */
public class TaskObserver extends RepositorieConnector implements MessageObserver {

    private Map<String, String> payload;
    private Map<String, Object> jsonObject;
    private static final Logger logger = Logger.getLogger("TaskObserver");


    /**
     * Updates the Map payload for this object to the jsonObject. Also calls the setAppointment method so long as the task_category key of payload equals appointment
     *
     * @param jsonObject a Map the payload for this object is updated to
     */
    public void updateMessageObserver(Map<String, Object> jsonObject) {
        if (jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            this.jsonObject = jsonObject;

            if (this.payload.get("task_category").equals("task")) {
                switch (this.payload.get("task")) {
                    case "newtask":
                        logger.log(Level.INFO, "In New Task");
                        createNewTask();
                        break;
                    default:
                        logger.log(Level.INFO, "default case");
                        break;
                }
            }
        }
    }

    private boolean createNewTask() {
        try {
            Task task = JsonCollection.jsonToTask(this.payload.get("content"));
            taskRepository.save(task);
            broadcastNewTask();
            return true;
        } catch(NullPointerException e) {
            logger.log(Level.INFO, "NullPointerException");
            sendTaskError(ErrorMessages.MYSQL_ERROR);
            return false;
        }
    }

    private void broadcastNewTask() {
        SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
        try {
            Task task = JsonCollection.jsonToTask(this.payload.get("content"));
            smackCcsClient.sendDownstreamMessage("task", "taskinsertsuccess", (String) jsonObject.get("from"),task);
            smackCcsClient.sendDownstreamMessage("task","broadcastedtask" ,"/topics/" + task.getGid() , task);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private boolean sendTaskError(String errortype) {
        SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
        try {
            smackCcsClient.sendDownstreamMessage("task", errortype, (String) jsonObject.get("from"), null);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            //todo maybe retry?
            return false;
        }
    }



    /**
     * Constructs a new AppointmentObserver and registers it to a MessageSubject
     * calling initRepositories
     * @param messageSubject a MessageSubject to register to
     */
    public TaskObserver(MessageSubject messageSubject) {
        messageSubject.registerMessageObserver(this);
        initRepositories();
        logger.log(Level.INFO, "TaskObserver registered");
    }
}
