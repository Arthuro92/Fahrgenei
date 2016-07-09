package com.android.cows.fahrgemeinschaft;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentAppointmentDetailsActivity extends Fragment {
    private static final String TAG = "FrgAppointmentDetails";

    public TextView terminName;
    public TextView terminTreffZeit;
    public TextView terminTreffOrt;
    public TextView terminAbfahrtZeit;
    public TextView terminZielOrt;
    public Toolbar toolbar;
    private Context context = GlobalAppContext.getAppContext();
    View contentViewAppointmentDetails;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contentViewAppointmentDetails = inflater.inflate(R.layout.activity_fragment_appointment_details, null);


        return contentViewAppointmentDetails;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //TextView terminName = (TextView) findViewById(R.id.terminName);
        TextView terminTreffZeit = (TextView) getActivity().findViewById(R.id.terminTreffZeitZ);
        TextView terminTreffOrt = (TextView) getActivity().findViewById(R.id.terminTreffOrtZ);
        TextView terminAbfahrtZeit = (TextView) getActivity().findViewById(R.id.terminAbfahrtZeitZ);
        TextView terminZielOrt = (TextView) getActivity().findViewById(R.id.terminZielOrtZ);

        Bundle bundle = getActivity().getIntent().getExtras();

        terminAbfahrtZeit.setText(bundle.getString("startingtime"));
        terminTreffOrt.setText(bundle.getString("meetingpoint"));
        terminTreffZeit.setText(bundle.getString("meetingtime"));
        terminZielOrt.setText(bundle.getString("destination"));
//        getActivity().getActionBar().setTitle(bundle.getString("name"));
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_appointment_detail, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}

