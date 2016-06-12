package com.android.cows.fahrgemeinschaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.dataobjects.Appointment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur on 25.05.2016.
 */
public class SingleAppointmentOverviewActivity extends AppCompatActivity {

    private static final String TAG = "AppointmentOverview";
    private BroadcastReceiver receiveappointment;
    private BroadcastReceiver errorreceivingappointment;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_overview);
        requestAppointment();

        createReceiver();

    }

    public void createReceiver() {
        receiveappointment = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences prefs = getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                //todo maybe error when no string in sharedpref
                String grpliststring = prefs.getString("applist", "");
                Gson gson = new Gson();

                ArrayList<Appointment> applist = gson.fromJson(grpliststring, new TypeToken<List<Appointment>>(){}.getType());
                createAppointmentOverview(applist);
                unregisterReceiver();

            }
        };

        errorreceivingappointment = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                CharSequence text = "" + bundle.get("error");
                Toast toast = Toast.makeText(SingleAppointmentOverviewActivity.this, text , Toast.LENGTH_LONG);
                toast.show();
                unregisterReceiver();
            }
        };
        registerReceiver();
    }

    public void requestAppointment() {
        //for appointments
        SharedPreferences prefs = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        MyGcmSend gcmsend = new MyGcmSend();
        Bundle bundle = getIntent().getExtras();
        String[] extras = new String[2];
        extras[0] = bundle.getString("gid");
        extras[1] = prefs.getString("userid", "");
        gcmsend.send("appointment", "getSingleAppointment", this, extras);
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(receiveappointment, new IntentFilter("singleAppointment"));
            LocalBroadcastManager.getInstance(this).registerReceiver(errorreceivingappointment, new IntentFilter("ERRORAppointment"));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if(isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiveappointment);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(errorreceivingappointment);
            isReceiverRegistered = false;
        }
    }

//todo we really want this? research what happends when receiver does not get unregistered
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiveappointment);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(errorreceivingappointment);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    /**
     * Creating for each Appointment a linearLayout
     *
     * @param apmlist list of Appointments which should be displayed
     */
    public void createAppointmentOverview(final List<Appointment> apmlist) {
        Log.i(TAG, "createGroup");

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);

        LinearLayout[] verticalLayoutMain = new LinearLayout[apmlist.size()];
        LinearLayout[] verticalHeadlineLayout = new LinearLayout[apmlist.size()];
        LinearLayout[] verticalContentLayout1 = new LinearLayout[apmlist.size()];
        LinearLayout[] verticalContentLayout2 = new LinearLayout[apmlist.size()];
        RelativeLayout[] relativeLayoutWrapper = new RelativeLayout[apmlist.size()];

        int i = 0;
        while (i < apmlist.size()) {

            relativeLayoutWrapper[i] = new RelativeLayout(this);
            relativeLayoutWrapper[i].setId(apmlist.size() + 1 + i);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0, 0, 0, 30);
            if (i > 0) {
                params2.addRule(RelativeLayout.BELOW, apmlist.size() + i);
                relativeLayoutWrapper[i].setLayoutParams(params2);
            } else {
                relativeLayoutWrapper[i].setLayoutParams(params2);
            }

            relativeLayoutWrapper[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO Onclick farbe ändern
                    Log.i(TAG, apmlist.get(v.getId() - apmlist.size() - 1).getName());
                    DateFormat dfm = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    Intent intent = new Intent(SingleAppointmentOverviewActivity.this, AppointmentDetailActivity.class);
                    intent.putExtra("test6", apmlist.get(v.getId() - apmlist.size() - 1).getName());
                    intent.putExtra("test7", dfm.format(apmlist.get(v.getId() - apmlist.size() - 1).getAbfahrzeit()));
                    intent.putExtra("test8", apmlist.get(v.getId() - apmlist.size() - 1).getTreffpunkt());
                    intent.putExtra("test9", dfm.format(apmlist.get(v.getId() - apmlist.size() - 1).getTreffpunkt_zeit()));
                    intent.putExtra("test10", apmlist.get(v.getId() - apmlist.size() - 1).getZielort());
                    startActivity(intent);
                }
            });


            verticalLayoutMain[i] = new LinearLayout(this);
            verticalLayoutMain[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 2200);
            verticalLayoutMain[i].setLayoutParams(params);

            verticalLayoutMain[i].setOrientation(LinearLayout.VERTICAL);
            verticalLayoutMain[i].setBackgroundColor(ContextCompat.getColor(this, R.color.darkblueGrey));
            verticalLayoutMain[i].setBackgroundResource(R.drawable.boxes_background);

            verticalHeadlineLayout[i] = new LinearLayout(this);
            verticalHeadlineLayout[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            verticalHeadlineLayout[i].setOrientation(LinearLayout.VERTICAL);


            verticalContentLayout1[i] = new LinearLayout(this);
            verticalContentLayout1[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            verticalContentLayout1[i].setOrientation(LinearLayout.VERTICAL);

            verticalContentLayout2[i] = new LinearLayout(this);
            verticalContentLayout2[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            verticalContentLayout2[i].setOrientation(LinearLayout.VERTICAL);


            TextView nametxt = new TextView(this);
            TextView membercounttxt = new TextView(this);
            TextView admintxt = new TextView(this);

            verticalHeadlineLayout[i].setPadding(50, 10, 0, 0);
            verticalContentLayout1[i].setPadding(50, 0, 0, 0);
            verticalContentLayout2[i].setPadding(50, 0, 0, 25);

            nametxt.setTextColor(ContextCompat.getColor(this, R.color.black));
            membercounttxt.setTextColor(ContextCompat.getColor(this, R.color.black));
            admintxt.setTextColor(ContextCompat.getColor(this, R.color.black));

            nametxt.setText(getString(R.string.apm_name, apmlist.get(i).getName()));
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            membercounttxt.setText(getString(R.string.treffpunkt_zeit, df.format(apmlist.get(i).getTreffpunkt_zeit())));
            admintxt.setText(getString(R.string.treffpunkt_ort, apmlist.get(i).getTreffpunkt()));

            nametxt.setTextSize(25);
            membercounttxt.setTextSize(20);
            admintxt.setTextSize(20);

            verticalHeadlineLayout[i].addView(nametxt);
            verticalContentLayout1[i].addView(membercounttxt);
            verticalContentLayout2[i].addView(admintxt);

            verticalLayoutMain[i].addView(verticalHeadlineLayout[i]);
            verticalLayoutMain[i].addView(verticalContentLayout1[i]);
            verticalLayoutMain[i].addView(verticalContentLayout2[i]);
            relativeLayoutWrapper[i].addView(verticalLayoutMain[i]);
            relativeLayout.addView(relativeLayoutWrapper[i]);

            i++;
        }
    }


}
