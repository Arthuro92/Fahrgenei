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
import android.widget.Button;
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

/**
        User user1 = new User("1","2","Cem","Cem@Homo.de");
        User user2 = new User("1","2","Tim","Tim@Homo.de");
        User user3 = new User("1","2","Blub","blub@Homo.de");
        ArrayList<User> user_list = new ArrayList<User>();
        user_list.add(user1);
        user_list.add(user2);
        user_list.add(user3);
        UserAdapter userAdapter = new UserAdapter( getActivity() ,R.layout.item_row, user_list);


        View v = inflater.inflate(R.layout.activity_fragment_gruppen_nutzer, container, false );
        listView = (ListView) v.findViewById(R.id.userListView);
        View header = (View) getActivity().getLayoutInflater().inflate(R.layout.header_row, null);
        listView.addHeaderView(header);
        listView.setAdapter(userAdapter);
 */



        return contentViewGruppenNutzer;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Button btn = (Button) contentViewGruppenNutzer.findViewById(R.id.buttonFragmentGruppenNutzer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo this in popup please not as activity

                Intent intent = new Intent(getActivity(), InviteUser.class);
                startActivity(intent);
            }
        });

        loadUserList();
        createReceiver();
    }

    public void loadUserList() {
        SharedPreferences prefs = getActivity().getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String gid = prefs.getString("currentgid", "");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        ArrayList<UserInGroup> isInGroupList = sqLiteDBHandler.getIsInGroupWithGroupId(gid);
        createUserList(isInGroupList);
    }

    public void createUserList(ArrayList<UserInGroup> userInGroupList) {
        UserAdapter userAdapter = new UserAdapter( getActivity() ,R.layout.item_row, userInGroupList);

        //View v = inflater.inflate(R.layout.activity_fragment_gruppen_nutzer, container, false );
        listView = (ListView) getActivity().findViewById(R.id.userListView);
        View header = (View) getActivity().getLayoutInflater().inflate(R.layout.header_row, null);
        // listView.addHeaderView(header);
        listView.setAdapter(userAdapter);
    }

    public void createReceiver() {
        updategrplist = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadUserList();
            }
        };
        registerReceiver();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updategrplist, new IntentFilter("updategroupuser"));
            isReceiverRegistered = true;
        }
    }

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
