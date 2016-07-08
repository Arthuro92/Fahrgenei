package com.android.cows.fahrgemeinschaft;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import de.dataobjects.Appointment;

public class EditAppointmentActivity extends AppCompatActivity {
    private static final String TAG = "EditAppointmentActivity";

    public  TextView terminName;
    public TextView terminTreffZeit;
    public TextView terminTreffOrt;
    public TextView terminAbfahrtZeit;
    public TextView terminZielOrt;
    public Button saveData;
    private Context context = GlobalAppContext.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);

        final TextView terminName = (TextView) findViewById(R.id.input_name2);
        final TextView terminTreffZeit = (TextView) findViewById(R.id.input_treffpunktZeit2);
        final TextView terminTreffOrt = (TextView) findViewById(R.id.input_treffpunkt2);
        final TextView terminAbfahrtZeit = (TextView) findViewById(R.id.input_abfahrtzeit2);
        final TextView terminZielOrt = (TextView) findViewById(R.id.input_zielort2);
        final Button savaData = (Button) findViewById(R.id.saveAppointmentButton);



        Bundle bundle = getIntent().getExtras();

        terminName.setText(bundle.getString("name"));
        terminAbfahrtZeit.setText(bundle.getString("startingtime"));
        terminTreffOrt.setText(bundle.getString("meetingpoint"));
        terminTreffZeit.setText(bundle.getString("meetingtime"));
        terminZielOrt.setText(bundle.getString("destination"));
        final int aid = (int) bundle.getSerializable("aid");

        savaData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //@TODO SQLITE DB und SERVER DB MUESSEN DEN EINTRAG FÜR DEN TERMIN ÄNDERN

                MyGcmSend gcmsend = new MyGcmSend();
                SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                Appointment gapm1 = new Appointment(aid, prefs.getString("currentgid",""), prefs.getString("currentgroupname", "") + " " + aid, terminAbfahrtZeit.getText().toString(), terminTreffZeit.getText().toString(), terminZielOrt.getText().toString(), terminTreffOrt.getText().toString());



                Intent intent = new Intent(context, AppointmentDetailActivity.class);
                Log.i("Taskdaten",aid +" "+terminName.getText()+" "+terminAbfahrtZeit.getText()+" "+terminTreffOrt.getText());
                intent.putExtra("aid", aid);
                intent.putExtra("name", terminName.getText().toString()  );
                intent.putExtra("startingtime", terminAbfahrtZeit.getText().toString());
                intent.putExtra("meetingpoint", terminTreffOrt.getText().toString());
                intent.putExtra("meetingtime", terminTreffZeit.getText().toString());
                intent.putExtra("destination", terminZielOrt.getText().toString());
                gcmsend.send("appointment", "insertappointment", gapm1, context);


                startActivity(intent);
                finish();
            }
        });
    }
}