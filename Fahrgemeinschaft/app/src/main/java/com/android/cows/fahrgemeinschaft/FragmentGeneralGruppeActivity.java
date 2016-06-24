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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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


        Button btn = (Button) contentViewGeneralGruppen.findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "switch to createGroupActivity");
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        loadGrpList();
        createReceiver();

    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        unregisterReceiver();
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        unregisterReceiver();
//    }

    public void createReceiver() {
        updategrplist = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadGrpList();
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


    public void loadGrpList() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        ArrayList<Groups> grplist = sqLiteDBHandler.getGroups();
        if (grplist.size() > 0) {
            createGroupOverview(grplist);
        } else {
            CharSequence text = "Du bist in noch keinen Gruppen!";
            Toast toast = Toast.makeText(FragmentGeneralGruppeActivity.this.getActivity(), text, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Creating for each Group a linearLayout
     *
     * @param grplist list of Groups which should be displayed
     */
    public void createGroupOverview(ArrayList<Groups> grplist) {
        Log.i(TAG, "createGroup");
        this.groupAdapter = new GroupAdapter(getActivity(), grplist);
        this.listView = (ListView) getActivity().findViewById(R.id.general_group_listview);
        this.listView.setAdapter(groupAdapter);
    }
}

