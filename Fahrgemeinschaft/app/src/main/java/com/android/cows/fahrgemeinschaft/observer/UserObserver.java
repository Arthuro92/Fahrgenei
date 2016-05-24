package com.android.cows.fahrgemeinschaft.observer;

import android.os.Bundle;

/**
 * Created by david on 23.05.2016.
 */
public class UserObserver implements MessageObserver {
    private Bundle payload;

    /**
     *
     */
    public void setUser() {
        System.out.println("USER SET TO: ");
    }

    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setUser method so long as the task_category key of payload equals user
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMO(Bundle jsonObject) {
        this.payload = jsonObject;
            if(this.payload.getString("task_category").equals("user")) {
                setUser();
            }
    }

    /**
     * Constructs a new UserObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public UserObserver(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("USEROBSERVER REGISTERED");
    }
}
