package com.android.cows.fahrgemeinschaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by david on 11.06.2016.
 */
public class ChatReceiver extends BroadcastReceiver {
    //new new new new
    ChatActivity chatActivity;

    /**
     * Sets chat message history via ChatActivity
     * @param context a Context of the ChatReceiver
     * @param intent a received Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        this.chatActivity.setArrayListFromExtra(intent);
    }

    /**
     * Constructs a ChatReceiver to handle chat messages
     * @param chatActivity the ChatActivity this ChatReceiver is referring to
     */
    public ChatReceiver(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;
    }
}
