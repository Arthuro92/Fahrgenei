package com.android.cows.fahrgemeinschaft.observer;

import android.os.Bundle;

/**
 * Created by david on 23.05.2016.
 */
public class ChatObserver implements MessageObserver {
    private Bundle payload;

    /**
     *
     */
    public void setChat() {
        System.out.println("CHAT SET TO: " + this.payload.toString());
        System.out.println("MESSAGE SET TO: " + this.payload.getString("content"));
    }

    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setChat method so long as the task_category key of payload equals chat
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMO(Bundle jsonObject) {
        this.payload = jsonObject;
        if(this.payload.getString("task_category").equals("chat")) {
            setChat();
        }
    }

    /**
     * Constructs a new ChatObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public ChatObserver(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("CHATOBSERVER REGISTERED");
    }
}
