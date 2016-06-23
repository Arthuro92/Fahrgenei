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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.adapters.AppointmentAdapter;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import com.dataobjects.Appointment;

import java.util.ArrayList;

public class FragmentGruppenTermineActivity extends Fragment {
    private static final String TAG = "FGenTermineActivity";
    View contentViewGruppenTermine;
    private BroadcastReceiver updategrplist;
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


      /**  Button btn = (Button) contentViewGruppenTermine.findViewById(R.id.buttonFragmentGruppeTermine);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo this in popup please not as activity

                Log.i(TAG, "switch to createAppointmentActivity");
                Intent intent = new Intent(getActivity(), CreateAppointmentActivity.class);
                Bundle bundle = getActivity().getIntent().getExtras();
                intent.putExtra("name", (String) bundle.get("name"));
                intent.putExtra("adminid", (String) bundle.get("adminid"));
                intent.putExtra("gid", (String) bundle.get("gid"));
                startActivity(intent);
            }
        });*/

        hideButtonIfNotAdmin();
        loadAppointmentList();
        createReceiver();

    }


    public void createReceiver() {
        updategrplist = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadAppointmentList();
            }
        };
        registerReceiver();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updategrplist, new IntentFilter("update"));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updategrplist);
            isReceiverRegistered = false;
        }
    }

    private void hideButtonIfNotAdmin() {
        if (!checkAdminStatus()) {
            Log.i(TAG, "No Admin Rights for this Group");
            //Button btn = (Button) contentViewGruppenTermine.findViewById(R.id.buttonFragmentGruppeTermine);
           // btn.setVisibility(View.INVISIBLE);
        }
    }

    private boolean checkAdminStatus() {
        Bundle bundle = getActivity().getIntent().getExtras();
        SharedPreferences prefs = getActivity().getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String id = prefs.getString("userid", "");
        if (id.equals(bundle.get("adminid"))) {
            return true;
        } else {
            return false;
    }
    }

    private void loadAppointmentList() {
        Bundle bundle = getActivity().getIntent().getExtras();
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        ArrayList<Appointment> appointmentlist = sqLiteDBHandler.getAppointments(bundle.getString("gid"));
        if (appointmentlist.size() > 0) {
            System.out.println("create APPOINTMENTLIST");
            createAppointments(appointmentlist);
        } else {
            CharSequence text = "Keine Termine!";
            Toast toast = Toast.makeText(FragmentGruppenTermineActivity.this.getActivity(), text, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    /**
     * Creating for each Group a linearLayout
     *
     * @param appointmentArrayList list of Groups which should be displayed
     */
    public void createAppointments(ArrayList<Appointment> appointmentArrayList) {
        Log.i(TAG, "createAppointments");
        this.appointmentAdapter = new AppointmentAdapter(getActivity(), R.layout.item_row, appointmentArrayList);
        this.listView = (ListView) getActivity().findViewById(R.id.group_appointment_listview);
        this.listView.setAdapter(appointmentAdapter);
    }
}
