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
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.R;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

import de.dataobjects.User;
import de.dataobjects.UserInGroup;

/**
 * Created by david on 12.06.2016.
 */
public class UserAdapter extends ArrayAdapter {
    //new new new new
    private Context context = GlobalAppContext.getAppContext();
    private LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    private int layoutResourceId;
    ArrayList<UserInGroup> data = new ArrayList<UserInGroup>();




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

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new UserHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.inv_status = (TextView)row.findViewById(R.id.inv_status);

            row.setTag(holder);
        }
        else
        {
            holder = (UserHolder)row.getTag();
        }

        UserInGroup userInGroup =  data.get(position);

        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        System.out.println("UID " + userInGroup.getUid());
        User user = sqLiteDBHandler.getUser(userInGroup.getUid());

        if(userInGroup.getIsJoined() == 0) {
            // @TODO  Das Umschalten des Status klappt auch noch nich. Muss eventuell in der Server DB noch was geändert werden.
            //row.setBackgroundResource(R.color.red);
            holder.inv_status.setText("Einladung versandt");
        } else if(userInGroup.getIsJoined() == -1) {
            Toast.makeText(getContext(), "Fehler. IsJoinend-Value ist falsch gesetzt.",
                    Toast.LENGTH_LONG).show();
        } else {
            holder.inv_status.setText("Angenommen");
        }


        holder.txtTitle.setText(user.getName());
        Log.d("UserAdapter: ","Holdername als "+user.getName()+" gesetzt.");
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
    public UserAdapter(Context context, int layoutResourceId, ArrayList<UserInGroup> data) {
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
