package com.android.cows.fahrgemeinschaft;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppointmentDetailActivity extends AppCompatActivity {

    private static final String TAG = "AppointmentDetailActivity";

    public TextView terminName;
    public TextView terminTreffZeit;
    public TextView terminTreffOrt;
    public TextView terminAbfahrtZeit;
    public TextView terminZielOrt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        TextView terminName = (TextView) findViewById(R.id.terminName);
        TextView terminTreffZeit = (TextView) findViewById(R.id.terminTreffZeit);
        TextView terminTreffOrt = (TextView) findViewById(R.id.terminTreffOrt);
        TextView terminAbfahrtZeit = (TextView) findViewById(R.id.terminAbfahrtZeit);
        TextView terminZielOrt = (TextView) findViewById(R.id.terminZielOrt);

        Bundle bundle = getIntent().getExtras();
        Calendar startingtimeCal = (Calendar) bundle.get("startingtime");
        Calendar meetingtimeCal = (Calendar) bundle.get("meetingtime");

        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        String startingTimeFormated = format1.format( startingtimeCal.getTime()) +" Uhr";
        String meetingtimeFormated = format1.format( meetingtimeCal.getTime()) +" Uhr";

        terminName.setText(bundle.getString("name"));
        terminAbfahrtZeit.setText(startingTimeFormated);
        terminTreffOrt.setText(bundle.getString("meetingpoint"));
        terminTreffZeit.setText(meetingtimeFormated);
        terminZielOrt.setText(bundle.getString("destination"));
    }

}

