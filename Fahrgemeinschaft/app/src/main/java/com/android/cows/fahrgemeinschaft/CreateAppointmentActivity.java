package com.android.cows.fahrgemeinschaft;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
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

    static EditText DateTreffpunktzeit;
    static EditText DateAbfahrtzeit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.createAppointmentProgBar);
        mRegistrationProgressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

        DateTreffpunktzeit = (EditText) findViewById(R.id.input_treffpunktZeit);
        DateTreffpunktzeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonTimePickerDialog(v);
                showTruitonDatePickerDialog(v);
            }
        });

        DateAbfahrtzeit = (EditText) findViewById(R.id.input_abfahrtzeit);
        DateAbfahrtzeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonTimePickerDialog(v);
                showTruitonDatePickerDialog(v);
            }
        });

    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            DateTreffpunktzeit.setText(day + "/" + (month + 1) + "/" + year);
            DateAbfahrtzeit.setText(day + "/" + (month + 1) + "/" + year);
        }


    }

    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet (TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            DateTreffpunktzeit.setText(DateTreffpunktzeit.getText() + " -" + hourOfDay + ":" + minute);
            DateAbfahrtzeit.setText(DateAbfahrtzeit.getText() + " -" + hourOfDay + ":"	+ minute);
        }
    }

    public void createAppointment(View view) {

        setLayoutInvisible();

        Log.i(TAG, "Create Appointment");

        MyGcmSend gcmsend = new MyGcmSend();

        Bundle bundle = getIntent().getExtras();

        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);
        int id = sqLiteDBHandler.getNextAppointmentID(bundle.getString("gid"));
        Appointment gapm1;

        // EditText _input_treffpunktZeit = (EditText) findViewById(R.id.input_treffpunktZeit);
        EditText _input_treffpunkt = (EditText) findViewById(R.id.input_treffpunkt);
        //EditText _input_abfahrtzeit = (EditText) findViewById(R.id.input_abfahrtzeit);
        EditText _input_zielort = (EditText) findViewById(R.id.input_zielort);

        //Todo String von Abfahrtzeit & Treffpunktzeit in Calendar ändern oder ähnliches
        String treffpunkt = _input_treffpunkt.getText().toString();
        String zielort = _input_zielort.getText().toString();
        String treffpunktZeit = DateTreffpunktzeit.getText().toString();
        String abfahrtzeit = DateAbfahrtzeit.getText().toString();


        if(id == 0) {
            Log.i(TAG, "no appointments, create appointment with id 1");
            gapm1 = new Appointment(1, (String) bundle.get("gid"), (String) bundle.get("name") + " " + 1,  "10", "10", "10", "10", 1);
        } else {
            id ++;
            Log.i(TAG, "Create Appointment with id " + id);
            gapm1 = new Appointment(id, (String) bundle.get("gid"), (String) bundle.get("name") + " " + id, "10", "10", "10", "10", 1);
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
