package com.android.cows.fahrgemeinschaft;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AppointmentDetailActivity extends AppCompatActivity {

    private static final String TAG = "AppointmentDetailActivity";

    public TextView terminName;
    public TextView terminTreffZeit;
    public TextView terminTreffOrt;
    public TextView terminAbfahrtZeit;
    public TextView terminZielOrt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        TextView terminName = (TextView) findViewById(R.id.terminName);
        TextView terminTreffZeit= (TextView) findViewById(R.id.terminTreffZeit);
        TextView terminTreffOrt = (TextView) findViewById(R.id.terminTreffOrt);
        TextView terminAbfahrtZeit = (TextView) findViewById(R.id.terminAbfahrtZeit);
        TextView terminZielOrt = (TextView) findViewById(R.id.terminZielOrt);

        Bundle bundle = getIntent().getExtras();
        terminName.setText(bundle.getString("test6"));
        terminAbfahrtZeit.setText(bundle.getString("test7"));
        terminTreffOrt.setText(bundle.getString("test8"));
        terminTreffZeit.setText(bundle.getString("test9"));
        terminZielOrt.setText(bundle.getString("test10"));
    }
}
