package com.android.cows.fahrgemeinschaft;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by irinahuber on 02.06.16.
 */
public class ViewPagerAdapterAppointmentTabs extends FragmentPagerAdapter {

    String[] tabtitlearrayAppointment = {"Termindetails", "Teilnehmer", "Aufgaben"};

    public ViewPagerAdapterAppointmentTabs(FragmentManager manager) {

        super(manager);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new FragmentAppointmentDetailsActivity();
            case 1:
                return new FragmentAppointmentParticipantActivity();
            case 2:
                return new FragmentAppointmentTaskActivity();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabtitlearrayAppointment[position];
    }
}
