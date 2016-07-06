package com.android.cows.fahrgemeinschaft;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppointmentDetailActivity extends AppCompatActivity {

    private static final String TAG = "AppointmentDetailActivity";

    public TextView terminName;
    public TextView terminTreffZeit;
    public TextView terminTreffOrt;
    public TextView terminAbfahrtZeit;
    public TextView terminZielOrt;
    public Toolbar toolbar;
    private Context context = GlobalAppContext.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);


        //TextView terminName = (TextView) findViewById(R.id.terminName);
        TextView terminTreffZeit = (TextView) findViewById(R.id.terminTreffZeit);
        TextView terminTreffOrt = (TextView) findViewById(R.id.terminTreffOrt);
        TextView terminAbfahrtZeit = (TextView) findViewById(R.id.terminAbfahrtZeit);
        TextView terminZielOrt = (TextView) findViewById(R.id.terminZielOrt);

        Bundle bundle = getIntent().getExtras();
      //  Calendar startingtimeCal = (Calendar) bundle.get("startingtime");
      //  Calendar meetingtimeCal = (Calendar) bundle.get("meetingtime");

       // SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy hh:mm")
        //String startingTimeFormated = format1.format( startingtimeCal.getTime()) +" Uhr";
        //String meetingtimeFormated = format1.format( meetingtimeCal.getTime()) +" Uhr";

//        terminName.setText(bundle.getString("name"));
        terminAbfahrtZeit.setText(bundle.getString("startingtime"));
        terminTreffOrt.setText(bundle.getString("meetingpoint"));
        terminTreffZeit.setText(bundle.getString("meetingtime"));
        terminZielOrt.setText(bundle.getString("destination"));
        setTitle(bundle.getString("name"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_event) {
            Bundle bundle = getIntent().getExtras();
            Intent intent = new Intent(context, EditAppointmentActivity.class);
            intent.putExtra("aid", bundle.getSerializable("aid") );
            intent.putExtra("name", bundle.getString("name"));
            intent.putExtra("startingtime", bundle.getString("startingtime"));
            intent.putExtra("meetingpoint", bundle.getString("meetingpoint"));
            intent.putExtra("meetingtime", bundle.getString("meetingtime"));
            intent.putExtra("destination", bundle.getString("destination"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            finish();
            return true;
        }


        if (id == R.id.action_delete_event) {
            //@TODO Lenni oder David! Bitte vom Server löschen.
            SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
            SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
            //String gid = prefs.getString("currentgid", "");
            Bundle bundle = getIntent().getExtras();
            int a = (int) bundle.getSerializable("aid");
            sqLiteDBHandler.deleteAppoinment(a);
            Intent intent = new Intent(AppointmentDetailActivity.this, GroupTabsActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

}

