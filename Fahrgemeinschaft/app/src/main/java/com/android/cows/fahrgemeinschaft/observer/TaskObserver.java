package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import de.dataobjects.JsonCollection;
import de.dataobjects.Task;

/**
 * Created by Lennart on 30.06.2016.
 */
public class TaskObserver implements MessageObserver {
    private Bundle payload;
    private Context context = GlobalAppContext.getAppContext();
    private static final String TAG = "TaskObserver";


    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setAppointment method so long as the task_category key of payload equals appointment
     *
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMessageObserver(Bundle jsonObject) {
        this.payload = jsonObject;
        if (this.payload.getString("task_category").equals("task")) {
            switch (this.payload.getString("task")) {
                case "broadcastedtask":
                    Log.i(TAG, "Received new task");
                    addTask();
                    sendLocalUpdateBroadcast();
                    break;
                case "deletetask":
                    Log.i(TAG, "Delete Task");
                    deleteTask();
                    sendLocalUpdateBroadcast();
                    sendLocalReturnBroadcast();
                    break;
                default:
                    if (this.payload.getString("task").startsWith("error")) {
                        Log.i(TAG, "ERRORTask");
                        errorTask(this.payload.getString("task"));
                    }
                    break;
            }
        }
    }
    /**
     * Send Local Broadcast to call GUI
     */
    private void sendLocalReturnBroadcast() {
        Intent intent = new Intent("returntogeneralgroups");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * Delete Task local
     */
    private void deleteTask() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        Task task = JsonCollection.jsonToTask(this.payload.getString("content"));
        sqLiteDBHandler.deleteTask(task.getTaskId(), task.getGid(), task.getAid());
    }

    /**
     * Send Local Broadcast to call GUI
     */
    private void sendLocalUpdateBroadcast() {
        Intent createdtasksuccess = new Intent("createdTask");
        LocalBroadcastManager.getInstance(context).sendBroadcast(createdtasksuccess);
    }

    /**
     * Add task local
     */
    private void addTask() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        sqLiteDBHandler.addTask(JsonCollection.jsonToTask(this.payload.getString("content")));
    }

    /**
     * Send local Broadcast to call GUI if Error on Serverside happend
     * @param error Error Message
     */
    private void errorTask(String error) {
        Intent erroratask = new Intent("ERRORTask");
        erroratask.putExtra("error", error);
        LocalBroadcastManager.getInstance(context).sendBroadcast(erroratask);
    }

    /**
     * Constructs a new AppointmentObserver and registers it to a MessageSubject
     *
     * @param ms a MessageSubject to register to
     */
    public TaskObserver(MessageSubject ms) {
        ms.registerMessageObserver(this);
        System.out.println("APPOINTMENTOBSERVER REGISTERED");
    }
}