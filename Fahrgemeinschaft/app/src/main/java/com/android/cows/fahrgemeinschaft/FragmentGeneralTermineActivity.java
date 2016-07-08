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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.adapters.AppointmentAdapter;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;
import java.util.Calendar;

import de.dataobjects.Appointment;

public class FragmentGeneralTermineActivity extends Fragment {

    private int lastid;
    private static final String TAG = "AppointmentOverview";
    View contentViewGeneralTermine;
    ListView listView;

    private BroadcastReceiver updateappointmentlist;
    private boolean isReceiverRegistered;

    private AppointmentAdapter appointmentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentViewGeneralTermine = inflater.inflate(R.layout.activity_fragment_general_termine, null);


        return contentViewGeneralTermine;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);



        /**Appointment gapm1 = new Appointment(1, "Termin1", "testgrp1", "qwertz", "qwertz", "Uni", "Wolfsburg", 1);
        *Appointment gapm2 = new Appointment(2, "Termin2", "testgrp1", "qwertz", "qwertz", "Sportplatz", "Sportplatz", 1);
        *Appointment gapm3 = new Appointment(3, "Termin3", "testgrp1", "qwertz", "qwertz", "Bahnhof", "Hannover", 1);
         ArrayList<Appointment> apmlist = new ArrayList<Appointment>();
        apmlist.add(gapm1);
        apmlist.add(gapm2);
        apmlist.add(gapm3);
        AppointmentAdapter appointmentAdapter = new AppointmentAdapter( getActivity() ,R.layout.item_row, apmlist);
       // createAppointmentOverview(apmlist);
        listView = (ListView) view.findViewById(R.id.apmListView);
        View header = (View) getActivity().getLayoutInflater().inflate(R.layout.header_row, null);
        // listView.addHeaderView(header);
        listView.setAdapter(appointmentAdapter);*/




        //loadAppointmentList();
        //createReceiver();
    }

    public void createReceiver() {
        updateappointmentlist = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("update in appointment fired");
                loadAppointmentList();
            }
        };
        registerReceiver();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateappointmentlist, new IntentFilter("updategroupappointments"));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateappointmentlist);
            isReceiverRegistered = false;
        }
    }


    private void loadAppointmentList() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        SharedPreferences prefs = getActivity().getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String uid = prefs.getString("userid", "");
        ArrayList<Appointment> appointmentlist = sqLiteDBHandler.getAllAppointments();


        if (appointmentlist.size() > 0) {
            createAppointments(appointmentlist);
        } else {
            CharSequence text = "Keine Termine!";
            Toast toast = Toast.makeText(FragmentGeneralTermineActivity.this.getActivity(), text, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void createAppointments(ArrayList<de.dataobjects.Appointment> appointmentArrayList) {
        Log.i(TAG, "createAppointments");
        this.appointmentAdapter = new AppointmentAdapter(getActivity(), R.layout.item_row, appointmentArrayList);
        this.listView = (ListView) getActivity().findViewById(R.id.group_appointment_listview);
        this.listView.setAdapter(appointmentAdapter);
    }
}
