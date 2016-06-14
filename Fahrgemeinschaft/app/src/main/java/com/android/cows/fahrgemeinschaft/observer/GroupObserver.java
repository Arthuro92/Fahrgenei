package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.gcm.TopicSubscriber;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import com.dataobjects.Group;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

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
     *
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMessageObserver(Bundle jsonObject) {
        this.payload = jsonObject;
        ;
        if (this.payload.getString("task_category").equals("group")) {
            switch (this.payload.getString("task")) {
                case "grouparray":
                    Log.i(TAG, "GroupArray");
                    groupArray();
                    break;
                case "invitationsuccess":
                    Log.i(TAG, "Invitation Success");
                    invitationSuccess();
                    break;
                case "groupinsertsuccess":
                    Log.i(TAG, "Group Insert Sucess");
                    updateLocalDatabase();
                    TopicSubscriber.subscribeToTopic(jsonToGroup(this.payload.getString("content")).getGid());
                    insertSuccess();
                    break;
                case "groupinvitation":
                    Log.i(TAG, "Groupinvitation");
                    groupInvitation();
                    //todo request to user if he wants to join this group
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

    private void invitationSuccess() {
        Intent insertSuccess = new Intent("invitesuccess");
        LocalBroadcastManager.getInstance(context).sendBroadcast(insertSuccess);
    }

    private void groupInvitation() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        Group grp = jsonToGroup(this.payload.getString("content"));
        sqLiteDBHandler.addGroup(grp);
        System.out.println("ADDING GROUP");
        sendInvitationAccept();
        sendLocalUpdateBroadcast();

    }

    private void sendLocalUpdateBroadcast() {
        Intent intent = new Intent("update");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void sendInvitationAccept() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        Group grp = jsonToGroup(this.payload.getString("content"));
        grp.setJoined(1);
        sqLiteDBHandler.joinGroup(grp);
        MyGcmSend myGcmSend = new MyGcmSend();
        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String[] string = new String[2];
        string[0] = grp.getGid();
        string[1] = prefs.getString("userid", "");
        TopicSubscriber.subscribeToTopic(grp.getGid());
        myGcmSend.send("group", "invitationaccept", context, string);
    }

    private Group jsonToGroup(String jsonInString) {
        Gson gson = new Gson();
        Group grp = gson.fromJson(jsonInString, Group.class);
        return grp;
    }

    private void updateLocalDatabase() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addGroup(jsonToGroup(this.payload.getString("content")));
    }

    private void insertSuccess() {
        Intent insertSuccess = new Intent("createdgroup");
        LocalBroadcastManager.getInstance(context).sendBroadcast(insertSuccess);
    }

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

        Gson gson = new Gson();
        String content = this.payload.getString("content");
        ArrayList<Group> grplist = gson.fromJson(content, new TypeToken<List<Group>>() {
        }.getType());

//        for(Group grp : grplist) {
//            updateLocalDatabase(grp);
//        }


        Intent gotgrouparray = new Intent("grouparraycomein");
        LocalBroadcastManager.getInstance(context).sendBroadcast(gotgrouparray);
    }
}
