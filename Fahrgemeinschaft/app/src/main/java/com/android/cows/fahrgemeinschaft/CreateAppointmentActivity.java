package com.android.cows.fahrgemeinschaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import com.dataobjects.Appointment;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

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

        Bundle bundle = getIntent().getExtras();

        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);
        int id = sqLiteDBHandler.getNextAppointmentID(bundle.getString("gid"));
        Appointment gapm1;

        EditText _input_treffpunktZeit = (EditText) findViewById(R.id.input_treffpunktZeit);
        EditText _input_treffpunkt = (EditText) findViewById(R.id.input_treffpunkt);
        EditText _input_abfahrtzeit = (EditText) findViewById(R.id.input_abfahrtzeit);
        EditText _input_zielort = (EditText) findViewById(R.id.input_zielort);

        //Todo String von Abfahrtzeit & Treffpunktzeit in Calendar ändern oder ähnliches
        String treffpunkt = _input_treffpunkt.getText().toString();
        String zielort = _input_zielort.getText().toString();
        String treffpunktZeit = _input_treffpunktZeit.getText().toString();
        String abfahrtzeit = _input_abfahrtzeit.getText().toString();


        Calendar abfahrtt_zeit4 = Calendar.getInstance();
        abfahrtt_zeit4.set(2016, 10, 10, 10, 00);
        Calendar treff_zeit4 = Calendar.getInstance();
        abfahrtt_zeit4.set(2016, 10, 10, 10, 00);

        if(id == 0) {
            Log.i(TAG, "no appointments, create appointment with id 1");
            gapm1 = new Appointment(1, (String) bundle.get("gid"), (String) bundle.get("name") + " " + 1,  abfahrtt_zeit4, treff_zeit4, treffpunkt, zielort, 1);
        } else {
            id ++;
            Log.i(TAG, "Create Appointment with id " + id);
            gapm1 = new Appointment(id, (String) bundle.get("gid"), (String) bundle.get("name") + " " + id, abfahrtt_zeit4, treff_zeit4, "Uni", "Wolfsburg", 1);
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
                intent2.putExtra("name", (String) bundle.get("name"));
                intent2.putExtra("adminid", (String) bundle.get("adminid"));
                intent2.putExtra("gid", (String) bundle.get("gid"));
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
