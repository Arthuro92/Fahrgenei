package observer;

import com.google.gson.Gson;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import SmackCcsClient.SmackCcsClient;
import database.Databaseoperator;
import dataobjects.Group;
import errors.ErrorMessages;


/**
 * Created by david on 23.05.2016.
 */
public class GroupObserver implements MessageObserver {
    //new
    private Map<String, String> payload;
    private static final Logger logger = Logger.getLogger("GroupObserver ");
    private Map<String, Object> jsonObject;

    /**
     * Updates the Map payload for this object to the jsonObject. Also calls the setGroup method so long as the task_category key of payload equals group
     * @param jsonObject a Map the payload for this object is updated to
     */
    @SuppressWarnings("unchecked")
    public void updateMessageObserver(Map<String, Object> jsonObject) {
        if(jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            this.jsonObject =  jsonObject;
            if(this.payload.get("task_category").equals("group")) {

                switch (this.payload.get("task")) {
                    case "getgrouparray":
                        logger.log(Level.INFO, "first switch task = grouparray");

                        SmackCcsClient smackclient = SmackCcsClient.getInstance();
                        try {
                            ArrayList<Group> grplist = Databaseoperator.getGroupList();
                            smackclient.sendDownstreamMessage("group","grouparray", (String) jsonObject.get("from"), grplist );
                        } catch (SmackException.NotConnectedException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "insertgroup":
                        //todo boolean does not get printed maybe handle boolean
                       logger.log(Level.INFO, "InsertGroup: " + insertGroup());
                        break;

                    default:
                        logger.log(Level.INFO, "default case");
                }
            }
        }
    }

    public boolean insertGroup() {
        logger.log(Level.INFO, "second switch task = insertGroup");

        if(this.payload.containsKey("extra0")) {

            if(Databaseoperator.insertNewGroup(this.payload.get("extra0"), this.payload.get("content") )) {
                sendInsertGroupSuccess();
                return true;
            } else {
                sendInsertGroupError(ErrorMessages.mysqlError);
                return false;
            }
        } else {
            logger.log(Level.INFO, "ERROR: No Extra  found!");
            sendInsertGroupError(ErrorMessages.invalidInformtion);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private boolean sendInsertGroupSuccess() {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        try {

            Gson gson = new Gson();
            smackclient.sendDownstreamMessage("group","groupinsertsuccess", (String) jsonObject.get("from"), gson.fromJson(this.payload.get("content"), Group.class) );
            return true;
        } catch (SmackException.NotConnectedException e) {
            //todo what now XD? retry or something
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public boolean sendInsertGroupError(String errortype) {
        SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
        try {
            smackCcsClient.sendDownstreamMessage("group" , errortype, (String) jsonObject.get("from"),null);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            //todo maybe retry?
            return false;
        }
    }
    /**
     * Constructs a new GroupObserver and registers it to a MessageSubject
     * @param messageSubject a MessageSubject to register to
     */
    public GroupObserver(MessageSubject messageSubject) {
        messageSubject.registerMessageObserver(this);
        logger.log(Level.INFO, "Groupobserver Registered");
    }
}
