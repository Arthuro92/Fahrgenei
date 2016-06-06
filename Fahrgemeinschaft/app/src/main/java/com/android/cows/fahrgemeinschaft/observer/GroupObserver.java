package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.google.gson.Gson;

/**
 * Created by david on 23.05.2016.
 */
public class GroupObserver implements MessageObserver {
    //new new
    private Bundle payload;
    private Context con = GlobalAppContext.getAppContext();
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


                Gson gson = new Gson();
                String content = this.payload.getString("content");
                SharedPreferences prefs = con.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                prefs.edit().putString("grplist" ,content).apply();

                Intent gotgrouparray = new Intent("grouparraycomein");
                LocalBroadcastManager.getInstance(con).sendBroadcast(gotgrouparray);
                break;
            case "groupmemberjoined": //todo this might be not a valid task
                Log.i(TAG, "second switch task = groupmemberjoined");
                break;
            default :
                Log.i(TAG,"default case");
        }
        }
    }

    /**
     * Constructs a new GroupObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public GroupObserver(MessageSubject ms) {
        ms.registerMessageObserver(this);
        System.out.println("GROUPOBSERVER REGISTERED");
    }
}
