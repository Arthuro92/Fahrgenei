package com.android.cows.fahrgemeinschaft.observer;

import android.os.Bundle;

/**
 * Created by david on 23.05.2016.
 */
public class GroupObserver implements MessageObserver {
    private Bundle payload;

    /**
     *
     */
    public void setGroup() {
        System.out.println("GROUP SET TO: ");
    }

    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setGroup method so long as the task_category key of payload equals group
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMO(Bundle jsonObject) {
        this.payload = jsonObject;
        if(this.payload.getString("task_category").equals("user")) {
            setGroup();
        }
    }

    /**
     * Constructs a new GroupObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public GroupObserver(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("GROUPOBSERVER REGISTERED");
    }
}
