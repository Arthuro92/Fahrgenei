package com.android.cows.fahrgemeinschaft.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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

    /**
     * Gets the User by accessing the shared preferences
     * @return user String
     */
    private String getAppointmentUser() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "Blubb");
    }

    private View setAppointmentView(View appointmentView, Appointment appointment) {
        return appointmentView;
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
        Appointment appointment = (Appointment) getItem(position);
        View appointmentView = this.layoutInflater.inflate(R.layout.appointment_layout, parent, false);
        return setAppointmentView(appointmentView, appointment);
    }

    /**
     * Constructs an Adapter
     * @param context a Context the Adapter is constructed from
     * @param resource an ArrayList to be handled and displayed by the Adapter
     */
    public AppointmentAdapter(Context context, ArrayList<Appointment> resource) {
        super(context, R.layout.appointment_layout, resource);
    }
}
