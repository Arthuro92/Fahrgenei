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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.dataobjects.Group;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateGroupActivity extends AppCompatActivity {

    private static final String TAG = "CreateGroupActivity";
    private BroadcastReceiver insertGroupSuccess;
    private BroadcastReceiver errorGroupInsert;
    private ProgressBar mRegistrationProgressBar;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.createGroupProgressBar);
        mRegistrationProgressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
    }


    public void creategroup(View view) {
        EditText groupname = (EditText) findViewById(R.id.groupname);
        if (groupname.getText().toString().trim().length() == 0) {
            Toast.makeText(CreateGroupActivity.this, "Kein gültiger Gruppenname!", Toast.LENGTH_LONG).show();
        } else {

            setLayoutInvisible();

            Log.i(TAG, "Create Group");
            SharedPreferences prefs = getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
            //todo maybe error when no string in sharedpref

            Group newgroup = new Group(groupname.getText().toString(),
                    1,
                    prefs.getString("userid", ""),
                    prefs.getString("username", ""),
                    groupname.getText().toString() + prefs.getString("userid", ""),
                    1);

            MyGcmSend gcmsend = new MyGcmSend();

            gcmsend.send("group", "insertgroup", newgroup, this);

            createReceiver();

        }
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
        findViewById(R.id.button).setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("ConstantConditions")
    private void setLayoutVisible() {
        findViewById(R.id.createGroupProgressBar).setVisibility(ProgressBar.GONE);
        findViewById(R.id.groupname).setVisibility(View.VISIBLE);
        findViewById(R.id.button).setVisibility(View.VISIBLE);
    }

    private void createReceiver() {
        insertGroupSuccess = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver();
                Intent intent2 = new Intent(CreateGroupActivity.this, GeneralTabsActivity.class);
                startActivity(intent2);
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
