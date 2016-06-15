package com.android.cows.fahrgemeinschaft;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "SettingsActivity";

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    Activity context = this;
    private TextView textTxt;

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);

        Log.i(TAG, prefs.getString("username", ""));
        Log.i(TAG, prefs.getString("useremail", ""));
        Log.i(TAG, prefs.getString("userid", ""));

        TextView username = (TextView) findViewById(R.id.userNameinhalt);
        TextView email = (TextView) findViewById(R.id.emailinhalt);
        username.setText(" " + prefs.getString("username", ""));
        email.setText(" " + prefs.getString("useremail", ""));


    }


    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                        //todo abmelden von GCM
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    // [END revokeAccess]
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }

    }
}


