package com.android.cows.fahrgemeinschaft;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Lennart on 03.05.2016.
 */
public class RegistrationIntentService extends IntentService {


    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    /**
     * Default Konstruktor
     */
    public RegistrationIntentService() {
        super(TAG);
    }

    /**
     * To obtain a token, call instanceID.getToken, providing the app server's sender ID and setting the scope
     * to GoogleCloudMessaging.INSTANCE_ID_SCOPE. Do not call this method in the main thread;
     * instead, use a service that extends IntentService as shown:
     * @param intent 
     */
     @Override
    protected void onHandleIntent(Intent intent) {
         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);


            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * subscribe to new Topic
     * @param token Topic subscribing to
     * @throws IOException
     */
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send any registration to your app's servers.
    }

}