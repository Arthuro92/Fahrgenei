package com.android.cows.fahrgemeinschaft;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by irinahuber on 02.06.16.
 */
public class ViewPagerAdapterGroupTerminTabs extends FragmentPagerAdapter {

    String[] tabtitlearray = {"Nutzer", "Termine", "Chat", "Aufgaben"};

    public ViewPagerAdapterGroupTerminTabs(FragmentManager manager) {

        super(manager);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new FragmentGruppenNutzerActivity();
            case 1:
                return new FragmentGruppenTermineActivity();
            case 2:
                return new FragmentGruppenChatActivity();
            case 3:
                return new FragmentGruppenAufgabenActivity();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabtitlearray[position];
    }
}
