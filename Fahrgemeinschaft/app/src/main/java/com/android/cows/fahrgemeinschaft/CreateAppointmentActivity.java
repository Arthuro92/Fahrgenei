package com.android.cows.fahrgemeinschaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import com.dataobjects.Appointment;

import java.util.Date;

public class CreateAppointmentActivity extends AppCompatActivity {
    private static final String TAG = "CreateGroupActivity";
    private BroadcastReceiver insertAppointmentsuccess;
    private BroadcastReceiver errorReceivingAppointment;
    private ProgressBar mRegistrationProgressBar;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.createAppointmentProgBar);
        mRegistrationProgressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

    }

    public void createAppointment(View view) {

        setLayoutInvisible();

        Log.i(TAG, "Create Appointment");

        MyGcmSend gcmsend = new MyGcmSend();

        SharedPreferences prefs = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String gid = prefs.getString("currentgid", "");
        String gname = prefs.getString("currentgroupname", "");

        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);
        int id = sqLiteDBHandler.getNextAppointmentID(gid);
        Appointment gapm1;

        if(id == 0) {
            Log.i(TAG, "no appointments, create appointment with id 1");
            gapm1 = new Appointment(1, gid, gname + " " + 1, new Date(2016, 10, 10, 10, 00), new Date(2016, 10, 10, 9, 45), "Uni", "Wolfsburg", 1);
        } else {
            id ++;
            Log.i(TAG, "Create Appointment with id " + id);
            gapm1 = new Appointment(id, gid, gname + " " + id, new Date(2016, 10, 10, 10, 00), new Date(2016, 10, 10, 9, 45), "Uni", "Wolfsburg", 1);
        }
        gcmsend.send("appointment", "insertappointment", gapm1, this);

        createReceiver();

    }

    @SuppressWarnings("ConstantConditions")
    private void setLayoutInvisible() {
        findViewById(R.id.createAppointmentProgBar).setVisibility(ProgressBar.VISIBLE);
        findViewById(R.id.button4).setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("ConstantConditions")
    private void setLayoutVisible() {
        findViewById(R.id.createAppointmentProgBar).setVisibility(ProgressBar.GONE);
        findViewById(R.id.button4).setVisibility(View.VISIBLE);
    }


    private void createReceiver() {
        insertAppointmentsuccess = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver();
                Intent intent2 = new Intent(CreateAppointmentActivity.this, GroupTabsActivity.class);
                Bundle bundle = getIntent().getExtras();
                startActivity(intent2);
            }
        };

        errorReceivingAppointment = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                CharSequence text = "" + bundle.get("error");
                Toast toast = Toast.makeText(CreateAppointmentActivity.this, text, Toast.LENGTH_LONG);
                toast.show();
                setLayoutVisible();
                unregisterReceiver();
            }
        };
        registerReceiver();
    }


    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(insertAppointmentsuccess, new IntentFilter("createdAppointment"));
            LocalBroadcastManager.getInstance(this).registerReceiver(errorReceivingAppointment, new IntentFilter("ERRORAppointment"));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(insertAppointmentsuccess);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(errorReceivingAppointment);
            isReceiverRegistered = false;
        }
        //todo do we need unregistering for receiver?
    }
}
