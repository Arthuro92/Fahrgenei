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
    private Map<String, String> payload;
    private static final Logger logger = Logger.getLogger("GroupObserver ");
    final long senderId = 634948576599L; // your GCM sender id
    final String password = "AIzaSyCJGcfOGX9KrxznVVy_4DJoLAK-vF8KS3s";
    /**
     *
     */
    public void setGroup() {
        System.out.println("GROUP SET TO: " + this.payload.toString());
        Databaseoperator.insertnewgroup("10", this.payload.get("content"));
    }

    /**
     * Updates the Map payload for this object to the jsonObject. Also calls the setGroup method so long as the task_category key of payload equals group
     * @param jsonObject a Map the payload for this object is updated to
     */
    public void updateMO(Map<String, Object> jsonObject) {
        if(jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if(this.payload.get("task_category").equals("group")) {

                switch (this.payload.get("task")) {
                    case "getgrouparray":
                        logger.log(Level.INFO, "first switch task = grouparray");
                        Group grp = new Group("gruppe1", 2, "lennart", "lennart", "lennart1234");
                        SmackCcsClient smackclient = SmackCcsClient.getInstance();
                        try {
                            ArrayList<Group> grplist = Databaseoperator.getgrouplist();
                            smackclient.sendDownstreamMessage("group","grouparray","/topics/global", grplist );
                        } catch (SmackException.NotConnectedException e) {
                            e.printStackTrace();
                        } //todo here might be a nullpointer exception when something in getgroupList goes wrong, solution for server AND client is here important
                        break;
                    case "insertgroup":
//                        smackclient = SmackCcsClient.getInstance();
                        logger.log(Level.INFO, "second switch task = insertgroup");
                        Databaseoperator.insertnewgroup(this.payload.get("id"), this.payload.get("content"));
//                        try {
//                                smackclient.sendDownstreamMessage("group","newgroup","/topics/global", this.payload.get("content") );
//                        } catch (SmackException.NotConnectedException e) {
//                            e.printStackTrace();
//                        }
                        //
                        break;
                    default:
                        logger.log(Level.INFO, "default case");
                }
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
