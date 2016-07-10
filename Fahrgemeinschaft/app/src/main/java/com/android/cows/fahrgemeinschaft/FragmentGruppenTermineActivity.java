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

import com.android.cows.fahrgemeinschaft.adapters.AppointmentAdapter;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;
import java.util.Collections;

import de.dataobjects.Appointment;

public class FragmentGruppenTermineActivity extends Fragment {
    private static final String TAG = "FGenTermineActivity";
    View contentViewGruppenTermine;
    private BroadcastReceiver updateappointmentlist;
    private boolean isReceiverRegistered;

    private AppointmentAdapter appointmentAdapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentViewGruppenTermine = inflater.inflate(R.layout.activity_fragment_gruppen_termine, null);


        return contentViewGruppenTermine;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideButtonIfNotAdmin();
        loadAppointmentList();
        createReceiver();
    }

    /**
     * create Receiver
     */
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

    /**
     * Register Receiver
     */
    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateappointmentlist, new IntentFilter("updategroupappointments"));
            isReceiverRegistered = true;
        }
    }

    /**
     * unregister Receiver
     */
    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateappointmentlist);
            isReceiverRegistered = false;
        }
    }

    /**
     * Method for hiding Buttons
     */
    private void hideButtonIfNotAdmin() {
        if (!checkAdminStatus()) {
            Log.i(TAG, "No Admin Rights for this Group");
          //  Button btn = (Button) contentViewGruppenTermine.findViewById(R.id.buttonFragmentGruppeTermine);
          //  btn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Method checks if current user is admin of current group
     */
    private boolean checkAdminStatus() {
        SharedPreferences prefs = getActivity().getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String id = prefs.getString("userid", "");
        String adminid = prefs.getString("currentgroupadminid", "");
        if (id.equals(adminid)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Load Appointmentlist
     */
    private void loadAppointmentList() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        SharedPreferences prefs = getActivity().getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String gid = prefs.getString("currentgid", "");
        ArrayList<Appointment> appointmentlist = sqLiteDBHandler.getAppointments(gid);
         createAppointments(appointmentlist);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    /**
     * Creating for each Group a linearLayout
     * @param appointmentArrayList list of Groups which should be displayed
     */
    public void createAppointments(ArrayList<de.dataobjects.Appointment> appointmentArrayList) {
        Log.i(TAG, "createAppointments");
        Collections.sort(appointmentArrayList);
        this.appointmentAdapter = new AppointmentAdapter(getActivity(), R.layout.item_row_apm, appointmentArrayList);
        this.listView = (ListView) getActivity().findViewById(R.id.group_appointment_listview);
        this.listView.setAdapter(appointmentAdapter);
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
