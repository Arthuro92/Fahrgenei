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

import com.android.cows.fahrgemeinschaft.AppointmentDetailActivity;
import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.R;

import java.util.ArrayList;

import de.dataobjects.Appointment;

/**
 * Created by david on 12.06.2016.
 */
public class AppointmentAdapter extends ArrayAdapter {
    //new new new new
    private Context context = GlobalAppContext.getAppContext();
    private LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    private int layoutResourceId;
    ArrayList<Appointment> data = new ArrayList<Appointment>();

    private String getAppointmentA() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "Blubb");
    }



    private View setAppointmentView(View appointmentView, Appointment appointment) {


        /*TextView textview = (TextView) appointmentView.findViewById(R.id.appointmentTextView1);
        textview.setText(appointment.getName() + " TESTS");
        if(appointment.getIsParticipant() == 0) {
            appointmentView.setBackgroundResource(R.color.red);
        } else {
            appointmentView.setBackgroundResource(R.color.blue_grey_500);
        }*/

        return appointmentView;
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
       /* final Appointment appointment = (Appointment) getItem(position); */


       /* View appointmentView = this.layoutInflater.inflate(R.layout.appointment_layout, parent, false);*/

        View row = convertView;
        AppointmentHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AppointmentHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.inv_status = (TextView)row.findViewById(R.id.inv_status);

            row.setTag(holder);
        }
        else
        {
            holder = (AppointmentHolder) row.getTag();
        }

        final Appointment appointment =  data.get(position);
        holder.txtTitle.setText(appointment.getName());
        Log.d("UserAdapter: ","Holdername als "+appointment.getName()+" gesetzt.");
        holder.imgIcon.setImageResource(R.drawable.football);
        holder.inv_status.setText("Angenommen");



        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, AppointmentDetailActivity.class);
                intent.putExtra("aid", appointment.getAid());
                intent.putExtra("name", appointment.getName());
                intent.putExtra("startingtime", appointment.getAbfahrzeit());
                intent.putExtra("meetingpoint", appointment.getTreffpunkt());
                intent.putExtra("meetingtime", appointment.getTreffpunkt_zeit());
                intent.putExtra("destination", appointment.getZielort());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
//
            }
        });

        return row;
    }

    /**
     * Constructs an Adapter
     *
     * @param context  a Context the Adapter is constructed from
     * @param data an ArrayList to be handled and displayed by the Adapter
     */
    public AppointmentAdapter(Context context, int layoutResourceId,  ArrayList<Appointment> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    static class AppointmentHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView inv_status;
    }
}
