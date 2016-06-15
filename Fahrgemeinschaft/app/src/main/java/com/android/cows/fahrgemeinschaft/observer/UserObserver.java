package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import com.dataobjects.User;
import com.google.gson.Gson;

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
                    SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
                    User user = jsonToUser(this.payload.getString("content"));
                    sqLiteDBHandler.addUser(user);
                    break;
                default:
                    Log.i(TAG, "Default Case");
                    break;
            }
        }

    }

    private User jsonToUser(String jsonInString) {
        Gson gson = new Gson();
        User user = gson.fromJson(jsonInString, User.class);
        return user;
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
