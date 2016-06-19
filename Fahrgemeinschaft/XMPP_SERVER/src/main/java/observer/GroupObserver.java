package observer;

import com.dataobjects.Group;
import com.dataobjects.JsonCollection;
import com.dataobjects.User;
import com.dataobjects.UserInGroup;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import SmackCcsClient.SmackCcsClient;
import database.Databaseoperator;
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
     *
     * @param jsonObject a Map the payload for this object is updated togit r
     */
    @SuppressWarnings("unchecked")
    public void updateMessageObserver(Map<String, Object> jsonObject) {
        if (jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            this.jsonObject = jsonObject;
            if (this.payload.get("task_category").equals("group")) {

                switch (this.payload.get("task")) {
                    case "getgrouparray":
                        logger.log(Level.INFO, "GroupArray: " + getGroupArray());
                        break;
                    case "insertgroup":
                        logger.log(Level.INFO, "InsertGroup: " + insertGroup());
                        break;
                    case "inviteuser":
                        logger.log(Level.INFO, "User: " + this.payload.get("extra0") + " invited to: " + this.payload.get("extra1") + " from: " + jsonObject.get("from"));
                        inviteUser();
                        break;
                    case "invitationaccept":
                        logger.log(Level.INFO, "User: " + this.payload.get("extra1") + " accepted Group Invitation for Group: " + this.payload.get("extra0"));
                        invitationaccepted();
                        break;
                    default:
                        logger.log(Level.INFO, "default case");
                }
            }
        }
    }

    private void invitationaccepted() {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        Databaseoperator.updateUserIsInGroup(this.payload.get("extra0"), this.payload.get("extra1"), 1);
        UserInGroup userInGroup = new UserInGroup(this.payload.get("extra1"), this.payload.get("extra0"), 1);
        try {
            smackclient.sendDownstreamMessage("user", "userjoinedgroup", "/topics/" + this.payload.get("extra0"), userInGroup );
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private boolean inviteUser() {
        String token = Databaseoperator.getUserTokenByEmail(this.payload.get("extra0"));
        if (token == null) {
            sendGroupError(ErrorMessages.USER_NOT_FOUND);
            return false;
        }

        String userid = Databaseoperator.getUserIdByEmail(this.payload.get("extra0"));
        if (userid == null) {
            sendGroupError(ErrorMessages.MYSQL_ERROR);
            return false;
        }
        if (!Databaseoperator.setUserIsInGroup(this.payload.get("extra1"), userid, 0)) {
            sendGroupError(ErrorMessages.MYSQL_ERROR);
        }
        if (!sendInvitation(token)) {
            sendGroupError(ErrorMessages.SENDING_INVITATION_FAILED);
            Databaseoperator.deleteUserIsInGroup(this.payload.get("extra1"), userid);
            return false;
        }
        if(!broadcastNewUser()) {
            sendGroupError(ErrorMessages.SEND_BROADCAST_USER_FAILED);
            logger.log(Level.INFO, "something in broadcastNewUser went wrong");
            return false;
            //todo what to do xD?
        }


        logger.log(Level.INFO, "Sending InvitationSucessMessage: " + sendInvitationSuccess());
        return true;
    }

    private boolean broadcastNewUser() {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        try {
            User user = JsonCollection.jsonToUser(Databaseoperator.getUserByEmail(this.payload.get("extra0")));
            Group group = JsonCollection.jsonToGroup(this.payload.get("extra2"));
            UserInGroup userInGroup = new UserInGroup(user.getId(), group.getGid(), 0);

            String[] stringarray = new String[2];
            stringarray[0] =  user.getJsonInString();
            stringarray[1] = userInGroup.getJsonInString();

            smackclient.sendDownstreamMessage("user", "newuser", "/topics/" + group.getGid(), stringarray);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendInvitationSuccess() {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        try {
            smackclient.sendDownstreamMessage("group", "invitationsuccess", (String) jsonObject.get("from"),null);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean sendInvitation(String token) {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        try {
            Group grp = JsonCollection.jsonToGroup(this.payload.get("extra2"));
            System.out.println(this.payload.get("extra2"));
            ArrayList<User> userList = Databaseoperator.getUsersWithGroupId(grp.getGid());
            ArrayList<UserInGroup> userIsInGroup = Databaseoperator.getUsersInGroupWithGroupId(grp.getGid());


            String[] stringarray = new String[3];
            stringarray[0] = grp.getJsonInString();
            stringarray[1] = JsonCollection.objectToJson(userList);
            stringarray[2] = JsonCollection.objectToJson(userIsInGroup);

            smackclient.sendDownstreamMessage("group", "groupinvitation", token, stringarray);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean getGroupArray() {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        try {
            ArrayList<Group> grplist = Databaseoperator.getGroupList();
            smackclient.sendDownstreamMessage("group", "grouparray", (String) jsonObject.get("from"), grplist);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertGroup() {
        Group grp = JsonCollection.jsonToGroup(this.payload.get("content"));

        if (Databaseoperator.insertNewGroup(grp.getGid(), this.payload.get("content"))) {
            if (Databaseoperator.setUserIsInGroup(grp.getGid(), grp.getAdminid(), 1)) {
                sendInsertGroupSuccess();
                return true;
            } else {
                Databaseoperator.deleteGroup(grp.getGid());
                sendGroupError(ErrorMessages.MYSQL_ERROR);
                return false;
            }
        } else {
            sendGroupError(ErrorMessages.MYSQL_ERROR);
            return false;
        }
    }


    @SuppressWarnings("unchecked")
    private boolean sendInsertGroupSuccess() {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        try {
            smackclient.sendDownstreamMessage("group", "groupinsertsuccess", (String) jsonObject.get("from"), JsonCollection.jsonToGroup(this.payload.get("content")));
            return true;
        } catch (SmackException.NotConnectedException e) {
            //todo what now XD? retry or something
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private boolean sendGroupError(String errortype) {
        SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
        try {
            smackCcsClient.sendDownstreamMessage("group", errortype, (String) jsonObject.get("from"), null);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            //todo maybe retry?
            return false;
        }
    }




    /**
     * Constructs a new GroupObserver and registers it to a MessageSubject
     *
     * @param messageSubject a MessageSubject to register to
     */
    public GroupObserver(MessageSubject messageSubject) {
        messageSubject.registerMessageObserver(this);
        logger.log(Level.INFO, "Groupobserver Registered");
    }
}
