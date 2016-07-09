package server.observer;


import com.example.dataobjects.Appointment;
import com.example.dataobjects.Groups;
import com.example.dataobjects.JsonCollection;
import com.example.dataobjects.Task;
import com.example.dataobjects.User;
import com.example.dataobjects.UserInAppointment;
import com.example.dataobjects.UserInGroup;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.Algorithm.Algorithm;
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
                        logger.log(Level.INFO, "User: " + this.payload.get("extra0") + " invited to: " + this.payload.get("extra1") + " from: " + jsonObject.get("from"));
                        inviteUser();
                        break;
                    case "invitationaccept":
                        logger.log(Level.INFO, "User: " + this.payload.get("extra1") + " accepted Groups Invitation for database.Groups: " + this.payload.get("extra0"));
                        invitationaccepted();
                        break;
                    case "newsubstitute":
                        logger.log(Level.INFO, "New Substitute");
                        updateSubstituteInGroup();
                        break;
                    case "deleteuseringroup":
                        logger.log(Level.INFO, "User Leaved Group");
                        deleteUserInGroup();
                        sendDeleteUserInGroup();
                        break;
                    case "deletegroup":
                        deleteGroup();
                        sendDeleteGroup();
                        break;
                    default:
                        logger.log(Level.INFO, "default case");
                }
            }
        }
    }

    private void sendDeleteGroup() {
        try {
            SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
            Groups groups = JsonCollection.jsonToGroup(this.payload.get("content"));
            smackCcsClient.sendDownstreamMessage("group", "deletegroup", "/topics/" + groups.getGid(), groups);
        } catch(SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private void deleteGroup() {
        Groups groups = JsonCollection.jsonToGroup(this.payload.get("content"));
        ArrayList<Appointment> appointmentArrayList = appointmentRepository.findByGid(groups.getGid());
        ArrayList<UserInGroup> userInGroupArrayList = userInGroupRepository.findByGid(groups.getGid());
        ArrayList<UserInAppointment> userInAppointmentArrayList = userInAppointmentRepository.findByGid(groups.getGid());
        ArrayList<Task> taskArrayListt = taskRepository.findByGid(groups.getGid());

        for(Appointment appointment : appointmentArrayList) {
            appointmentRepository.delete(appointment);
        }
        for(UserInGroup userInGroup : userInGroupArrayList) {
            userInGroupRepository.delete(userInGroup);
        }
        for(UserInAppointment userInAppointment : userInAppointmentArrayList) {
            userInAppointmentRepository.delete(userInAppointment);
        }
        for(Task task : taskArrayListt) {
            taskRepository.delete(task);
        }
        groupsRepository.delete(groups);
    }

    private void sendDeleteUserInGroup() {
        try {
            SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
            UserInGroup userInGroup = JsonCollection.jsonToUserInGroup(this.payload.get("content"));
            smackCcsClient.sendDownstreamMessage("group", "deleteuseringroup", "/topics/" + userInGroup.getGid(), userInGroup);
        } catch(SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

    }

    private void deleteUserInGroup() {
        UserInGroup userInGroup = JsonCollection.jsonToUserInGroup(this.payload.get("content"));
        userInGroupRepository.delete(userInGroup);
        ArrayList<UserInAppointment> userInAppointmentArrayList = userInAppointmentRepository.findByUid(userInGroup.getUid());
        for(UserInAppointment userInAppointment : userInAppointmentArrayList) {
            if(userInAppointment.getGid().equals(userInGroup.getGid())) {
                userInAppointmentRepository.delete(userInAppointment);
                Algorithm algorithm = new Algorithm();
                algorithm.calculateDrivers(userInAppointment);
            }
        }
    }

    private void updateSubstituteInGroup() {
        try {
            SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
            Groups groups = JsonCollection.jsonToGroup(this.payload.get("content"));
            groupsRepository.save(groups);
            smackCcsClient.sendDownstreamMessage("group", "updatinggroup", "/topics/" + groups.getGid(), groups);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "Error updating Group with new Substitute");
        }
    }

    private void invitationaccepted() {
        try {
        SmackCcsClient smackclient = SmackCcsClient.getInstance();
        UserInGroup userInGroup = new UserInGroup(this.payload.get("extra1"), this.payload.get("extra0"), 1);
        userInGroupRepository.save(userInGroup);
            sendGroupInformation();
            smackclient.sendDownstreamMessage("user", "userjoinedgroup", "/topics/" + this.payload.get("extra0"), userInGroup );
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "Error updating UserinGroup in invitationaccepted");
        }
    }

    private void sendGroupInformation() {
        try {
            String gid = this.payload.get("extra0");
            SmackCcsClient smackclient = SmackCcsClient.getInstance();
            ArrayList<UserInGroup> userInGroupList = userInGroupRepository.findByGid(gid);
            ArrayList<User> userList = new ArrayList<>();

            for (UserInGroup userInGroup : userInGroupList) {
                userList.add(userRepository.findOne(userInGroup.getUid()));
            }

            ArrayList<Appointment> appointmentArrayList = appointmentRepository.findByGid(gid);

            String[] stringarray = new String[3];
            stringarray[0] = JsonCollection.objectToJson(userList);
            stringarray[1] = JsonCollection.objectToJson(userInGroupList);
            stringarray[2] = JsonCollection.objectToJson(appointmentArrayList);
            smackclient.sendDownstreamMessage("group", "groupinformation", (String) jsonObject.get("from"), stringarray);
            Groups groups = groupsRepository.findOne(gid);
            smackclient.sendDownstreamMessage("group", "updatinggroup", (String) jsonObject.get("from"), groups);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Error sending Group Information");
        } catch (SmackException.NotConnectedException e) {
            logger.log(Level.INFO, "Not Connected Error SmackClient");
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
            System.out.println(grp.getJsonInString());
            smackclient.sendDownstreamMessage("group", "groupinvitation", token, grp);
            return true;
        } catch (SmackException.NotConnectedException e) {
            logger.log(Level.INFO, "Smack Not Connected Exception");
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "Error sending Invitation: NullPointerException");
            e.printStackTrace();
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
            System.out.println((String) jsonObject.get("from"));
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
