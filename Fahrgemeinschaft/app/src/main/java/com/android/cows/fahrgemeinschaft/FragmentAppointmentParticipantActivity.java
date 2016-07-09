package com.android.cows.fahrgemeinschaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.cows.fahrgemeinschaft.adapters.UserAdapterAppointments;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

import de.dataobjects.UserInAppointment;

public class FragmentAppointmentParticipantActivity extends Fragment {

    View contentViewAppointmentDriver;
    private static final String TAG = "TaskOverview";
    private BroadcastReceiver updateparticipantlist;
    private boolean isReceiverRegistered;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentViewAppointmentDriver = inflater.inflate(R.layout.activity_fragment_appointment_driver, null);


        return contentViewAppointmentDriver;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadParticipants();
        createReceiver();
    }

    private void loadParticipants() {
        SharedPreferences prefs = getActivity().getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        Bundle bundle = getActivity().getIntent().getExtras();
        String gid = prefs.getString("currentgid", "");
        int aid = bundle.getInt("aid");

        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        ArrayList<UserInAppointment> userInAppointmentArrayList = sqLiteDBHandler.getParticipantsInAppointment(aid, gid);
        System.out.println(userInAppointmentArrayList.size());
        createUserList(userInAppointmentArrayList);
    }

    public void createUserList(ArrayList<UserInAppointment> userInAppointmentArrayList) {
        UserAdapterAppointments userAdapterAppointments = new UserAdapterAppointments( getActivity() ,R.layout.item_row, userInAppointmentArrayList);

        //View v = inflater.inflate(R.layout.activity_fragment_gruppen_nutzer, container, false );
        listView = (ListView) getActivity().findViewById(R.id.driverListViewTab);
        View header = (View) getActivity().getLayoutInflater().inflate(R.layout.header_row, null);
        // listView.addHeaderView(header);
        listView.setAdapter(userAdapterAppointments);
    }


    public void createReceiver() {
        updateparticipantlist = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadParticipants();
            }
        };
        registerReceiver();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateparticipantlist, new IntentFilter("updateparticipants"));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateparticipantlist);
            isReceiverRegistered = false;
        }
    }

        @Override
        public void onPause() {
            super.onPause();
            unregisterReceiver();
        }

        @Override
        public void onResume() {
            super.onResume();
            registerReceiver();
        }

}


