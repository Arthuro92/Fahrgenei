package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import com.dataobjects.JsonCollection;
import com.dataobjects.User;
import com.dataobjects.UserInGroup;

/**
 * Created by david on 23.05.2016.
 */
public class UserObserver implements MessageObserver {
    //new new
    private Bundle payload;
    private Context context = GlobalAppContext.getAppContext();
    private static final String TAG = "UserObserver";


    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setUser method so long as the task_category key of payload equals user
     *
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMessageObserver(Bundle jsonObject) {
        this.payload = jsonObject;
        if (this.payload.getString("task_category").equals("user")) {
            switch (this.payload.getString("task")) {
                case "newuser":
                    Log.i(TAG, "NewUser");
                    addNewUser();
                    break;
                case "userjoinedgroup":
                    Log.i(TAG, "UserJoinedGroup");
                    userJoinedGroup();
                    break;
                default:
                    Log.i(TAG, "Default Case");
                    break;
            }
        }

    }

    private void userJoinedGroup() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addIsInGroup(JsonCollection.jsonToUserInGroup(this.payload.getString("content")));
        sendLocalUpdateBroadcast();
    }

    private void addNewUser() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        String[] stringarray = JsonCollection.jsonToStringArray(this.payload.getString("content"));
        User user = JsonCollection.jsonToUser(stringarray[0]);
        UserInGroup userinGroup = JsonCollection.jsonToUserInGroup(stringarray[1]);

        sqLiteDBHandler.addUser(user);
        sqLiteDBHandler.addIsInGroup(userinGroup);
        sendLocalUpdateBroadcast();
    }

    private void sendLocalUpdateBroadcast() {
        Intent intent = new Intent("update");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * Constructs a new UserObserver and registers it to a MessageSubject
     *
     * @param ms a MessageSubject to register to
     */
    public UserObserver(MessageSubject ms) {
        ms.registerMessageObserver(this);
        System.out.println("USEROBSERVER REGISTERED");
    }
}
