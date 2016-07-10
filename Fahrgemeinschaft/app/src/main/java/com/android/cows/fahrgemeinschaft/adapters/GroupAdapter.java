package com.android.cows.fahrgemeinschaft.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.GroupTabsActivity;
import com.android.cows.fahrgemeinschaft.R;
import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.gcm.TopicSubscriber;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

import de.dataobjects.Groups;

/**
 * Created by david on 12.06.2016.
 */
public class GroupAdapter extends ArrayAdapter {
    private static final String TAG = "GroupAdapter";
    private Context context = GlobalAppContext.getAppContext();
    private LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    private int layoutResourceId;
    ArrayList<Groups> data = new ArrayList<Groups>();

    /**
     *
     * @param groupView view
     * @param group group to be shown
     * @return
     */
    private View setGroupView(View groupView, final de.dataobjects.Groups group) {
        TextView textview = (TextView) groupView.findViewById(R.id.groupTextView1);
        textview.setText(group.getName());
        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        final int isJoined = sqLiteDBHandler.getIsJoint(group.getGid(),prefs.getString("userid", ""));


        return groupView;
    }

    /**
     * Method to send when User accepted Invitation
     * @param group group he accepted
     */
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

    /**
     * Send Broad to GUI
     */
    private void sendLocalUpdateBroadcast() {
        Intent intent = new Intent("updategroupgeneral");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * Create Dialog for asking if user wants to accept inivitation
     * @param view view
     * @param group group dialog is created for
     */
    private void openAlert(View view, final Groups group) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Abfrage");

        alertDialogBuilder.setMessage("Möchten Sie dieser Gruppe beitreten?");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
                SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                sqLiteDBHandler.setIsJoint( group.getGid(), prefs.getString("userid", "") , 1);
                // go to a new activity of the app
                dialog.cancel();
               /* Toast.makeText(getContext(), "You chose a positiv answer",
                        Toast.LENGTH_LONG).show(); */

                Intent intent = new Intent(context, GroupTabsActivity.class);
                // SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                sendInvitationAccept(group);
                prefs.edit().putString("currentgroupname", group.getName()).apply();
                prefs.edit().putString("currentgroupadminid", group.getAdminid()).apply();
                prefs.edit().putString("currentgid", group.getGid()).apply();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
                SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                sqLiteDBHandler.deleteGroup(group.getGid());
                sendLocalUpdateBroadcast();
                // cancel the alert box and put a Toast to the user
                dialog.cancel();
                Toast.makeText(getContext(), "Sie haben die Einladung nicht angenommen.",
                        Toast.LENGTH_LONG).show();
            }
        });
        // set neutral button: Exit the app message


        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
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

        GroupHolder holder = null;
        View row = convertView;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GroupHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.inv_status = (TextView)row.findViewById(R.id.inv_status);

            row.setTag(holder);
        }
        else
        {
            holder = (GroupHolder)row.getTag();
        }

        final Groups group =  data.get(position);
        holder.txtTitle.setText(group.getName());
        Log.d("UserAdapter: ","Holdername als "+group.getName()+" gesetzt.");
        holder.imgIcon.setImageResource(R.drawable.group);
        holder.inv_status.setText("");

        //return row;


       /* final Group group = (Group) getItem(position);
        View groupView = this.layoutInflater.inflate(R.layout.group_layout, parent, false);*/

        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
                final int isJoined = sqLiteDBHandler.getIsJoint(group.getGid(), prefs.getString("userid", ""));


                if(isJoined == 0) {
                    openAlert(v, group);
                   // sqLiteDBHandler.setIsJoint( group.getGid(), prefs.getString("userid", "") , 1);


                } else if ( isJoined == -1){
                    Log.i(TAG, "Error in getting IsJoined Value in");
                } else{

                    Intent intent = new Intent(context, GroupTabsActivity.class);
                   // SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                    prefs.edit().putString("currentgroupname", group.getName()).apply();
                    prefs.edit().putString("currentgroupadminid", group.getAdminid()).apply();
                    prefs.edit().putString("currentgid", group.getGid()).apply();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }

                /*Intent intent = new Intent(context, GroupTabsActivity.class);

                SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                prefs.edit().putString("currentgroupname", group.getName()).apply();
                prefs.edit().putString("currentgroupadminid", group.getAdminid()).apply();
                prefs.edit().putString("currentgid", group.getGid()).apply();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
                //openAlert(v);
//
            }
        });

       /* return setGroupView(row, group);*/
        View groupView = this.layoutInflater.inflate(R.layout.group_layout, parent, false);
        setGroupView(groupView, group);
        return row;
    }

    /**
     * Constructs an Adapter
     *
     * @param context  a Context the Adapter is constructed from
     * @param data an ArrayList to be handled and displayed by the Adapter
     */
    public GroupAdapter(Context context, int layoutResourceId, ArrayList<Groups> data) {
      //  super(context, R.layout.group_layout, resource);

        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    /**
     * Holder class
     */
    static class GroupHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView inv_status;
    }
}
