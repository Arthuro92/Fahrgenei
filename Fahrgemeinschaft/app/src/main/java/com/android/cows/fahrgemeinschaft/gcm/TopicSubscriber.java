package com.android.cows.fahrgemeinschaft.gcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;


/**
 * Created by Lennart on 14.06.2016.
 */
public class TopicSubscriber {
    private static final String TAG = "TopicSubscriber";

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     */
    // [START subscribe_topics]
    public static void subscribeToTopic(String topic) {
        FirebaseMessaging pubSub = FirebaseMessaging.getInstance();
        pubSub.subscribeToTopic(topic);
        Log.i(TAG, "Subscribe to Topic: " + topic);
    }

    public static void unsubscribeFromTopic(String topic) {
        FirebaseMessaging pubSub = FirebaseMessaging.getInstance();
        pubSub.unsubscribeFromTopic(topic);
        Log.i(TAG, "Unsubscribe from Topic: " + topic);
    }
}
