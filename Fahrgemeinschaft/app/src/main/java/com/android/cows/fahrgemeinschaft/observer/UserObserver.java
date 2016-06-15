package com.android.cows.fahrgemeinschaft.observer;

import android.os.Bundle;

/**
 * Created by david on 23.05.2016.
 */
public class UserObserver implements MessageObserver {
    //new new
    private Bundle payload;
    private static final String TAG = "UserObserver";


    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setUser method so long as the task_category key of payload equals user
     *
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMessageObserver(Bundle jsonObject) {
        this.payload = jsonObject;
        if (this.payload.getString("task_category").equals("user")) {
        }
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
