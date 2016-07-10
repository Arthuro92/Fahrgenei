package com.android.cows.fahrgemeinschaft;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class GroupDetailOverview extends AppCompatActivity {

    private static final String TAG = "GroupDetailOverview";

    public TextView grpName;
    public TextView adminName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail_overview);

        TextView grpName = (TextView) findViewById(R.id.grpName);
        TextView adminName = (TextView) findViewById(R.id.adminName);

        Bundle bundle = getIntent().getExtras();
        grpName.setText(bundle.getString("name"));
        adminName.setText(bundle.getString("adminname"));
    }

}
