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
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dataobjects.Groups;

public class CreateGroupActivity extends AppCompatActivity {

    private static final String TAG = "CreateGroupActivity";
    private BroadcastReceiver insertGroupSuccess;
    private BroadcastReceiver errorGroupInsert;
    private ProgressBar mRegistrationProgressBar;
    private boolean isReceiverRegistered;
    private AppCompatButton createGroupButton;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.createGroupProgressBar);
        mRegistrationProgressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        createGroupButton = (AppCompatButton) findViewById(R.id.createGroupButton);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gruppe erstellen");


        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText groupname = (EditText) findViewById(R.id.groupname);
                if (!checkRegEx(groupname.getText().toString())) {
                    Toast.makeText(CreateGroupActivity.this, "Kein gültiger Gruppenname!", Toast.LENGTH_LONG).show();
                } else {

                    setLayoutInvisible();

                    Log.i(TAG, "Create Group");
                    SharedPreferences prefs = getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);

                    Groups newgroup = new Groups(groupname.getText().toString(),
                            1,
                            prefs.getString("userid", ""),
                            prefs.getString("username", ""),
                            groupname.getText().toString() + prefs.getString("userid", ""));

                    MyGcmSend gcmsend = new MyGcmSend();

                    gcmsend.send("group", "insertgroup", newgroup, CreateGroupActivity.this);

                    createReceiver();



                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_appointment_menu, menu);
        return true;
    }

    private boolean checkRegEx(String text) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    @SuppressWarnings("ConstantConditions")
    private void setLayoutInvisible() {
        findViewById(R.id.createGroupProgressBar).setVisibility(ProgressBar.VISIBLE);
        findViewById(R.id.groupname).setVisibility(View.INVISIBLE);
        findViewById(R.id.createGroupButton).setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("ConstantConditions")
    private void setLayoutVisible() {
        findViewById(R.id.createGroupProgressBar).setVisibility(ProgressBar.GONE);
        findViewById(R.id.groupname).setVisibility(View.VISIBLE);
        findViewById(R.id.createGroupButton).setVisibility(View.VISIBLE);
    }

    private void createReceiver() {
        insertGroupSuccess = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver();
                Intent intent2 = new Intent(CreateGroupActivity.this, GeneralTabsActivity.class);
                startActivity(intent2);
                finish();
            }
        };

        errorGroupInsert = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                CharSequence text = "" + bundle.get("error");
                Toast toast = Toast.makeText(CreateGroupActivity.this, text, Toast.LENGTH_LONG);
                toast.show();
                setLayoutVisible();
                unregisterReceiver();
            }
        };
        registerReceiver();
    }


    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(insertGroupSuccess, new IntentFilter("createdgroup"));
            LocalBroadcastManager.getInstance(this).registerReceiver(errorGroupInsert, new IntentFilter("ERRORGroup"));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(insertGroupSuccess);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(errorGroupInsert);
            isReceiverRegistered = false;
        }
        //todo do we need unregistering for receiver?
    }
}
