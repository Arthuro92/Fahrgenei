package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import de.dataobjects.Appointment;
import de.dataobjects.JsonCollection;

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
                    updateLocalDatabase();
                    appointmentInsertSuccess();
                    break;
                case "newappointment":
                    Log.i(TAG, "new Appointment received");
                    updateLocalDatabase();
                    sendLocalUpdateBroadcast();
                    break;
                case "updatingparticipantsuccess":
                    Log.i(TAG, "Updating Participant Success");
                    updateUserInAppointment();
                    sendLocalUpdateBroadcast();
                    break;
                case "newdrivers":
                    Log.i(TAG, "New Drivers");
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

    private void updateUserInAppointment() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addIsInAppointment(JsonCollection.jsonToUserInAppointment(this.payload.getString("content")));
    }

    private void sendLocalUpdateBroadcast() {
        Intent intent = new Intent("updategroupappointments");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void updateLocalDatabase() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addAppointment(jsonToAppointment(this.payload.getString("content")));
        sqLiteDBHandler.updateUserInAppointment(jsonToAppointment(this.payload.getString("content")));;
    }

    private Appointment jsonToAppointment(String jsonInString) {
        return JsonCollection.jsonToAppointment(jsonInString);
    }

    private void appointmentInsertSuccess() {
        Intent sucessCreateAppointment = new Intent("createdAppointment");
        LocalBroadcastManager.getInstance(context).sendBroadcast(sucessCreateAppointment);
    }

    private void errorAppointment(String error) {
        Intent errorAppointment = new Intent("ERRORAppointment");
        errorAppointment.putExtra("error", error);
        LocalBroadcastManager.getInstance(context).sendBroadcast(errorAppointment);
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
