/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.cows.fahrgemeinschaft.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.MainActivity;
import com.android.cows.fahrgemeinschaft.R;
import com.android.cows.fahrgemeinschaft.dataobjects.User;
import com.android.cows.fahrgemeinschaft.observer.ChatObserver;
import com.android.cows.fahrgemeinschaft.observer.GroupObserver;
import com.android.cows.fahrgemeinschaft.observer.MessageSubject;
import com.android.cows.fahrgemeinschaft.observer.NotificationObserver;
import com.android.cows.fahrgemeinschaft.observer.UserObserver;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";
    private static MessageSubject ms = new MessageSubject();
    private static UserObserver uo = new UserObserver(ms);
    private static ChatObserver co = new ChatObserver(ms);
    private static GroupObserver go = new GroupObserver(ms);
    private static NotificationObserver no = new NotificationObserver(ms);

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.toString();
        Log.d(TAG, "MESSAGE RECIEVED");
        Log.d(TAG, "FROM: " + from);
        Log.d(TAG, "MESSAGE: " + message);
        ms.setJsonObject(data);

//        Gson gson = new Gson();
//        String jsonInString = data.getString("user");
//        User user = gson.fromJson(jsonInString, User.class);
        //todo non topic message handling
        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }
        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}