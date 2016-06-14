package com.android.cows.fahrgemeinschaft.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.AppointmentDetailActivity;
import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.R;
import com.dataobjects.Appointment;

import java.util.ArrayList;

/**
 * Created by david on 12.06.2016.
 */
public class AppointmentAdapter extends ArrayAdapter {
    //new new new new
    private Context context = GlobalAppContext.getAppContext();
    private LayoutInflater layoutInflater = LayoutInflater.from(getContext());

    private View setAppointmentView(View appointmentView, Appointment appointment) {
        TextView textview = (TextView) appointmentView.findViewById(R.id.appointmentTextView1);
        textview.setText(appointment.getName() + " TESTS");
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
        final Appointment appointment = (Appointment) getItem(position);
        View appointmentView = this.layoutInflater.inflate(R.layout.appointment_layout, parent, false);
        appointmentView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, AppointmentDetailActivity.class);
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

        return setAppointmentView(appointmentView, appointment);
    }

    /**
     * Constructs an Adapter
     *
     * @param context  a Context the Adapter is constructed from
     * @param resource an ArrayList to be handled and displayed by the Adapter
     */
    public AppointmentAdapter(Context context, ArrayList<Appointment> resource) {
        super(context, R.layout.appointment_layout, resource);
    }
}
