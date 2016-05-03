package com.android.cows.fahrgemeinschaft;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Lennart on 02.05.2016.
 * handle the creation, rotation, and updating of registration tokens.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    /**
     * The listener service's onTokenRefresh method should be invoked if the GCM registration token has been refreshed:
     */
    @Override
    public void onTokenRefresh() {
        //fetch updated Instance ID token and notify our app´s server of any changes (if applicable)
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
