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

import com.android.cows.fahrgemeinschaft.adapters.UserAdapter;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

import de.dataobjects.UserInGroup;

public class FragmentGruppenNutzerActivity extends Fragment {

    View contentViewGruppenNutzer;
    ListView listView;
    private BroadcastReceiver updategrplist;
    private boolean isReceiverRegistered;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contentViewGruppenNutzer = inflater.inflate(R.layout.activity_fragment_gruppen_nutzer, null);
        return contentViewGruppenNutzer;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUserList();
        createReceiver();
    }

    /**
     * Load User List from Database
     */
    public void loadUserList() {
        SharedPreferences prefs = getActivity().getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String gid = prefs.getString("currentgid", "");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        ArrayList<UserInGroup> isInGroupList = sqLiteDBHandler.getUserList(gid);
        createUserList(isInGroupList);
    }

    /**
     * Create User List and show it in ListView
     * @param userInGroupList userInGroupList to be shown
     */
    public void createUserList(ArrayList<UserInGroup> userInGroupList) {
        UserAdapter userAdapter = new UserAdapter( getActivity() ,R.layout.item_row, userInGroupList);

        //View v = inflater.inflate(R.layout.activity_fragment_gruppen_nutzer, container, false );
        listView = (ListView) getActivity().findViewById(R.id.userListView);
        View header = (View) getActivity().getLayoutInflater().inflate(R.layout.header_row, null);
        // listView.addHeaderView(header);
        listView.setAdapter(userAdapter);
    }

    /**
     * Create Receiver for calling the GUI later
     */
    public void createReceiver() {
        updategrplist = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadUserList();
            }
        };
        registerReceiver();
    }

    /**
     * Register Receiver
     */
    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updategrplist, new IntentFilter("updategroupuser"));
            isReceiverRegistered = true;
        }
    }

    /**
     * Unregister Receiver
     */
    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updategrplist);
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
