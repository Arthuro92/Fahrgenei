package com.android.cows.fahrgemeinschaft.observer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.FragmentGruppenChatActivity;
import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.GroupTabsActivity;
import com.android.cows.fahrgemeinschaft.R;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import de.dataobjects.Chat;
import de.dataobjects.Groups;
import de.dataobjects.JsonCollection;

//import com.android.cows.fahrgemeinschaft.ChatActivity;


/**
 * Created by david on 23.05.2016.
 */
public class ChatObserver implements MessageObserver {
    //version 1
    private static final String TAG = "ChatObserver";
    private static final int NID = 987654321;
    private Context context = GlobalAppContext.getAppContext();
    private Bundle payload;

    /**
     * Sets the intent to launch ChatActivity
     *
     * @param chatMessage a Chat object to be added as Extra
     * @return Intent that launches ChatActivity
     */
    private Intent setChatIntent(de.dataobjects.Chat chatMessage) {
        Intent i = new Intent();
        i.putExtra("Chat", chatMessage);
        i.setAction("com.android.cows.fahrgemeinschaft.UPDATECHAT");
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        return i;
    }

    /**
     * Sets and issues a Notification concerning the contents of the jsonObject
     *
     * @param i an Intent that is triggered on Notification click
     */
    private void issueNotification(Intent i) {
        PendingIntent pIntent = PendingIntent.getActivity(this.context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nm = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncb = new NotificationCompat.Builder(context);
        ncb.setSmallIcon(R.drawable.ic_stat_ic_notification);

        Chat chat = JsonCollection.jsonToChat(this.payload.getString("content"));
        Groups groups = getGroup(chat.getGid());
//        ncb.setContentTitle(this.payload.getString("task_category"));
        ncb.setContentTitle(groups.getName());
        ncb.setContentText(chat.getChatMessageFrom() +  ": " + chat.getChatMessageText());
        ncb.setWhen(System.currentTimeMillis());
        ncb.setContentIntent(pIntent);
        nm.notify(NID, ncb.build());
        Log.i(TAG, "NOTIFICATION SET");
    }

    /**
     * Gets the User by accessing the shared preferences
     *
     * @return user String
     */
    private String getChatUser() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "Blubb");
    }

    private void setCurrentGroup(String gid) {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        Groups groups = getGroup(gid);

        sharedPreferences.edit().putString("currentgid", gid).apply();
        sharedPreferences.edit().putString("currentgroupname", groups.getName()).apply();
        sharedPreferences.edit().putString("currentgroupadminid", groups.getAdminid()).apply();
    }

    private Groups getGroup(String gid) {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context,null);
        return sqLiteDBHandler.getGroup(gid);
    }
    /**
     * Parses certain parts of the jsonObject to a Chat object
     *
     * @param jsonInString a Json String to be parsed
     * @return a resulting Chat object
     */
    private Chat setChatMessage(String jsonInString) {
        return JsonCollection.jsonToChat(jsonInString);
    }

    /**
     * Adds a chatMessage to the local database
     *
     * @param chatMessage Chat object to be added to database
     */
    private void updateLocalDatabase(de.dataobjects.Chat chatMessage) {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addChatMessage(chatMessage);
    }

    /**
     * Handles chat relevant data and notifies
     *
     * @param chatMessage a Chat object to be handled
     */
    public void setInfoAndData(de.dataobjects.Chat chatMessage) {
        Log.i(TAG, "CHAT MESSAGE: " + chatMessage.getChatMessageText());
        if (!chatMessage.getChatMessageFrom().equals(getChatUser()) && !FragmentGruppenChatActivity.activeActivity || !FragmentGruppenChatActivity.gid.equals(chatMessage.getGid())) {
            updateLocalDatabase(chatMessage);
            setCurrentGroup(chatMessage.getGid());
            issueNotification(new Intent(context, GroupTabsActivity.class).putExtra("calledfromchatobserver","calledfromchatobserver"));
            Log.i(TAG, "ACTIVE ACTIVITY STATUS: " + FragmentGruppenChatActivity.activeActivity);
        } else if (!chatMessage.getChatMessageFrom().equals(getChatUser()) && FragmentGruppenChatActivity.activeActivity && FragmentGruppenChatActivity.gid.equals(chatMessage.getGid())) {
            updateLocalDatabase(chatMessage);
            context.sendBroadcast(setChatIntent(chatMessage));
            Log.i(TAG, "ACTIVE ACTIVITY STATUS: " + FragmentGruppenChatActivity.activeActivity);
        }
    }

    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setChat method so long as the task_category key of payload equals chat
     *
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMessageObserver(Bundle jsonObject) {
        this.payload = jsonObject;
        if (this.payload.getString("task_category").equals("chat")) {
            setInfoAndData(setChatMessage(this.payload.getString("content")));
        }
    }

    /**
     * Constructs a new ChatObserver and registers it to a MessageSubject
     *
     * @param messageSubject a MessageSubject to register to
     */
    public ChatObserver(MessageSubject messageSubject) {
        messageSubject.registerMessageObserver(this);
        Log.i(TAG, "CHATOBSERVER REGISTERED");
    }
}
