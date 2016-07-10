package com.android.cows.fahrgemeinschaft;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by cemcosgun on 25.06.16.
 */

public class FragmentDatePicker extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {

    public static final int FLAG_START_DATE = 0;
    public static final int FLAG_END_DATE = 1;

    public static int flag = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        // Create a new instance of DatePickerDialog and return it
        return datePickerDialog;
    }

    public void setFlag(int i) {
        flag = i;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
      //  Calendar calendar = Calendar.getInstance();
       // calendar.set(year, month, day);
        if (flag == FLAG_START_DATE) {
            CreateAppointmentActivity.DateTreffpunktzeit.setText(day + "/" + (month + 1) + "/" + year);
        } else if (flag == FLAG_END_DATE) {
            CreateAppointmentActivity.DateAbfahrtzeit.setText(day + "/" + (month + 1) + "/" + year);
        }
    }
}
