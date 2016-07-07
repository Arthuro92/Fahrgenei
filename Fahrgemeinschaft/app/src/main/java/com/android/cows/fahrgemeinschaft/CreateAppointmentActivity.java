package com.android.cows.fahrgemeinschaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import de.dataobjects.Appointment;
import de.dataobjects.Groups;

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
        createDateOnClickListener();

        Button createAppointmentButton = (Button) findViewById(R.id.createappointbutton);
        createAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAppointment(v);
            }
        });

    }

    private void createDateOnClickListener() {
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
        DialogFragment newFragment = new FragmentDatePicker();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


//    public static class FragmentDatePicker extends DialogFragment implements
//            DatePickerDialog.OnDateSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            // Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            // Do something with the date chosen by the user
//            DateTreffpunktzeit.setText(day + "/" + (month + 1) + "/" + year);
//            DateAbfahrtzeit.setText(day + "/" + (month + 1) + "/" + year);
//        }
//
//
//    }

    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new FragmentTimePicker();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


//    public static class FragmentTimePicker extends DialogFragment implements
//            TimePickerDialog.OnTimeSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current time as the default values for the picker
//            final Calendar c = Calendar.getInstance();
//            int hour = c.get(Calendar.HOUR_OF_DAY);
//            int minute = c.get(Calendar.MINUTE);
//
//            // Create a new instance of TimePickerDialog and return it
//            return new TimePickerDialog(getActivity(), this, hour, minute,
//                    DateFormat.is24HourFormat(getActivity()));
//        }
//
//        public void onTimeSet (TimePicker view, int hourOfDay, int minute) {
//            // Do something with the time chosen by the user
//            DateTreffpunktzeit.setText(DateTreffpunktzeit.getText() + " -" + hourOfDay + ":" + minute);
//            DateAbfahrtzeit.setText(DateAbfahrtzeit.getText() + " -" + hourOfDay + ":"	+ minute);
//        }
//    }

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
            gapm1 = new Appointment(1, prefs.getString("currentgid",""), prefs.getString("currentgroupname", "") + " " + 1,  "10", "10", "10", "10");
        } else {
            id ++;
            Log.i(TAG, "Create Appointment with id " + id);
            gapm1 = new Appointment(id, prefs.getString("currentgid",""), prefs.getString("currentgroupname", "") + " " + id, "10", "10", "10", "10");
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
