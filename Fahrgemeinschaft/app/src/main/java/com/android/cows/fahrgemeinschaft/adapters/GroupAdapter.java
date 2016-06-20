package com.android.cows.fahrgemeinschaft.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.GroupTabsActivity;
import com.android.cows.fahrgemeinschaft.R;
import com.dataobjects.Group;
import com.dataobjects.User;

import java.util.ArrayList;

/**
 * Created by david on 12.06.2016.
 */
public class GroupAdapter extends ArrayAdapter {

    private Context context = GlobalAppContext.getAppContext();
    private LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    private int layoutResourceId;
    ArrayList<Group> data = new ArrayList<Group>();

    private View setGroupView(View groupView, Group group) {


        TextView textview = (TextView) groupView.findViewById(R.id.groupTextView1);
        textview.setText(group.getName());

        if(group.getisJoined() == 0) {
            groupView.setBackgroundResource(R.color.red);
        } else {
            groupView.setBackgroundResource(R.color.blue_grey_500);
        }
        return groupView;
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

        final Group group =  data.get(position);
        holder.txtTitle.setText(group.getName());
        Log.d("UserAdapter: ","Holdername als "+group.getName()+" gesetzt.");
        holder.imgIcon.setImageResource(R.drawable.group);
        holder.inv_status.setText("");

        //return row;


       /* final Group group = (Group) getItem(position);
        View groupView = this.layoutInflater.inflate(R.layout.group_layout, parent, false);*/

        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupTabsActivity.class);
                intent.putExtra("name", group.getName());
                intent.putExtra("adminid", group.getAdminid());
                intent.putExtra("gid", group.getGid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
//
            }
        });

        return row;
       /* return setGroupView(row, group);*/
    }

    /**
     * Constructs an Adapter
     *
     * @param context  a Context the Adapter is constructed from
     * @param data an ArrayList to be handled and displayed by the Adapter
     */
    public GroupAdapter(Context context, int layoutResourceId, ArrayList<Group> data) {
      //  super(context, R.layout.group_layout, resource);

        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    static class GroupHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView inv_status;
    }
}
