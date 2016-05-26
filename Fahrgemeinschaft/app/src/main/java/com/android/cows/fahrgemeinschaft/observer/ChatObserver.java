package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.os.Bundle;
import com.android.cows.fahrgemeinschaft.ChatActivity;
import com.android.cows.fahrgemeinschaft.dataobjects.Chat;
import com.google.gson.Gson;

/**
 * Created by david on 23.05.2016.
 */
public class ChatObserver implements MessageObserver {
    private Bundle payload;
    private static Context con; //todo default context for android? similar to NOTIFICATIN_SERVICE
    private Chat c;

    /**
     * Sets the Context for the ChatObserver class(not instantiated) to the Context of the Activity this method is called from
     * @param c a Context from the Activity this method is called from
     */
    public static void setContext(Context c) {
        con = c;
    }

    /**
     * Parses certain parts of the jsonObject to a Chat object and adds it to the ChatActivity
     */
    public void setChat() {
        System.out.println("CHAT SET TO: " + this.payload.toString());
        System.out.println("MESSAGE SET TO: " + this.payload.getString("content"));
        Gson gson = new Gson();
        String jsonInString = this.payload.getString("content");
        this.c = gson.fromJson(jsonInString, Chat.class);
        //todo variable with user id email name
        if(!this.c.getChatMessageFrom().equals("Zon")) {
            ChatActivity.setCalFromObserver(this.c);
        }
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
