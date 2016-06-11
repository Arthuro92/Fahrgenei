package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.dataobjects.Group;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
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
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMessageObserver(Bundle jsonObject) {
        this.payload = jsonObject;;
        if(this.payload.getString("task_category").equals("group")) {
        switch (this.payload.getString("task")) {
            case "grouparray":
                Log.i(TAG, "first switch task = grouparray");
                groupArray();
                break;
            case "groupmemberjoined": //todo this might be not a valid task
                Log.i(TAG, "second switch task = groupmemberjoined");
                break;
            case "groupinsertsuccess" :
                Log.i(TAG, "Group Insert Sucess");
                updateLocalDatabase(jsonToGroup(this.payload.getString("content")));
                insertSuccess();
                break;
            default :
                if(this.payload.getString("task").startsWith("error")) {
                    Log.i(TAG, "second switch task = ERRORAppointment");
                    errorGroup(this.payload.getString("task"));
                }
                Log.i(TAG,"default case");
        }
        }
    }

    private Group jsonToGroup(String jsonInString) {
        Gson gson = new Gson();
        System.out.println(jsonInString);
        Group grp = gson.fromJson(jsonInString, Group.class);
        return grp;
    }

    private void updateLocalDatabase(Group group) {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addGroup(group);
    }

    private void insertSuccess() {
        Intent insertSuccess = new Intent("createdgroup");
        LocalBroadcastManager.getInstance(context).sendBroadcast(insertSuccess);
    }

    private void errorGroup(String error) {
        Intent errorGroup = new Intent("ERRORGroup");
        errorGroup.putExtra("error", error );
        LocalBroadcastManager.getInstance(context).sendBroadcast(errorGroup);
    }

    /**
     * Constructs a new GroupObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public GroupObserver(MessageSubject ms) {
        ms.registerMessageObserver(this);
        Log.i(TAG, "GroupObserver Registered");
    }

    public void groupArray() {

        Gson gson = new Gson();
        String content = this.payload.getString("content");
        ArrayList<Group> grplist = gson.fromJson(content, new TypeToken<List<Group>>(){}.getType());

//        for(Group grp : grplist) {
//            updateLocalDatabase(grp);
//        }



        Intent gotgrouparray = new Intent("grouparraycomein");
        LocalBroadcastManager.getInstance(context).sendBroadcast(gotgrouparray);
    }
}
