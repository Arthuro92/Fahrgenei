package com.android.cows.fahrgemeinschaft;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.cows.fahrgemeinschaft.adapters.UserAdapter;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import com.dataobjects.User;

import java.util.ArrayList;

public class FragmentGruppenNutzerActivity extends Fragment {

    View contentViewGruppenNutzer;
    ListView listView;

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


        User user1 = new User("1","2","Cem","Cem@Homo.de");
        User user2 = new User("2","2","Tim","Tim@Homo.de");
        User user3 = new User("3","2","Blub","blub@Homo.de");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        sqLiteDBHandler.addUser(user1);
        sqLiteDBHandler.addUser(user2);
        sqLiteDBHandler.addUser(user3);
        ArrayList<User> user_list = sqLiteDBHandler.getUserListOfGroup();
     /**   user_list.add(user1);
        user_list.add(user2);
        user_list.add(user3);*/
        UserAdapter userAdapter = new UserAdapter( getActivity() ,R.layout.item_row, user_list);


        //View v = inflater.inflate(R.layout.activity_fragment_gruppen_nutzer, container, false );
        listView = (ListView) view.findViewById(R.id.userListView);
        View header = (View) getActivity().getLayoutInflater().inflate(R.layout.header_row, null);
       // listView.addHeaderView(header);
        listView.setAdapter(userAdapter);


      //  Button btn = (Button) contentViewGruppenNutzer.findViewById(R.id.buttonFragmentGruppenNutzer);
      //  btn.setOnClickListener(new View.OnClickListener() {


       /**     @Override
            public void onClick(View v) {
                //todo this in popup please not as activity

                Intent intent = new Intent(getActivity(), InviteUser.class);
                Bundle bundle = getActivity().getIntent().getExtras();
                intent.putExtra("gid", bundle.getString("gid"));

                startActivity(intent);
            }
        });*/




        super.onViewCreated(view, savedInstanceState);
    }
}
