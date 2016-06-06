package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.google.gson.Gson;

/**
 * Created by david on 24.05.2016.
 */
@SuppressWarnings("ALL") //todo maybe fix it? or handle it?
public class AppointmentObserver implements MessageObserver {
    //new new
    private Bundle payload;
    private Context con = GlobalAppContext.getAppContext();
    private static final String TAG = "AppointmentObserver";


    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setAppointment method so long as the task_category key of payload equals appointment
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMessageObserver(Bundle jsonObject) {
        this.payload = jsonObject;
        if (this.payload.getString("task_category").equals("appointment")) {
            switch (this.payload.getString("task")) {
                case "singleAppointment":
                    Log.i(TAG, "first switch task = grouparray");
                    singleAppointment();
                    break;
                default:
                    if(this.payload.getString("task").startsWith("error")) {
                        Log.i(TAG, "second switch task = ERRORAppointment");
                        errorAppointment(this.payload.getString("task"));
                    }
                    break;
            }
        }
    }

    private void errorAppointment(String error) {
        Intent errorappointment = new Intent("ERRORAppointment");
        errorappointment.putExtra("error", error );
        LocalBroadcastManager.getInstance(con).sendBroadcast(errorappointment);
    }

    public void singleAppointment() {
        Gson gson = new Gson();
        String content = this.payload.getString("content");
        SharedPreferences prefs = con.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        prefs.edit().putString("applist", content).apply();

        Intent singleappointment = new Intent("singleAppointment");
        LocalBroadcastManager.getInstance(con).sendBroadcast(singleappointment);
    }


    /**
     * Constructs a new AppointmentObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public AppointmentObserver(MessageSubject ms) {
        ms.registerMessageObserver(this);
        System.out.println("APPOINTMENTOBSERVER REGISTERED");
    }
}
