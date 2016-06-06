package com.android.cows.fahrgemeinschaft.gcm;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Lennart on 13.05.2016.
 * A Generic Class for sending java Objects to GCM Server
 */

public class MyGcmSend<T> {

    private static final String TAG = "MyGcmSend";

    /**
     *
     * Sending messages to GCM
     * @param task_category valid categorys: chat, group, appointment, user
     * @param task valid tasks //todo choose valid tasks
     * @param javaobject every valid javaobject
     * @param extras use this field for ids, or something which you want to write in database, Can be NULL!
     * @param con the context try this, as context
     */
    public void send(String task_category, String task, T javaobject, Context con, String[] extras) {



        // Add custom implementation, as needed.
        final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(con);
        final String senderId = con.getString(R.string.gcm_defaultSenderId);
        final String msgId = nextMessageId();

        Log.i(TAG, "Try Sending Message");

        String logstring = "";
        try {
            Bundle payload = new Bundle();
            payload.putString("task_category", task_category);
            payload.putString("task", task);

            Gson gson = new Gson();
            String javaobjectstring = gson.toJson(javaobject);
            payload.putString("content", javaobjectstring);

            if(extras != null) {
                int i = 0;
                while(i < extras.length) {
                    payload.putString("extra"+i, extras[i]);
                    i++;
                }
            }

            gcm.send(senderId + "@gcm.googleapis.com", msgId, payload);
            logstring = "Sent message success";
        } catch (IOException ex) {
            logstring = "Error :" + ex.getMessage();
        }
        Log.i(TAG, logstring);
    }

    /**
     * Use this for Requests
     * Sending messages to GCM
     * @param task_category valid categorys: chat, group, appointment, user
     * @param task valid tasks //todo choose valid tasks
     * @param con the context try this, as context
     * @param extras use this field for ids, or something which you want to write in database, Can be NULL!
     */
    public void send(String task_category, String task, Context con, String[] extras) {



        // Add custom implementation, as needed.
        final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(con);
        final String senderId = con.getString(R.string.gcm_defaultSenderId);
        final String msgId = nextMessageId();

        Log.i(TAG, "Try Sending Message");

        String logstring = "";
        try {
            Bundle payload = new Bundle();
            payload.putString("task_category", task_category);
            payload.putString("task", task);

            if(extras != null) {
                int i = 0;
                while(i < extras.length) {
                    payload.putString("extra"+i, extras[i]);
                    i++;
                }
            }

            gcm.send(senderId + "@gcm.googleapis.com", msgId, payload);
            logstring = "Sent message success";
        } catch (IOException ex) {
            logstring = "Error :" + ex.getMessage();
        }
        Log.i(TAG, logstring);
    }




    //todo change random id so it is actually unique
    /**
     * Returns a random message id to uniquely identify a message.
     * <p>
     * <p>Note: This is generated by a pseudo random number generator for
     * illustration purpose, and is not guaranteed to be unique.
     */
    public String nextMessageId() {
        return "m-" + UUID.randomUUID().toString();
    }
}