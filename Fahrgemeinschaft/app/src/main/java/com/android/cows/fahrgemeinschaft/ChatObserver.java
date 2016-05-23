package com.android.cows.fahrgemeinschaft;

import java.util.Map;

/**
 * Created by david on 23.05.2016.
 */
public class ChatObserver implements MessageObserver {
    private Map<String, String> payload;
    public void setChat() {
        System.out.println("Message set to: ");
    }
    public void updateMO(Map<String, Object> jsonObject) {
        if(jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if(this.payload.get("task_category").equals("chat")) {
                setChat();
            }
        }
    }
    public ChatObserver(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("ChatObserver registered!");
    }
}
