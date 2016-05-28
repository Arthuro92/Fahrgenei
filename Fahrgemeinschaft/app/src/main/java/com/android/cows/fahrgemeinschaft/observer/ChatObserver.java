package com.android.cows.fahrgemeinschaft.observer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.android.cows.fahrgemeinschaft.ChatActivity;
import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.R;
import com.android.cows.fahrgemeinschaft.dataobjects.Chat;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by david on 23.05.2016.
 */
public class ChatObserver implements MessageObserver {
    private static final int NID = 987654321;
    private Context con = GlobalAppContext.getAppContext();
    private Bundle payload;
    private Chat c;

    /**
     * Sets the intent to launch ChatActivity
     * @return Intent that launches ChatActivity
     */
    private Intent setChatIntent() {
        Intent i = new Intent(con, ChatActivity.class);
        i.putExtra("Chat", this.c);
        return i;
    }

    /**
     * Sets and issues a Notification concerning the contents of the jsonObject
     * @param i an Intent that is triggered on Notification click
     */
    private void issueNotification(Intent i) {
        System.out.println("NOTIFICATION: " + this.payload.toString() + " CONTENT: " + this.payload.getString("content"));
        PendingIntent pIntent = PendingIntent.getActivity(con, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nm = (NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncb = new NotificationCompat.Builder(con);
        ncb.setSmallIcon(R.drawable.ic_stat_ic_notification);
        ncb.setContentTitle(this.payload.getString("task_category"));
        ncb.setContentText(this.payload.getString("content"));
        ncb.setWhen(System.currentTimeMillis());
        ncb.setContentIntent(pIntent);
        nm.notify(NID, ncb.build());
    }

    /**
     * Parses certain parts of the jsonObject to a Chat object and issues a Notification
     */
    public void setChat() {
        System.out.println("CHAT: " + this.payload.toString() + " MESSAGE: " + this.payload.getString("content"));
        Gson gson = new Gson();
        String jsonInString = this.payload.getString("content");
        this.c = gson.fromJson(jsonInString, Chat.class);
        //todo variable with user id email name
        if(!this.c.getChatMessageFrom().equals("Zon")) {
            issueNotification(setChatIntent());
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
