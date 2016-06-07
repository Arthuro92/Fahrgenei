package observer;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import SmackCcsClient.SmackCcsClient;
import database.Databaseoperator;
import dataobjects.Group;


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
                        //todo boolean does not get printed
                        System.out.println("InsertUser: " + insertgroup());
                        break;

                    default:
                        logger.log(Level.INFO, "default case");
                }
            }
        }
    }

    public boolean insertgroup() {
        logger.log(Level.INFO, "second switch task = insertgroup");

        if(this.payload.containsKey("extra0")) {

            if(Databaseoperator.insertNewGroup(this.payload.get("extra0"), this.payload.get("content"))) {
                return true;
            } else {
                //todo send to client that groupinsert failed
                return false;
            }
        } else {
            //todo send to client that groupinsert failed
            logger.log(Level.INFO, "ERROR: No Extra  found!");
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
