package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import com.dataobjects.Appointment;
import com.dataobjects.JsonCollection;

/**
 * Created by david on 24.05.2016.
 */
@SuppressWarnings("ALL") //todo maybe fix it? or handle it?
public class AppointmentObserver implements MessageObserver {
    //new new
    private Bundle payload;
    private Context context = GlobalAppContext.getAppContext();
    private static final String TAG = "AppointmentObserver";


    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setAppointment method so long as the task_category key of payload equals appointment
     *
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMessageObserver(Bundle jsonObject) {
        this.payload = jsonObject;
        if (this.payload.getString("task_category").equals("appointment")) {
            switch (this.payload.getString("task")) {
                case "singleAppointment":
                    Log.i(TAG, "single Appointment");
                    singleAppointment();
                    break;
                case "appointmentinsertsuccess":
                    Log.i(TAG, "Appointmentinsersuccess");
                    updateLocalDatabase(1);
                    appointmentInsertSuccess();
                    break;
                case "newappointment":
                    Log.i(TAG, "new Appointment received");
                    updateLocalDatabase(0);
                    sendLocalUpdateBroadcast();
                    break;
                default:
                    if (this.payload.getString("task").startsWith("error")) {
                        Log.i(TAG, "ERRORAppointment");
                        errorAppointment(this.payload.getString("task"));
                    }
                    break;
            }
        }
    }

    private void sendLocalUpdateBroadcast() {
        Intent intent = new Intent("update");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void updateLocalDatabase(int isParticipant) {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addAppointment(jsonToAppointment(this.payload.getString("content")), isParticipant);
    }

    private Appointment jsonToAppointment(String jsonInString) {
        return JsonCollection.jsonToAppointment(jsonInString);
    }

    private void appointmentInsertSuccess() {
        Intent errorappointment = new Intent("createdAppointment");
        LocalBroadcastManager.getInstance(context).sendBroadcast(errorappointment);
    }

    private void errorAppointment(String error) {
        Intent errorappointment = new Intent("ERRORAppointment");
        errorappointment.putExtra("error", error);
        LocalBroadcastManager.getInstance(context).sendBroadcast(errorappointment);
    }

    public void singleAppointment() {
        String content = this.payload.getString("content");

//        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
//        prefs.edit().putString("applist", content).apply();

        Intent singleappointment = new Intent("singleAppointment");
        LocalBroadcastManager.getInstance(context).sendBroadcast(singleappointment);
    }


    /**
     * Constructs a new AppointmentObserver and registers it to a MessageSubject
     *
     * @param ms a MessageSubject to register to
     */
    public AppointmentObserver(MessageSubject ms) {
        ms.registerMessageObserver(this);
        System.out.println("APPOINTMENTOBSERVER REGISTERED");
    }
}
