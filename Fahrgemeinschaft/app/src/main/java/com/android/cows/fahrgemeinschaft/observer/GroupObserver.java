package com.android.cows.fahrgemeinschaft.observer;

import java.util.Map;

/**
 * Created by david on 23.05.2016.
 */
public class GroupObserver implements MessageObserver {
    private Map<String, String> payload;
    public void setGroup() {
        System.out.println("Group set to: ");
    }
    public void updateMO(Map<String, Object> jsonObject) {
        if(jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if(this.payload.get("task_category").equals("group")) {
                setGroup();
            }
        }
    }
    public GroupObserver(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("GroupObserver registered!");
    }
}
