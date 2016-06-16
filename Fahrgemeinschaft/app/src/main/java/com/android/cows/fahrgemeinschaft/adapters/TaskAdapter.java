package com.android.cows.fahrgemeinschaft.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.android.cows.fahrgemeinschaft.TaskDetailActivity;
import com.dataobjects.Task;
import com.dataobjects.User;

import java.util.ArrayList;

/**
 * Created by Arthur on 16.06.2016.
 */
public class TaskAdapter extends ArrayAdapter {
    private Context context = GlobalAppContext.getAppContext();
    private LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    private int layoutResourceId;
    ArrayList<Task> data = new ArrayList<Task>();
    private static final String TAG = "TaskAdapter";




    /**
     * Gets the User by accessing the shared preferences
     * @return user String
     */
    private String getTaskTask() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "Blubb");
    }

    private View setTaskView(View taskView, User task) {
        return taskView;
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


        View row = convertView;
        TaskHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TaskHolder();
          //  holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.nameFeld = (TextView)row.findViewById(R.id.taskName2);
          //  holder.inv_status = (TextView)row.findViewById(R.id.inv_status);
            holder.beschreibungsFeld = (TextView)row.findViewById(R.id.taskDescr2);
            holder.bearbeiterFeld = (TextView) row.findViewById(R.id.taskAssignee2);

            row.setTag(holder);
        }
        else
        {
            holder = (TaskHolder)row.getTag();
        }

        final Task task =  data.get(position);
        holder.nameFeld.setText(task.getAufgabenName());
        Log.d("UserAdapter: ","Holdername als "+task.getAufgabenName()+" gesetzt.");
       // holder.imgIcon.setImageResource(R.drawable.user128);
       // holder.inv_status.setText("Angenommen");
        holder.beschreibungsFeld.setText(task.getAufgabenBeschreibung());
        holder.bearbeiterFeld.setText("Bearbeiter: " + task.getBearbeiter());

        row.setOnClickListener(new View.OnClickListener() {
                                   public void onClick(View v) {
                                       Log.i(TAG, task.getAufgabenName() );
                                       Intent intent = new Intent(context, TaskDetailActivity.class);
                                       intent.putExtra("taskname", task.getAufgabenName());
                                       intent.putExtra("taskdescription", task.getAufgabenBeschreibung());
                                       intent.putExtra("taskincharge", task.getBearbeiter());
                                       //based on item add info to intent
                                       context.startActivity(intent);
                                   };
        });




        return row;
    }

    /**
     * Constructs an Adapter
     * @param context a Context the Adapter is constructed from
     * @param data an ArrayList to be handled and displayed by the Adapter
     */
    public TaskAdapter(Context context, int layoutResourceId, ArrayList<Task> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    static class TaskHolder
    {
       // ImageView imgIcon;
        TextView nameFeld;
        //TextView inv_status;
        TextView beschreibungsFeld;
        TextView bearbeiterFeld;
    }


}
