package com.android.cows.fahrgemeinschaft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

public class OverviewActivity extends AppCompatActivity {
    private static final String TAG = "OverviewActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);


        ImageButton groupButton = (ImageButton) findViewById(R.id.groupButton);
        groupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent nextToGroup = new Intent(OverviewActivity.this, GroupActivity.class);
                Log.i(TAG,"OnClickGroup");
                startActivity(nextToGroup);

            }
        });

        ImageButton calendarButton = (ImageButton) findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent nextToCalendar = new Intent(OverviewActivity.this, CalendarActivity.class);
                startActivity(nextToCalendar);

            }
        });

        ImageButton messageButton = (ImageButton) findViewById(R.id.messageButton);
        messageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent nextToMessage = new Intent(OverviewActivity.this, MessageActivity.class);
                startActivity(nextToMessage);

            }
        });


        ImageButton notesButton = (ImageButton) findViewById(R.id.notesButton);
        notesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent nextToNotes = new Intent(OverviewActivity.this, NotesActivity.class);
                startActivity(nextToNotes);

            }
        });

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent nextToSettings = new Intent(OverviewActivity.this, SettingsActivity.class);
                startActivity(nextToSettings);

            }
        });


        ImageButton mailButton = (ImageButton) findViewById(R.id.mailButton);
        mailButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent nextToCalendar = new Intent(OverviewActivity.this, CalendarActivity.class);
                startActivity(nextToCalendar);

            }

            });

    }
}