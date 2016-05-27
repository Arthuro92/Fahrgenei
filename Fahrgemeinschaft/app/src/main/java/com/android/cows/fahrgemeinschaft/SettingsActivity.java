package com.android.cows.fahrgemeinschaft;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity {

     public static final  String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences prefs = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        Log.i(TAG, prefs.getString("username", ""));
        Log.i(TAG, prefs.getString("useremail", ""));
        Log.i(TAG, prefs.getString("userid", ""));
    }
}
