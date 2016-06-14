package com.android.cows.fahrgemeinschaft;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


        Button btn = (Button) contentViewGruppenTermine.findViewById(R.id.buttonFragmentGruppeTermine);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo this in popup please not as activity

                Log.i(TAG, "switch to createAppointmentActivity");
                Intent intent = new Intent(getActivity(), CreateAppointmentActivity.class);
                startActivity(intent);
                startActivity(intent);
            }
        });
        
        loadAppointmentList();
    }

    private void loadAppointmentList() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        ArrayList<Appointment> appointmentlist = sqLiteDBHandler.getAppointments();
        if (appointmentlist.size() > 0) {
            System.out.println("create APPOINTMENTLIST");
            createAppointments(appointmentlist);
        } else {
            CharSequence text = "Keine Termine!";
            Toast toast = Toast.makeText(FragmentGruppenTermineActivity.this.getActivity(), text, Toast.LENGTH_LONG);
            toast.show();
        }
    }
    /**
     * Creating for each Group a linearLayout
     *
     * @param appointmentArrayList list of Groups which should be displayed
     */
    public void createAppointments(ArrayList<Appointment> appointmentArrayList) {
        Log.i(TAG, "createAppointments");
        this.appointmentAdapter = new AppointmentAdapter(getActivity(), appointmentArrayList);
        this.listView = (ListView) getActivity().findViewById(R.id.group_appointment_listview);
        this.listView.setAdapter(appointmentAdapter);
    }
}
