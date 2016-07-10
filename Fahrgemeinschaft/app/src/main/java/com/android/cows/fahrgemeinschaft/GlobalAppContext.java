package com.android.cows.fahrgemeinschaft;

import android.app.Application;
import android.content.Context;

/**
 * Created by david on 28.05.2016.
 * provides an Global App Context which exists as long as the App is running
 * is good for the need of long living contexts
 */
public class GlobalAppContext extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        GlobalAppContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return GlobalAppContext.context;
    }
}
