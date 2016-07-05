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

import android.os.Bundle;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.observer.AppointmentObserver;
import com.android.cows.fahrgemeinschaft.observer.ChatObserver;
import com.android.cows.fahrgemeinschaft.observer.GroupObserver;
import com.android.cows.fahrgemeinschaft.observer.MessageSubject;
import com.android.cows.fahrgemeinschaft.observer.SecurityObserver;
import com.android.cows.fahrgemeinschaft.observer.TaskObserver;
import com.android.cows.fahrgemeinschaft.observer.UserObserver;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";
    private static MessageSubject ms = new MessageSubject();
    private static SecurityObserver securityObserver = new SecurityObserver(ms);
    private static UserObserver uo = new UserObserver(ms);
    private static ChatObserver co = new ChatObserver(ms);
    private static TaskObserver taskObserver = new TaskObserver(ms);
    private static AppointmentObserver ao = new AppointmentObserver(ms);
    private static GroupObserver go = new GroupObserver(ms);

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, "MESSAGE RECIEVED");
        Log.d(TAG, "FROM: " + from);
        Log.d(TAG, "MESSAGE: " + data.toString());
        ms.setJsonObject(data);

        //todo non topic message handling
        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }
    }
}
