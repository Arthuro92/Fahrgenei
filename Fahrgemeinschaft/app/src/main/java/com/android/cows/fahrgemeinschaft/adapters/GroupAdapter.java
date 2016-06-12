package com.android.cows.fahrgemeinschaft.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.GroupTabsActivity;
import com.android.cows.fahrgemeinschaft.R;
import com.android.cows.fahrgemeinschaft.dataobjects.Group;

import java.util.ArrayList;

/**
 * Created by david on 12.06.2016.
 */
public class GroupAdapter extends ArrayAdapter {

    private Context context = GlobalAppContext.getAppContext();
    private LayoutInflater layoutInflater = LayoutInflater.from(getContext());


    private View setGroupView(View groupView, Group group) {
        TextView textview = (TextView) groupView.findViewById(R.id.groupId1);
        textview.setText(group.getName());
        return groupView;
    }


    /**
     * Sets the LayoutInflater to the base View to display the item at position
     * @param position an Integer referencing the position of the current Chat object in the ArrayList
     * @param convertView
     * @param parent
     * @return the fully set displayable view for one list element
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Group group = (Group) getItem(position);
        View groupView = this.layoutInflater.inflate(R.layout.group_layout, parent, false);
        groupView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    Intent intent = new Intent(context, GroupTabsActivity.class);
                    intent.putExtra("name", group.getName());
                    intent.putExtra("adminname", group.getAdminid());
                    intent.putExtra("gid", group.getGid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
//
            }
        });
        return setGroupView(groupView, group);
    }

    /**
     * Constructs an Adapter
     * @param context a Context the Adapter is constructed from
     * @param resource an ArrayList to be handled and displayed by the Adapter
     */
    public GroupAdapter(Context context, ArrayList<Group> resource) {
        super(context, R.layout.group_layout, resource);
    }
}
