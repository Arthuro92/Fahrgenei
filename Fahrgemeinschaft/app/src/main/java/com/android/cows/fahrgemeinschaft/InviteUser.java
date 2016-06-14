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
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import com.dataobjects.Group;
import com.google.gson.Gson;

public class InviteUser extends AppCompatActivity {
    private static final String TAG = "InviteUser";
    private BroadcastReceiver inviteSuccess;
    private BroadcastReceiver errorInviting;
    private ProgressBar mRegistrationProgressBar;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.inviteUserProgressBar);
        mRegistrationProgressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
    }

    public void invitefriend(View view) {
        EditText editText = (EditText) findViewById(R.id.inviteEmail);
        if (editText.getText().toString().trim().length() != 0) {
            SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);

            setLayoutInvisible();
            MyGcmSend gcmSend = new MyGcmSend();

            String[] string = new String[3];
            string[0] = editText.getText().toString();
            Bundle bundle = getIntent().getExtras();
            string[1] = bundle.getString("gid");

            Group group = sqLiteDBHandler.getGroup(string[1]);
            if (group != null) {
                //todo handle invites with own email or email already invited
                Gson gson = new Gson();
                string[2] = gson.toJson(group);
                gcmSend.send("group", "inviteuser", this, string);
                createReceiver();
            } else {
                CharSequence text = "Fehler Gruppe in die Eingeladen werden soll nicht gefunden in SQLLight Db";
                Toast toast = Toast.makeText(InviteUser.this, text, Toast.LENGTH_LONG);
                toast.show();

            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setLayoutInvisible() {
        findViewById(R.id.inviteUserProgressBar).setVisibility(ProgressBar.VISIBLE);
        findViewById(R.id.inviteEmail).setVisibility(View.INVISIBLE);
        findViewById(R.id.invitefriendbtn).setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("ConstantConditions")
    private void setLayoutVisible() {
        findViewById(R.id.inviteUserProgressBar).setVisibility(ProgressBar.GONE);
        findViewById(R.id.inviteEmail).setVisibility(View.VISIBLE);
        findViewById(R.id.invitefriendbtn).setVisibility(View.VISIBLE);
    }


    private void createReceiver() {
        inviteSuccess = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver();
                Intent intent2 = new Intent(InviteUser.this, GroupTabsActivity.class);
                Bundle bundle = getIntent().getExtras();
                intent2.putExtra("name", (String) bundle.get("name"));
                intent2.putExtra("adminid", (String) bundle.get("adminid"));
                intent2.putExtra("gid", (String) bundle.get("gid"));
                startActivity(intent2);
            }
        };

        errorInviting = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                CharSequence text = "" + bundle.get("error");
                Toast toast = Toast.makeText(InviteUser.this, text, Toast.LENGTH_LONG);
                toast.show();
                setLayoutVisible();
                unregisterReceiver();
            }
        };
        registerReceiver();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(inviteSuccess, new IntentFilter("invitesuccess"));
            LocalBroadcastManager.getInstance(this).registerReceiver(errorInviting, new IntentFilter("ERRORGroup"));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(inviteSuccess);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(errorInviting);
            isReceiverRegistered = false;
        }
    }
}
