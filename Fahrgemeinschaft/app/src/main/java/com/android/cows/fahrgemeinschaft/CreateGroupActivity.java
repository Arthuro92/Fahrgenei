package com.android.cows.fahrgemeinschaft;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.dataobjects.Group;
import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;

public class CreateGroupActivity extends AppCompatActivity {

    private static final String TAG = "CreateGroupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }


    public void creategroup(View view) {
        EditText groupname = (EditText) findViewById(R.id.groupname);
        if(groupname.getText().toString().trim().length() == 0) {
            Toast.makeText(CreateGroupActivity.this, "Kein gültiger Gruppenname!", Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "Create Group");
            SharedPreferences prefs = getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
            //todo maybe error when no string in sharedpref

            Group newgroup = new Group(groupname.getText().toString(),
                    1,
                    prefs.getString("userid","")
                    , prefs.getString("username","")
                    , groupname.getText().toString()+prefs.getString("userid",""));

            MyGcmSend gcmsend = new MyGcmSend();

            String[] array = new String[1];
            array[0] = newgroup.getGid();

            gcmsend.send("group", "insertgroup", newgroup,this, array);

            Intent intent = new Intent(this, GroupOverview.class);
            startActivity(intent);
        }
    }
}
