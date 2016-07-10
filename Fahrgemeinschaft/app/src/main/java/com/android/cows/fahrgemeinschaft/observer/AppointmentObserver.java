package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

import de.dataobjects.Appointment;
import de.dataobjects.JsonCollection;
import de.dataobjects.UserInAppointment;
import de.dataobjects.UserInGroup;

/**
 * Created by david on 24.05.2016.
 */
@SuppressWarnings("ALL")
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
                case "updatingparticipant":
                    Log.i(TAG, "Updating Participant");
                    updateUserInAppointment();
                    sendLocalUpdateBroadcast();
                    sendLocalParticipantUpdateBroadcast();
                    break;
                case "newdrivers":
                    Log.i(TAG, "New Drivers");
                    updateLocalNewDrivers();
                    break;
                case "deleteappointment":
                    Log.i(TAG, "Delete Appointment");
                    deleteAppointment();
                    sendLocalUpdateBroadcast();
                    sendLocalReturnBroadcast();
                default:
                    if (this.payload.getString("task").startsWith("error")) {
                        Log.i(TAG, "ERRORAppointment");
                        errorAppointment(this.payload.getString("task"));
                    }
                    break;
            }
        }
    }

    /**
     * Delete Appointment local
     */
    private void deleteAppointment() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        Appointment appointment = JsonCollection.jsonToAppointment(this.payload.getString("content"));
        sqLiteDBHandler.deleteAppoinment(appointment.getAid(), appointment.getGid());
    }

    /**
     * Update Local Tables to insert new Drivers correctly
     */
    private void updateLocalNewDrivers() {
        String[] solutionarray = JsonCollection.jsonToStringArray(this.payload.getString("content"));
        ArrayList<UserInGroup> userInGroupArrayListOld = JsonCollection.jsonToUserInGroupList(solutionarray[0]);
        ArrayList<UserInAppointment> userInAppointmentArrayListOld = JsonCollection.jsonToUserInAppointmentList(solutionarray[1]);
        ArrayList<UserInGroup> userInGroupArrayList = JsonCollection.jsonToUserInGroupList(solutionarray[2]);
        ArrayList<UserInAppointment> userInAppointmentArrayList = JsonCollection.jsonToUserInAppointmentList(solutionarray[3]);

        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);

        sqLiteDBHandler.addAppointment(JsonCollection.jsonToAppointment(solutionarray[4]));
        sendLocalUpdateBroadcast();

        for(UserInGroup userInGroup : userInGroupArrayListOld) {
            Log.i(TAG, "Old Driver in group was: " + userInGroup.getUid());
            sqLiteDBHandler.addIsInGroup(userInGroup);
        }
        for(UserInAppointment userInAppointment : userInAppointmentArrayListOld) {
            Log.i(TAG, "Old Driver is Driver: " + userInAppointment.isDriver());
            sqLiteDBHandler.addIsInAppointment(userInAppointment);
        }
        for(UserInGroup userInGroup : userInGroupArrayList) {
            Log.i(TAG, "New Driver in group :" + userInGroup.getUid());
            sqLiteDBHandler.addIsInGroup(userInGroup);
        }
        for(UserInAppointment userInAppointment : userInAppointmentArrayList) {
            Log.i(TAG, "New Driver: " + userInAppointment.getUid());
            Log.i(TAG, "Is now Driver: " + userInAppointment.isDriver());
            sqLiteDBHandler.addIsInAppointment(userInAppointment);
        }

    }

    /**
     * Update UserInAppointment local
     */
    private void updateUserInAppointment() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addIsInAppointment(JsonCollection.jsonToUserInAppointment(this.payload.getString("content")));
    }

    /**
     * Send Local Broadcast to call GUI
     */
    private void sendLocalUpdateBroadcast() {
        Intent intent = new Intent("updategroupappointments");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * Send Local Broadcast to call GUI
     */
    private void sendLocalParticipantUpdateBroadcast() {
        Intent intent = new Intent("updateparticipants");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * Send Local Broadcast to call GUI
     */
    private void sendLocalReturnBroadcast() {
        Intent intent = new Intent("returntogrouptabs");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * Update Local Database when insert/getting new Appointment
     */
    private void updateLocalDatabase() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addAppointment(JsonCollection.jsonToAppointment((this.payload.getString("content"))));
        sqLiteDBHandler.updateUserInAppointment(JsonCollection.jsonToAppointment(this.payload.getString("content")));;
    }

    /**
     * Send Local Broadcast to call GUI
     */
    private void appointmentInsertSuccess() {
        Intent sucessCreateAppointment = new Intent("createdAppointment");
        LocalBroadcastManager.getInstance(context).sendBroadcast(sucessCreateAppointment);
    }

    /**
     * Send Local Broadcast to call GUI if error on serverside happend
     * @param error Error Message
     */
    private void errorAppointment(String error) {
        Intent errorAppointment = new Intent("ERRORAppointment");
        errorAppointment.putExtra("error", error);
        LocalBroadcastManager.getInstance(context).sendBroadcast(errorAppointment);
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
