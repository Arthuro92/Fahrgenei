package com.android.cows.fahrgemeinschaft;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class OverviewActivity extends AppCompatActivity {

    private static final String TAG = "OverviewActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        SharedPreferences prefs = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        Log.i(TAG, prefs.getString("username", ""));
        Log.i(TAG, prefs.getString("useremail", ""));
        Log.i(TAG, prefs.getString("userid", ""));

        ImageButton groupButton = (ImageButton) findViewById(R.id.groupButton);
        if (groupButton != null) {
            groupButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent nextToGroup = new Intent(OverviewActivity.this, GroupOverview.class);
                    Log.i(TAG,"OnClickGroup");
                    startActivity(nextToGroup);

                }
            });
        }

        ImageButton calendarButton = (ImageButton) findViewById(R.id.calendarButton);
        if (calendarButton != null) {
            calendarButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent nextToCalendar = new Intent(OverviewActivity.this, AppointmentOverview.class);
                    startActivity(nextToCalendar);

                }
            });
        }

        ImageButton messageButton = (ImageButton) findViewById(R.id.messageButton);
        if (messageButton != null) {
            messageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    Intent tochat = new Intent(OverviewActivity.this, ChatActivity.class);
//                    startActivity(tochat);

                }
            });
        }


        ImageButton notesButton = (ImageButton) findViewById(R.id.notesButton);
        if (notesButton != null) {
            notesButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent nextToNotes = new Intent(OverviewActivity.this, TaskOverview.class);
                    startActivity(nextToNotes);

                }
            });
        }

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        if (settingsButton != null) {
            settingsButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent nextToSettings = new Intent(OverviewActivity.this, SettingsActivity.class);
                    startActivity(nextToSettings);

                }
            });
        }


        ImageButton mailButton = (ImageButton) findViewById(R.id.mailButton);
        if (mailButton != null) {
            mailButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                    Intent nextToCalendar = new Intent(OverviewActivity.this, CalendarActivity.class);
//                    startActivity(nextToCalendar);

                }

                });
        }

    }
}