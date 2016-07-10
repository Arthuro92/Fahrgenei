package com.android.cows.fahrgemeinschaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.cows.fahrgemeinschaft.adapters.GroupAdapter;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

import de.dataobjects.Groups;

public class FragmentGeneralGruppeActivity extends Fragment {
    private static final String TAG = "FGenGroupActivity";
    private BroadcastReceiver updategrplist;
    private boolean isReceiverRegistered;

    View contentViewGeneralGruppen;
    private GroupAdapter groupAdapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contentViewGeneralGruppen = inflater.inflate(R.layout.activity_fragment_general_gruppe, null);


        return contentViewGeneralGruppen;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadGrpList();
        createReceiver();

    }

    /**
     * create receiver
     */
    public void createReceiver() {
        updategrplist = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadGrpList();
            }
        };
        registerReceiver();
    }

    /**
     * registerreceiver
     */
    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updategrplist, new IntentFilter("updategroupgeneral"));
            isReceiverRegistered = true;
        }
    }

    /**
     * unregisterreceiver
     */
    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updategrplist);
            isReceiverRegistered = false;
        }
    }

    /**
     * Load Group List
     */
    public void loadGrpList() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        ArrayList<Groups> grplist = sqLiteDBHandler.getGroups();
        createGroupOverview(grplist);
    }

    /**
     * Creating for each Group a linearLayout
     * @param grplist list of Groups which should be displayed
     */
    public void createGroupOverview(ArrayList<Groups> grplist) {
        Log.i(TAG, "createGroup");
        this.groupAdapter = new GroupAdapter(getActivity(),R.layout.item_row, grplist);
        this.listView = (ListView) getActivity().findViewById(R.id.general_group_listview);
        this.listView.setAdapter(groupAdapter);
    }
}

