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

public class FragmentGeneralTermineActivity extends Fragment {

    private int lastid;
    private static final String TAG = "AppointmentOverview";
    View contentViewGeneralTermine;
    ListView listView4;
    private Context context = GlobalAppContext.getAppContext();
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
        loadAppointmentList();
        createReceiver();
    }

    /**
     * create receiver
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
     * register receiver
     */
    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateappointmentlist, new IntentFilter("updategroupappointments"));
            isReceiverRegistered = true;
        }
    }

    /**
     * unregister receiver
     */
    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateappointmentlist);
            isReceiverRegistered = false;
        }
    }

    /**
     * Load Appointment List from database
     */
    private void loadAppointmentList() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String uid = prefs.getString("userid", "");

        //ArrayList<Appointment> appointmentlist = sqLiteDBHandler.getMyAppointments(uid);
        ArrayList<Appointment> appointmentlist = sqLiteDBHandler.getAllAppointments();
        Log.i("Inhalt ApmListe:", appointmentlist.toString() );
            if(appointmentlist != null) {
                createAppointments(appointmentlist);
            }
    }

    /**
     * Create ListView with appointmentList
     * @param appointmentArrayList AppointmentList
     */
    public void createAppointments(ArrayList<Appointment> appointmentArrayList) {
        Collections.sort(appointmentArrayList);
        Log.i(TAG, "createAppointments");
        this.appointmentAdapter = new AppointmentAdapter(getActivity(), R.layout.item_row_apm, appointmentArrayList);
        Log.i("Inhalt apmArrayList: ", appointmentArrayList.toString() );
        Log.i("Inhalt apmAdapter: ", appointmentAdapter.toString());
        String a = "Ist "+appointmentAdapter.isEmpty() ;
        Log.i("apmAdapter leer? ", a);
        this.listView4 = (ListView) getActivity().findViewById(R.id.apmListView);
        this.listView4.setAdapter(appointmentAdapter);

    }
}
