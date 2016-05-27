package com.android.cows.fahrgemeinschaft;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.dataobjects.Appointment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Arthur on 25.05.2016.
 */
public class AppointmentOverview extends AppCompatActivity  {

        private int lastid;
        private static final String TAG = "AppointmentOverview";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_appointment_overview);
            Appointment gapm1 = new Appointment("1", "Termin1", new Date(2016, 10, 10, 10, 00), new Date(2016, 10, 10, 9, 45), "Uni");
            Appointment gapm2 = new Appointment("2", "Termin2", new Date(2016, 9, 9, 9, 00), new Date(2016, 10, 10, 8, 45), "Sportplatz");
            Appointment gapm3 = new Appointment("3", "Termin3", new Date(2016, 8, 8, 8, 00), new Date(2016, 10, 10, 8, 45), "Bahnhof");
            List<Appointment> apmlist = new ArrayList<Appointment>();
            apmlist.add(gapm1);
            apmlist.add(gapm2);
            apmlist.add(gapm3);

            createAppointmentOverview(apmlist);


        }

        /**
         * Creating for each Appointment a linearLayout
         * @param apmlist list of Appointments which should be displayed
         */
        public void createAppointmentOverview(final List<Appointment> apmlist) {
            Log.i(TAG, "createGroup");

            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);

            LinearLayout[] verticalLayoutMain = new LinearLayout[apmlist.size()];
            LinearLayout[] verticalHeadlineLayout = new LinearLayout[apmlist.size()];
            LinearLayout[] verticalContentLayout1 = new LinearLayout[apmlist.size()];
            LinearLayout[] verticalContentLayout2 = new LinearLayout[apmlist.size()];
            RelativeLayout[] relativeLayoutWrapper = new RelativeLayout[apmlist.size()];

            int i = 0;
            while(i < apmlist.size()) {

                relativeLayoutWrapper[i] = new RelativeLayout(this);
                relativeLayoutWrapper[i].setId(apmlist.size()+1+i);
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params2.setMargins(0,0,0,30);
                if(i > 0) {
                    params2.addRule(RelativeLayout.BELOW, apmlist.size()+i);
                    relativeLayoutWrapper[i].setLayoutParams(params2);
                } else {
                    relativeLayoutWrapper[i].setLayoutParams(params2);
                }

                relativeLayoutWrapper[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO Onclick farbe ändern
                        Log.i(TAG, apmlist.get(v.getId()-apmlist.size()-1).getName());
                    }
                });


                verticalLayoutMain[i] = new LinearLayout(this);
                verticalLayoutMain[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,2200);
                verticalLayoutMain[i].setLayoutParams(params);

                verticalLayoutMain[i].setOrientation(LinearLayout.VERTICAL);
                verticalLayoutMain[i].setBackgroundColor(ContextCompat.getColor(this, R.color.darkblueGrey));

                verticalHeadlineLayout[i] = new LinearLayout(this);
                verticalHeadlineLayout[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
                verticalHeadlineLayout[i].setOrientation(LinearLayout.VERTICAL);


                verticalContentLayout1[i] = new LinearLayout(this);
                verticalContentLayout1[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
                verticalContentLayout1[i].setOrientation(LinearLayout.VERTICAL);

                verticalContentLayout2[i] = new LinearLayout(this);
                verticalContentLayout2[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
                verticalContentLayout2[i].setOrientation(LinearLayout.VERTICAL);


                TextView nametxt = new TextView(this);
                TextView membercounttxt = new TextView(this);
                TextView admintxt = new TextView(this);

                nametxt.setTextColor(ContextCompat.getColor(this, R.color.black));
                membercounttxt.setTextColor(ContextCompat.getColor(this, R.color.black));
                admintxt.setTextColor(ContextCompat.getColor(this, R.color.black));

                nametxt.setText(getString(R.string.apm_name, apmlist.get(i).getName()));
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                membercounttxt.setText(getString(R.string.treffpunkt_zeit, df.format(apmlist.get(i).getTreffpunkt_zeit())));
                admintxt.setText(getString(R.string.treffpunkt_ort, apmlist.get(i).getTreffpunkt()));

                nametxt.setTextSize(25);
                membercounttxt.setTextSize(20);
                admintxt.setTextSize(20);

                verticalHeadlineLayout[i].addView(nametxt);
                verticalContentLayout1[i].addView(membercounttxt);
                verticalContentLayout2[i].addView(admintxt);

                verticalLayoutMain[i].addView(verticalHeadlineLayout[i]);
                verticalLayoutMain[i].addView(verticalContentLayout1[i]);
                verticalLayoutMain[i].addView(verticalContentLayout2[i]);
                relativeLayoutWrapper[i].addView(verticalLayoutMain[i]);
                relativeLayout.addView(relativeLayoutWrapper[i]);

                i++;
            }
        }



}
