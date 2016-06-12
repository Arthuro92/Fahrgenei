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
import com.android.cows.fahrgemeinschaft.dataobjects.Group;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

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

        loadgrplist();
        createReceiver();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    public void createReceiver() {
        updategrplist = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadgrplist();
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


    public void loadgrplist() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        ArrayList<Group> grplist = sqLiteDBHandler.getGroups();
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
    public void createGroupOverview(ArrayList<Group> grplist) {
        Log.i(TAG, "createGroup");
        this.groupAdapter = new GroupAdapter(getActivity(), grplist);
        this.listView = (ListView) getActivity().findViewById(R.id.general_group_listview);
        this.listView.setAdapter(groupAdapter);


//        //TODO maybe better solution for it?
//
//        RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.relativelayout);
//
//        LinearLayout[] verticalLayoutMain = new LinearLayout[grplist.size()];
//        LinearLayout[] verticalHeadlineLayout = new LinearLayout[grplist.size()];
//        LinearLayout[] verticalContentLayout1 = new LinearLayout[grplist.size()];
//        LinearLayout[] verticalContentLayout2 = new LinearLayout[grplist.size()];
//        RelativeLayout[] relativeLayoutWrapper = new RelativeLayout[grplist.size()];
//
//        int i = 0;
//        while (i < grplist.size()) {
//
//            relativeLayoutWrapper[i] = new RelativeLayout(getActivity());
//            relativeLayoutWrapper[i].setId(grplist.size() + 1 + i);
//            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params2.setMargins(0, 0, 0, 30);
//            if (i > 0) {
//                params2.addRule(RelativeLayout.BELOW, grplist.size() + i);
//                relativeLayoutWrapper[i].setLayoutParams(params2);
//            } else {
//                relativeLayoutWrapper[i].setLayoutParams(params2);
//            }
//            relativeLayoutWrapper[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //TODO Onclick farbe ändern
//                    Log.i(TAG, grplist.get(v.getId() - grplist.size() - 1).getName());
//                    Intent intent = new Intent(getActivity(), GroupTabsActivity.class);
//                    intent.putExtra("name", grplist.get(v.getId() - grplist.size() - 1).getName());
//                    intent.putExtra("adminname", grplist.get(v.getId() - grplist.size() - 1).getAdminname());
//                    intent.putExtra("gid", grplist.get(v.getId() - grplist.size() - 1).getGid());
//                    startActivity(intent);
//
//                }
//            });
//
//
//            verticalLayoutMain[i] = new LinearLayout(getActivity());
//            verticalLayoutMain[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, 0, 0, 2200);
//            verticalLayoutMain[i].setLayoutParams(params);
//
//            verticalLayoutMain[i].setOrientation(LinearLayout.VERTICAL);
//            if (grplist.get(i).getisJoined() == 1) {
//                verticalLayoutMain[i].setBackgroundResource(R.drawable.boxes_background);
//            } else {
//                verticalLayoutMain[i].setBackgroundResource(R.drawable.boxes_background_notjoined);
//            }
//
//            verticalHeadlineLayout[i] = new LinearLayout(getActivity());
//            verticalHeadlineLayout[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
//            verticalHeadlineLayout[i].setOrientation(LinearLayout.VERTICAL);
//
//
//            verticalContentLayout1[i] = new LinearLayout(getActivity());
//            verticalContentLayout1[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
//            verticalContentLayout1[i].setOrientation(LinearLayout.VERTICAL);
//
//            verticalContentLayout2[i] = new LinearLayout(getActivity());
//            verticalContentLayout2[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
//            verticalContentLayout2[i].setOrientation(LinearLayout.VERTICAL);
//
//
//            TextView nametxt = new TextView(getActivity());
//            TextView membercounttxt = new TextView(getActivity());
//            TextView admintxt = new TextView(getActivity());
//
//            nametxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
//            membercounttxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
//            admintxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
//
//            nametxt.setText(getString(R.string.grpname, grplist.get(i).getName()));
//            membercounttxt.setText(getString(R.string.membercount, Integer.toString(grplist.get(i).getMembercount())));
//            admintxt.setText(getString(R.string.grpadmin, grplist.get(i).getAdminname()));
//
//            nametxt.setTextSize(20);
//            membercounttxt.setTextSize(15);
//            admintxt.setTextSize(15);
//
//            verticalHeadlineLayout[i].addView(nametxt);
//            verticalContentLayout1[i].addView(membercounttxt);
//            verticalContentLayout2[i].addView(admintxt);
//
//            verticalHeadlineLayout[i].setPadding(50, 10, 0, 0);
//            verticalContentLayout1[i].setPadding(50, 0, 0, 0);
//            verticalContentLayout2[i].setPadding(50, 0, 0, 25);
//
//            verticalLayoutMain[i].addView(verticalHeadlineLayout[i]);
//            verticalLayoutMain[i].addView(verticalContentLayout1[i]);
//            verticalLayoutMain[i].addView(verticalContentLayout2[i]);
//            relativeLayoutWrapper[i].addView(verticalLayoutMain[i]);
//            relativeLayout.addView(relativeLayoutWrapper[i]);
//
//            i++;
//        }
    }
}

