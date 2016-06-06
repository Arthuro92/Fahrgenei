package com.android.cows.fahrgemeinschaft.observer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.ChatActivity;
import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.R;
import com.android.cows.fahrgemeinschaft.dataobjects.Chat;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * Created by david on 23.05.2016.
 */
public class ChatObserver implements MessageObserver {
    //new
    private static final int NID = 987654321;
    private Context context = GlobalAppContext.getAppContext();
    private Bundle payload;
    private static final String TAG = "ChatObserver";

    /**
     * Sets the intent to launch ChatActivity
     * @param chatMessage a Chat object to be added as Extra
     * @return Intent that launches ChatActivity
     */
    private Intent setChatIntent(Chat chatMessage) {
        Intent i = new Intent(this.context, ChatActivity.class);
        i.putExtra("Chat", chatMessage);
        return i;
    }

    /**
     * Sets and issues a Notification concerning the contents of the jsonObject
     * @param i an Intent that is triggered on Notification click
     */
    private void issueNotification(Intent i) {
        PendingIntent pIntent = PendingIntent.getActivity(this.context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nm = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncb = new NotificationCompat.Builder(context);
        ncb.setSmallIcon(R.drawable.ic_stat_ic_notification);
        ncb.setContentTitle(this.payload.getString("task_category"));
        ncb.setContentText(this.payload.getString("content"));
        ncb.setWhen(System.currentTimeMillis());
        ncb.setContentIntent(pIntent);
        nm.notify(NID, ncb.build());
        System.out.println("NOTIFICATION SET");
    }

    /**
     * Gets the User by accessing the shared preferences
     * @return user String
     */
    private String getChatUser() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "Blubb");
    }

    /**
     * Parses certain parts of the jsonObject to a Chat object
     * @param jsonInString a Json String to be parsed
     * @return a resulting Chat object
     */
    private Chat setChatMessage(String jsonInString){
        Gson gson = new Gson();
        return gson.fromJson(jsonInString, Chat.class);
    }

    private void updateLocalDatabase(Chat chatMessage) {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addChatMessage(chatMessage);
    }

    /**
     * Handles chat relevant data and notifies
     * @param chatMessage a Chat object to be handled
     */
    public void setInfoAndData(Chat chatMessage) {

        Log.i(TAG, "Chat Message: " + chatMessage.getChatMessageText());
        if(!chatMessage.getChatMessageFrom().equals(getChatUser()) && !ChatActivity.activeActivity) {
            updateLocalDatabase(chatMessage);
            issueNotification(setChatIntent(chatMessage));

            Log.i(TAG,"ACTIVE ACTIVITY STATUS: " + ChatActivity.activeActivity);
        } else if(!chatMessage.getChatMessageFrom().equals(getChatUser()) && ChatActivity.activeActivity) {
//            updateLocalDatabase(chatMessage);
            this.context.startActivity(setChatIntent(chatMessage).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            Log.i(TAG,"ACTIVE ACTIVITY STATUS: " + ChatActivity.activeActivity);
        }
    }

    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setChat method so long as the task_category key of payload equals chat
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMO(Bundle jsonObject) {
        this.payload = jsonObject;
        if(this.payload.getString("task_category").equals("chat")) {
            setInfoAndData(setChatMessage(this.payload.getString("content")));
        }
        System.out.println("CHATOBSERVER ONUPDATE");
    }

    /**
     * Constructs a new ChatObserver and registers it to a MessageSubject
     * @param messageSubject a MessageSubject to register to
     */
    public ChatObserver(MessageSubject messageSubject) {
        messageSubject.registerMO(this);
        System.out.println("CHATOBSERVER REGISTERED");
    }
}
