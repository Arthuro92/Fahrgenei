package com.android.cows.fahrgemeinschaft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import de.dataobjects.User;


public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "SettingsActivity";

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    Activity context = this;
    private TextView wievielfreieplätze;
    private SeekBar seekBar;
    private Switch mySwitch;
    private TextView textAnzahlPlaetze;
    private boolean isDriver = false;
    private int freeseats = 0;
    private int progress = 0;

    //private Spinner spinner;
    //private static final String[]paths = {"1", "4", "6", "8"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SeekBar seekBar = (SeekBar) findViewById(R.id.sitzAnzahl_seekBar);
        seekBar.animate().alpha(0.0f);
        seekBar.setVisibility(View.INVISIBLE);
        mySwitch = (Switch) findViewById(R.id.autoSwitch);

        wievielfreieplätze = (TextView) findViewById(R.id.plaetze);
        wievielfreieplätze.animate().alpha(0.0f);
        wievielfreieplätze.setVisibility(View.INVISIBLE);

        textAnzahlPlaetze = (TextView) findViewById(R.id.AnzahlPlaetze);
        textAnzahlPlaetze.setText("Anzahl Sitzplätze: " + seekBar.getProgress() + "/" + seekBar.getMax());
        textAnzahlPlaetze.animate().alpha(0.0f);
        textAnzahlPlaetze.setVisibility(View.INVISIBLE);

        SharedPreferences prefs = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);

        TextView username = (TextView) findViewById(R.id.userNameid);
        TextView email = (TextView) findViewById(R.id.emailid);
        username.setText("Name: " + prefs.getString("username", ""));
        email.setText("Email: " + prefs.getString("useremail", ""));
        initializeVariables();
        onClickSaveButton();
        switchbutton();
        seekBarInit();
        checkIfProfile();

    }

    private void checkIfProfile() {
        SharedPreferences prefs = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        if(prefs.getBoolean("userprofile", false)) {
            SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);
            User user =sqLiteDBHandler.getUser(prefs.getString("userid",""));
            mySwitch.setChecked(user.isDriver());
            seekBar.setProgress(user.getFreeSeats());
        }

    }

    private void seekBarInit() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textAnzahlPlaetze.setText("Anzahl Sitzplätze: " + progress + "/" + seekBar.getMax());
//                Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textAnzahlPlaetze.setText("Anzahl Sitzplätze: " + progress + "/" + seekBar.getMax());
                freeseats = progress;
//                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void switchbutton() {

        //noinspection ConstantConditions
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    isDriver = true;
                    freeseats = progress;
                    seekBar.setVisibility(View.VISIBLE);
                    textAnzahlPlaetze.setVisibility(View.VISIBLE);
                    wievielfreieplätze.setVisibility(View.VISIBLE);
                    seekBar.animate().alpha(1.0f);
                    textAnzahlPlaetze.animate().alpha(1.0f);
                    wievielfreieplätze.animate().alpha(1.0f);
                }else{
                    freeseats = 0;
                    isDriver = false;
                    seekBar.animate().alpha(0.0f);
                    textAnzahlPlaetze.animate().alpha(0.0f);
                    wievielfreieplätze.animate().alpha(0.0f);
                }

            }
        });
    }

    private void onClickSaveButton() {
        Button savebtn = (Button) findViewById(R.id.saveSettings);
        //noinspection ConstantConditions
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo check if user filled the information fields
                SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                MyGcmSend gcmsender = new MyGcmSend();
                String id = prefs.getString("userid", "");
                String name = prefs.getString("username", "");
                String email = prefs.getString("useremail", "");
                String token = prefs.getString("usertoken", "");

                User user = new User(id, token, name, email,mySwitch.isChecked(), freeseats);
                SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(SettingsActivity.this, null);
                sqLiteDBHandler.addUser(user);
                gcmsender.send("user", "registration", user, SettingsActivity.this);
                prefs.edit().putBoolean("userprofile",true).apply();
                Intent intent = new Intent(SettingsActivity.this, GeneralTabsActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initializeVariables() {
            seekBar = (SeekBar) findViewById(R.id.sitzAnzahl_seekBar);
            textAnzahlPlaetze = (TextView) findViewById(R.id.AnzahlPlaetze);
        }


    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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


