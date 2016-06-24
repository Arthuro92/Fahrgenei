package com.android.cows.fahrgemeinschaft.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.GroupTabsActivity;
import com.android.cows.fahrgemeinschaft.R;
import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.gcm.TopicSubscriber;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

/**
 * Created by david on 12.06.2016.
 */
public class GroupAdapter extends ArrayAdapter {
    private static final String TAG = "GroupAdapter";
    private Context context = GlobalAppContext.getAppContext();
    private LayoutInflater layoutInflater = LayoutInflater.from(getContext());


    private View setGroupView(View groupView, final de.dataobjects.Groups group) {
        TextView textview = (TextView) groupView.findViewById(R.id.groupTextView1);
        textview.setText(group.getName());
        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        final int isJoined = sqLiteDBHandler.getIsJoint(group.getGid(),prefs.getString("userid", ""));

        if(isJoined == 0) {
            groupView.setBackgroundResource(R.color.red);
            groupView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    sendInvitationAccept(group);
                }
            });

        } else if(isJoined == -1) {
            Log.i(TAG, "Error in getting IsJoined Value in");
        } else {
            groupView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                    prefs.edit().putString("currentgroupname", group.getName()).apply();
                    prefs.edit().putString("currentgroupadminid", group.getAdminid()).apply();
                    prefs.edit().putString("currentgid", group.getGid()).apply();

                    Intent intent = new Intent(context, GroupTabsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            groupView.setBackgroundResource(R.color.blue_grey_500);
        }

        return groupView;
    }

    private void sendInvitationAccept(de.dataobjects.Groups group) {
        Log.i(TAG, "Send Invitation Accept");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        MyGcmSend myGcmSend = new MyGcmSend();
        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String[] string = new String[2];
        string[0] = group.getGid();
        string[1] = prefs.getString("userid", "");
        sqLiteDBHandler.joinGroup(group.getGid(), string[1],1);
        TopicSubscriber.subscribeToTopic(group.getGid());
        myGcmSend.send("group", "invitationaccept", context, string);
        sendLocalUpdateBroadcast();
    }
    private void sendLocalUpdateBroadcast() {
        Intent intent = new Intent("update");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    /**
     * Sets the LayoutInflater to the base View to display the item at position
     *
     * @param position    an Integer referencing the position of the current Chat object in the ArrayList
     * @param convertView
     * @param parent
     * @return the fully set displayable view for one list element
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final de.dataobjects.Groups group = (de.dataobjects.Groups) getItem(position);
        View groupView = this.layoutInflater.inflate(R.layout.group_layout, parent, false);
        return setGroupView(groupView, group);
    }

    /**
     * Constructs an Adapter
     *
     * @param context  a Context the Adapter is constructed from
     * @param resource an ArrayList to be handled and displayed by the Adapter
     */
    public GroupAdapter(Context context, ArrayList<de.dataobjects.Groups> resource) {
        super(context, R.layout.group_layout, resource);
    }
}
