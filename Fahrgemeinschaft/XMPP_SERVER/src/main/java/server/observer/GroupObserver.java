package server.observer;


import com.example.dataobjects.Groups;
import com.example.dataobjects.JsonCollection;
import com.example.dataobjects.User;
import com.example.dataobjects.UserInGroup;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.errors.ErrorMessages;
import server.smackccsclient.SmackCcsClient;


/**
 * Created by david on 23.05.2016.
 */
public class GroupObserver  extends RepositorieConnector implements MessageObserver {
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
                    case "insertgroup":
                        logger.log(Level.INFO, "InsertGroup: " + insertGroup());
                        break;
                    case "inviteuser":
                        logger.log(Level.INFO, "database.User: " + this.payload.get("extra0") + " invited to: " + this.payload.get("extra1") + " from: " + jsonObject.get("from"));
                        inviteUser();
                        break;
                    case "invitationaccept":
                        logger.log(Level.INFO, "database.User: " + this.payload.get("extra1") + " accepted database.Groups Invitation for database.Groups: " + this.payload.get("extra0"));
                        invitationaccepted();
                        break;
                    default:
                        logger.log(Level.INFO, "default case");
                }
            }
        }
    }

    private void invitationaccepted() {
        try {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        UserInGroup userInGroup = new UserInGroup(this.payload.get("extra1"), this.payload.get("extra0"), 1);
        userInGroupRepository.save(userInGroup);

            smackclient.sendDownstreamMessage("user", "userjoinedgroup", "/topics/" + this.payload.get("extra0"), userInGroup );
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "Error updating UserinGroup in invitationaccepted");
        }
    }

    private boolean inviteUser() {
        User user = userRepository.findByEmail(this.payload.get("extra0"));
        try {
            if (user == null) {
                sendGroupError(ErrorMessages.USER_NOT_FOUND);
                return false;
            }

            UserInGroup userInGroup = new UserInGroup(user.getId(), this.payload.get("extra1"), 0);
            userInGroupRepository.save(userInGroup);

            if(!sendInvitation(user.getToken())) {
                sendGroupError(ErrorMessages.SENDING_INVITATION_FAILED);
                userInGroupRepository.delete(userInGroup);
                return false;
            }

            if(!broadcastNewUser()) {
                sendGroupError(ErrorMessages.SEND_BROADCAST_USER_FAILED);
                logger.log(Level.INFO, "something in broadcastNewUser went wrong");
                return false;
                //todo how to handle this?
            }

        } catch (NullPointerException e) {
            sendGroupError(ErrorMessages.MYSQL_ERROR);
            logger.log(Level.INFO, "Mysql Error in invitreUser");
            return false;
        }
        logger.log(Level.INFO, "Sending InvitationSucessMessage: " + sendInvitationSuccess());
        return true;
    }


    private boolean broadcastNewUser() {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        try {
            User user = userRepository.findByEmail(this.payload.get("extra0"));
            Groups group = JsonCollection.jsonToGroup(this.payload.get("extra2"));
            UserInGroup userInGroup = new UserInGroup(user.getId(), group.getGid(), 0);

            String[] stringarray = new String[2];
            stringarray[0] =  user.getJsonInString();
            stringarray[1] = userInGroup.getJsonInString();

            smackclient.sendDownstreamMessage("user", "newuser", "/topics/" + group.getGid(), stringarray);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "NullPointerException in broadcastNewUser");
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
            Groups grp = JsonCollection.jsonToGroup(this.payload.get("extra2"));

            ArrayList<UserInGroup> userInGroupList = userInGroupRepository.findByGid(grp.getGid());
            ArrayList<User> userList = new ArrayList<>();
            for(UserInGroup userInGroup : userInGroupList) {
                userList.add(userRepository.findOne(userInGroup.getUid()));
            }

            String[] stringarray = new String[3];
            stringarray[0] = grp.getJsonInString();
            stringarray[1] = JsonCollection.objectToJson(userList);
            stringarray[2] = JsonCollection.objectToJson(userInGroupList);

            smackclient.sendDownstreamMessage("group", "groupinvitation", token, stringarray);
            return true;
        } catch (SmackException.NotConnectedException e) {
            logger.log(Level.INFO, "Smack Not Connected Exception");
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
                logger.log(Level.INFO, "Error sending Invitation: NullPointerException");
                return false;
        }
    }

    private boolean insertGroup() {
        Groups group = JsonCollection.jsonToGroup(this.payload.get("content"));

        try {
            groupsRepository.save(group);
            UserInGroup userInGroup = new UserInGroup(group.getAdminid(), group.getGid(), 1);
            userInGroupRepository.save(userInGroup);
            sendInsertGroupSuccess();
            return true;
        } catch (NullPointerException e) {
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
        initRepositories();
        logger.log(Level.INFO, "Groupobserver Registered");
    }
}
