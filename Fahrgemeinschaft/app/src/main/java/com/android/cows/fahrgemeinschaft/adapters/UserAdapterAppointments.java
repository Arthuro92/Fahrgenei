package com.android.cows.fahrgemeinschaft.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.R;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

import de.dataobjects.User;
import de.dataobjects.UserInAppointment;

/**
 * Created by david on 12.06.2016.
 */
public class UserAdapterAppointments extends ArrayAdapter {
    //new new new new
    private static final String TAG = "UserAdapterGroups";
    private Context context = GlobalAppContext.getAppContext();
    private LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    private int layoutResourceId;
    ArrayList<UserInAppointment> data = new ArrayList<UserInAppointment>();




    /**
     * Gets the User by accessing the shared preferences
     *
     * @return user String
     */
    private String getUserUser() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "Blubb");
    }

    private View setUserView(View userView, User user) {
        return userView;
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
        View row = convertView;
        UserHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new UserHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.inv_status = (TextView)row.findViewById(R.id.inv_status);

            row.setTag(holder);
        } else {
            holder = (UserHolder)row.getTag();
        }

        final UserInAppointment userInAppointment =  data.get(position);

        final SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);

        User user = sqLiteDBHandler.getUser(userInAppointment.getUid());

        holder.inv_status.setText("Mitfahrer");
        if(userInAppointment.isDriver()) {
            holder.inv_status.setText("Fahrer mit " + user.getFreeSeats() + " Plätzen");
        }

//        row.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
//                Groups group = sqLiteDBHandler.getGroup(prefs.getString("currentgid",""));
//                if(userInAppointment.getUid() != group.getAdminid()) {
//                    group.setSubstitute(userInAppointment.getUid());
//
//                    Log.i(TAG, group.getName());
//                    Log.i(TAG, userInAppointment.getUid());
//                    MyGcmSend myGcmSend = new MyGcmSend();
//                    myGcmSend.send("group", "newsubstitute", group, context);
//                }
//            }
//        });

        holder.txtTitle.setText(user.getName());
        Log.d("UserAdapterGroups: ","Holdername als "+user.getName()+" gesetzt.");
        holder.imgIcon.setImageResource(R.drawable.user128);

        return row;
    }

    /**
     * Constructs an Adapter
     *
     * @param context  a Context the Adapter is constructed from
     * @param context a Context the Adapter is constructed from
     * @param data an ArrayList to be handled and displayed by the Adapter
     */
    public UserAdapterAppointments(Context context, int layoutResourceId, ArrayList<UserInAppointment> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    static class UserHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView inv_status;
    }


}
