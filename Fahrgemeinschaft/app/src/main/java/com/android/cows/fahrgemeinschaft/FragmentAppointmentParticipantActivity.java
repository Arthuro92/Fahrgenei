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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.cows.fahrgemeinschaft.adapters.UserAdapterAppointments;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

import de.dataobjects.Groups;
import de.dataobjects.UserInAppointment;

public class FragmentAppointmentParticipantActivity extends Fragment {

    View contentViewAppointmentDriver;
    private static final String TAG = "TaskOverview";
    private BroadcastReceiver updateparticipantlist;
    private boolean isReceiverRegistered;
    ListView listView;
    private Context context = GlobalAppContext.getAppContext();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentViewAppointmentDriver = inflater.inflate(R.layout.activity_fragment_appointment_driver, null);
        setHasOptionsMenu(true);

        return contentViewAppointmentDriver;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem adduser = menu.findItem(R.id.action_edit_event);
        MenuItem deletegrp = menu.findItem(R.id.action_delete_event);
        MenuItem addtask = menu.findItem(R.id.action_create_task);

        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String gid = prefs.getString("currentgid", "");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);

        Groups groups = sqLiteDBHandler.getGroup(gid);

        if(!groups.getAdminid().equals(prefs.getString("userid", "")) && groups.getSubstitute() == null ||groups.getSubstitute() != null && !groups.getSubstitute().equals(prefs.getString("userid",""))) {
            adduser.setVisible(false);
            deletegrp.setVisible(false);
            addtask.setVisible(false);
        } else {
            adduser.setVisible(false);
            deletegrp.setVisible(false);
            addtask.setVisible(true);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadParticipants();
        createReceiver();
    }

    /**
     * Load UserList who are Participants in Appointment
     */
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

    /**
     * Create ListView with UserList
     * @param userInAppointmentArrayList Users who are Participant in specific Appointment
     */
    public void createUserList(ArrayList<UserInAppointment> userInAppointmentArrayList) {
        UserAdapterAppointments userAdapterAppointments = new UserAdapterAppointments( getActivity() ,R.layout.item_row, userInAppointmentArrayList);

        //View v = inflater.inflate(R.layout.activity_fragment_gruppen_nutzer, container, false );
        listView = (ListView) getActivity().findViewById(R.id.driverListViewTab);
        View header = (View) getActivity().getLayoutInflater().inflate(R.layout.header_row, null);
        // listView.addHeaderView(header);
        listView.setAdapter(userAdapterAppointments);
    }

    /**
     * create receiver
     */
    public void createReceiver() {
        updateparticipantlist = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadParticipants();
            }
        };
        registerReceiver();
    }

    /**
     * register receiver
     */
    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateparticipantlist, new IntentFilter("updateparticipants"));
            isReceiverRegistered = true;
        }
    }

    /**
     * unregister receiver
     */
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


