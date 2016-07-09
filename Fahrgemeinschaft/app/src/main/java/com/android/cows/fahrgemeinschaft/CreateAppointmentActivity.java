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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import de.dataobjects.Appointment;
import de.dataobjects.Groups;

public class CreateAppointmentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CreateGroupActivity";
    private BroadcastReceiver insertAppointmentsuccess;
    private BroadcastReceiver errorReceivingAppointment;
    private ProgressBar mRegistrationProgressBar;
    private boolean isReceiverRegistered;

    static EditText DateTreffpunktzeit;
    static EditText DateAbfahrtzeit;
    //static EditText Terminname;

    private FragmentDatePicker mFragmentDatePicker;
    private FragmentTimePicker mFragmentTimePicker;

    static Button createAppointmentButton;

    static EditText _input_treffpunktZeit;
    static EditText _input_treffpunkt;
    static EditText _input_abfahrtzeit;
    static EditText _input_zielort;
    static EditText _input_terminname;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.createAppointmentProgBar);
        mRegistrationProgressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

       // createDateOnClickListener();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Termin erstellen");

        DateTreffpunktzeit = (EditText) findViewById(R.id.input_treffpunktZeit);
        DateAbfahrtzeit = (EditText) findViewById(R.id.input_abfahrtzeit);
        mFragmentDatePicker = new FragmentDatePicker();
        mFragmentTimePicker = new FragmentTimePicker();
        DateTreffpunktzeit.setOnClickListener(this);
        DateAbfahrtzeit.setOnClickListener(this);


        _input_treffpunktZeit = (EditText) findViewById(R.id.input_treffpunktZeit);
        _input_treffpunkt = (EditText) findViewById(R.id.input_treffpunkt);
        _input_abfahrtzeit = (EditText) findViewById(R.id.input_abfahrtzeit);
        _input_zielort = (EditText) findViewById(R.id.input_zielort);
        _input_terminname = (EditText) findViewById(R.id.input_terminname);

        createAppointmentButton = (Button) findViewById(R.id.createappointbutton);
        createAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String terminnae = _input_terminname.getText().toString();
                String treffpunktZeit = _input_treffpunktZeit.getText().toString();
                String treffpunkt = _input_treffpunkt.getText().toString();
                String abfahrtzeit = _input_abfahrtzeit.getText().toString();
                String zielort = _input_zielort.getText().toString();
                if (terminnae.equalsIgnoreCase(""))
                {
                    _input_terminname.setError("Pflichtfeld! Bitte geben Sie einen Namen für diesen Termin an.");
                } else if (treffpunktZeit.equalsIgnoreCase(""))
                {
                    _input_treffpunktZeit.setError("Pflichtfeld! Bitte terminieren Sie den Treffpunkt.");
                } else if (treffpunkt.equalsIgnoreCase("")) {
                    _input_treffpunkt.setError("Pflichtfeld! Bitte wählen Sie einen Ort des Treffpunktes aus.");
                } else if (abfahrtzeit.equalsIgnoreCase("")) {
                    _input_abfahrtzeit.setError("Pflichtfeld! Bitte terminieren Sie die Abfahrt.");
                } else if (zielort.equalsIgnoreCase("")) {
                _input_zielort.setError("Pflichtfeld! Bitte geben Sie den Zielort an.");
                } else {
                createAppointment(v);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.input_treffpunktZeit) {
            mFragmentDatePicker.setFlag(FragmentDatePicker.FLAG_START_DATE);
            mFragmentTimePicker.show(getSupportFragmentManager(), "timePicker");
            mFragmentDatePicker.show(getSupportFragmentManager(), "datePicker");
        } else if (id == R.id.input_abfahrtzeit) {
            mFragmentDatePicker.setFlag(FragmentDatePicker.FLAG_END_DATE);
            mFragmentTimePicker.show(getSupportFragmentManager(), "timePicker");
            mFragmentDatePicker.show(getSupportFragmentManager(), "datePicker");
        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_appointment_menu, menu);
        return true;
    }

    public void createAppointment(View view) {
        SharedPreferences prefs = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String gid = prefs.getString("currentgid", "");
        String userid = prefs.getString("userid","");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this,null);
        Groups groups = sqLiteDBHandler.getGroup(gid);

        setLayoutInvisible();

        Log.i(TAG, "Create Appointment");

        MyGcmSend gcmsend = new MyGcmSend();


        String gname = prefs.getString("currentgroupname", "");

        int id = sqLiteDBHandler.getNextAppointmentID(gid);
        de.dataobjects.Appointment gapm1;


        //Todo String von Abfahrtzeit & Treffpunktzeit in Calendar ändern oder ähnliches
        String treffpunkt = _input_treffpunkt.getText().toString();
        String zielort = _input_zielort.getText().toString();
        String treffpunktZeit = _input_treffpunktZeit.getText().toString();
        String abfahrtzeit = _input_abfahrtzeit.getText().toString();
        String terminname = _input_terminname.getText().toString();


        if(id == 0) {
            Log.i(TAG, "no appointments, create appointment with id 1");
            gapm1 = new Appointment(1, prefs.getString("currentgid",""), terminname, treffpunkt, zielort, treffpunktZeit, abfahrtzeit);
        } else {
            id ++;
            Log.i(TAG, "Create Appointment with id " + id);
            gapm1 = new Appointment(id, prefs.getString("currentgid",""), terminname, treffpunkt, zielort, treffpunktZeit, abfahrtzeit);
        }
        gcmsend.send("appointment", "insertappointment", gapm1, this);

        createReceiver();

    }

    @SuppressWarnings("ConstantConditions")
    private void setLayoutInvisible() {
        findViewById(R.id.createAppointmentProgBar).setVisibility(ProgressBar.VISIBLE);
        findViewById(R.id.createappointbutton).setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("ConstantConditions")
    private void setLayoutVisible() {
        findViewById(R.id.createAppointmentProgBar).setVisibility(ProgressBar.GONE);
        findViewById(R.id.createappointbutton).setVisibility(View.VISIBLE);
    }


    private void createReceiver() {
        insertAppointmentsuccess = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver();
                Intent intent2 = new Intent(CreateAppointmentActivity.this, GroupTabsActivity.class);
                Bundle bundle = getIntent().getExtras();
//                startActivity(intent2);
                finish();
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
