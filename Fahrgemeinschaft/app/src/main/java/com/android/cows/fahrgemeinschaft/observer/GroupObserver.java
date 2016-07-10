package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.gcm.TopicSubscriber;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

import de.dataobjects.Appointment;
import de.dataobjects.Groups;
import de.dataobjects.JsonCollection;
import de.dataobjects.Task;
import de.dataobjects.User;
import de.dataobjects.UserInAppointment;
import de.dataobjects.UserInGroup;

/**
 * Created by david on 23.05.2016.
 */
public class GroupObserver implements MessageObserver {
    //new new
    private Bundle payload;
    private Context context = GlobalAppContext.getAppContext();
    private static final String TAG = "GroupOberserver";


    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setGroup method so long as the task_category key of payload equals group
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMessageObserver(Bundle jsonObject) {
        this.payload = jsonObject;
        ;
        if (this.payload.getString("task_category").equals("group")) {
            switch (this.payload.getString("task")) {
                case "grouparray" :
                    Log.i(TAG, "GroupArray");
                    groupArray();
                    break;
                case "invitationsuccess" :
                    Log.i(TAG, "Invitation Success");
                    invitationSuccess();
                    break;
                case "groupinsertsuccess" :
                    Log.i(TAG, "Group Insert Sucess");
                    updateLocalGroupTable();
                    updateLocalUserIsInGroup(1);
                    TopicSubscriber.subscribeToTopic(JsonCollection.jsonToGroup(this.payload.getString("content")).getGid());
                    insertSuccess();
                    break;
                case "groupinvitation" :
                    Log.i(TAG, "Groupinvitation");
                    groupInvitation();
                    break;
                case "groupinformation" :
                    Log.i(TAG, "Group Information");
                    addGroupInformations();
                    break;
                case "updatinggroup" :
                    Log.i(TAG, "Updating Group");
                    updatingGroup();
                    sendLocalUpdateUserBroadcast();
                    break;
                case "deleteuseringroup":
                    Log.i(TAG, "User leaved Group");
                    deleteUserInGroup();
                    sendLocalUpdateUserBroadcast();
                    sendLocalParticipantUpdateBroadcast();
                    break;
                case "deletegroup":
                    Log.i(TAG, "Delete Group");
                    deleteGroup();
                    sendLocalUpdateGroupsBroadcast();
                    sendLocalReturnBroadcast();
                    break;
                default:
                    if (this.payload.getString("task").startsWith("error")) {
                        Log.i(TAG, "ERROR Group");
                        errorGroup(this.payload.getString("task"));
                    }
                    Log.i(TAG, "default case");
            }
        }
    }

    private void deleteGroup() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.deleteGroup(JsonCollection.jsonToGroup(this.payload.getString("content")).getGid());
    }

    private void deleteUserInGroup() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        UserInGroup userInGroup = JsonCollection.jsonToUserInGroup(this.payload.getString("content"));
        sqLiteDBHandler.deleteUserInGroup(userInGroup.getGid(), userInGroup.getUid());
        sqLiteDBHandler.deleteUserInAppointment(userInGroup.getGid(), userInGroup.getUid());
        TopicSubscriber.unsubscribeFromTopic(userInGroup.getGid());
    }

    private void updatingGroup() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        Groups groups = JsonCollection.jsonToGroup(this.payload.getString("content"));
        sqLiteDBHandler.addGroup(groups);
    }

    private void invitationSuccess() {
        Intent insertSuccess = new Intent("invitesuccess");
        LocalBroadcastManager.getInstance(context).sendBroadcast(insertSuccess);
    }

    private void sendLocalUpdateGroupsBroadcast() {
        Intent intent = new Intent("updategroupgeneral");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void sendLocalReturnBroadcast() {
        Intent intent = new Intent("returntogeneralgroups");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void sendLocalParticipantUpdateBroadcast() {
        Intent intent = new Intent("updateparticipants");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void updateLocalGroupTable() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addGroup(JsonCollection.jsonToGroup(this.payload.getString("content")));
    }

    private void groupInvitation() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);

        Groups grp = JsonCollection.jsonToGroup(this.payload.getString("content"));

        sqLiteDBHandler.addGroup(grp);

        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        sqLiteDBHandler.joinGroup(grp.getGid(),  prefs.getString("userid", ""),0);

        Log.i(TAG, "ADDING GROUP" + grp.getName());
        sendLocalUpdateGroupsBroadcast();
    }

    /**
     * Adding all Group information getting called when user joined group
     */
    public void addGroupInformations() {
        updateLocalInsertNewGroupMembers();
        updateLocalAppointmentTable();
        updateLocalTaskTable();
        sendLocalUpdateGroupsBroadcast();
        sendLocalUpdateAppointmentsBroadcast();
        sendLocalUpdateUserBroadcast();
        sendLocalUpdateTaskBroadcast();
    }

    /**
     * Send Local Broadcast to call GUI
     */
    private void sendLocalUpdateTaskBroadcast() {
        Intent createdtasksuccess = new Intent("createdTask");
        LocalBroadcastManager.getInstance(context).sendBroadcast(createdtasksuccess);
    }

    /**
     * Insert new Tasks
     */
    private void updateLocalTaskTable() {
        Log.i(TAG, "update Local DB, Insert New Appointments");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        String[] stringArray = JsonCollection.jsonToStringArray((this.payload.getString("content")));
        ArrayList<Task> taskArrayList = JsonCollection.jsonToTaskList(stringArray[3]);

        for(Task task : taskArrayList) {
            sqLiteDBHandler.addTask(task);
        }
    }

    /**
     * notify GUI
     */
    private void sendLocalUpdateAppointmentsBroadcast() {
        Intent intent = new Intent("updategroupappointments");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * notify GUI
     */
    public void sendLocalUpdateUserBroadcast() {
        Intent intent = new Intent("updategroupuser");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * notify GUI
     */
    public void updateLocalAppointmentTable() {
        Log.i(TAG, "update Local DB, Insert New Appointments");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String[] stringArray = JsonCollection.jsonToStringArray((this.payload.getString("content")));
        ArrayList<Appointment> appointmentArrayList = JsonCollection.jsonToAppointmentList(stringArray[2]);

        for(Appointment appointment : appointmentArrayList) {
            UserInAppointment userInAppointment = new UserInAppointment(appointment.getAid(), appointment.getGid(), prefs.getString("userid",""),0);
            sqLiteDBHandler.addIsInAppointment(userInAppointment);
            sqLiteDBHandler.addAppointment(appointment);
        }
    }

    /**
     * Insert new User is in group
     * @param i if is joined or not 1 is joined 0 is not
     */
    private void updateLocalUserIsInGroup(int i) {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String uid = prefs.getString("userid", "");
        sqLiteDBHandler.addIsInGroup(JsonCollection.jsonToGroup(this.payload.getString("content")).getGid(),uid, i,0);
    }

    /**
     * Insert new Group Members
     */
    private void updateLocalInsertNewGroupMembers() {
        Log.i(TAG, "update Local DB, Insert New Group Members");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        String[] stringArray = JsonCollection.jsonToStringArray((this.payload.getString("content")));
        ArrayList<User> userList = JsonCollection.jsonToUserList(stringArray[0]);

        ArrayList<UserInGroup> userInGroupList = JsonCollection.jsonToUserInGroupList(stringArray[1]);
        for(User user : userList) {
            sqLiteDBHandler.addUser(user);
        }
        for(UserInGroup userInGroup : userInGroupList) {
            sqLiteDBHandler.addIsInGroup(userInGroup);
        }
    }

    /**
     * Group Insert success
     */
    private void insertSuccess() {
        Intent insertSuccess = new Intent("createdgroup");
        LocalBroadcastManager.getInstance(context).sendBroadcast(insertSuccess);
    }

    /**
     * Send GUI that an error happened
     * @param error
     */
    private void errorGroup(String error) {
        Intent errorGroup = new Intent("ERRORGroup");
        errorGroup.putExtra("error", error);
        LocalBroadcastManager.getInstance(context).sendBroadcast(errorGroup);
    }

    /**
     * Constructs a new GroupObserver and registers it to a MessageSubject
     *
     * @param ms a MessageSubject to register to
     */
    public GroupObserver(MessageSubject ms) {
        ms.registerMessageObserver(this);
        Log.i(TAG, "GroupObserver Registered");
    }

    public void groupArray() {

        String content = this.payload.getString("content");
        ArrayList<de.dataobjects.Groups> grplist = JsonCollection.jsonToGroupList(content);

//        for(Group grp : grplist) {
//            updateLocalGroupTable(grp);
//        }


        Intent gotgrouparray = new Intent("grouparraycomein");
        LocalBroadcastManager.getInstance(context).sendBroadcast(gotgrouparray);
    }
}
