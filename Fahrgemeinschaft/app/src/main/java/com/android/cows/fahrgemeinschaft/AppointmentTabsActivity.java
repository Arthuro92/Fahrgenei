package com.android.cows.fahrgemeinschaft;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import de.dataobjects.Appointment;
import de.dataobjects.Groups;
import de.dataobjects.UserInAppointment;

public class AppointmentTabsActivity extends AppCompatActivity {
    ViewPager viewPagerAppointment;
    TabLayout tabLayoutAppointment;
    private BroadcastReceiver returntogroupgeneral;
    private BroadcastReceiver returntogrouptabs;
    private boolean isReceiverRegistered;


    Toolbar toolbar;

    private Context context = GlobalAppContext.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_tabs);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        viewPagerAppointment = (ViewPager) findViewById(R.id.viewpagerAppointment);

        final ViewPagerAdapterAppointmentTabs viewPagerAdapter = new ViewPagerAdapterAppointmentTabs(getSupportFragmentManager());
        viewPagerAppointment.setAdapter(viewPagerAdapter);

        Bundle bundle = this.getIntent().getExtras();


        tabLayoutAppointment = (TabLayout) findViewById(R.id.tablayoutAppointment);
        tabLayoutAppointment.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayoutAppointment.setupWithViewPager(viewPagerAppointment);



        tabLayoutAppointment.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerAppointment.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setTitle(bundle.getString("name"));
        createReceiver();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment_detail, menu);

        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String gid = prefs.getString("currentgid", "");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);

        Groups groups = sqLiteDBHandler.getGroup(gid);
        if(!groups.getAdminid().equals(prefs.getString("userid", "")) && groups.getSubstitute() == null ||groups.getSubstitute() != null && !groups.getSubstitute().equals(prefs.getString("userid",""))) {

            MenuItem adduser = menu.findItem(R.id.action_edit_event);
            MenuItem  deletegrp = menu.findItem(R.id.action_delete_event);
            MenuItem addtask = menu.findItem(R.id.action_create_task);

            adduser.setVisible(false);
            deletegrp.setVisible(false);
            addtask.setVisible(false);
        }



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_event) {
            Bundle bundle = getIntent().getExtras();
            Intent intent = new Intent(context, EditAppointmentActivity.class);
            intent.putExtra("aid", bundle.getSerializable("aid") );
            intent.putExtra("name", bundle.getString("name"));
            intent.putExtra("startingtime", bundle.getString("startingtime"));
            intent.putExtra("meetingpoint", bundle.getString("meetingpoint"));
            intent.putExtra("meetingtime", bundle.getString("meetingtime"));
            intent.putExtra("destination", bundle.getString("destination"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }

        if (id == R.id.action_delete_event) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Abfrage");

            alertDialogBuilder.setMessage("Möchten Sie diesen Termin löschen?");
            // set positive button: Yes message
            alertDialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
                    SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                    String gid = prefs.getString("currentgid", "");
                    Bundle bundle = getIntent().getExtras();
                    int aid = (int) bundle.getSerializable("aid");
                    sqLiteDBHandler.deleteAppoinment(aid, gid);

                    Appointment appointment = new Appointment(aid, gid, "0", "0", "0", "0", "0");
                    MyGcmSend myGcmSend = new MyGcmSend();
                    myGcmSend.send("appointment", "deleteappointment", appointment, context);
                    finish();
                }
            });
            // set negative button: No message
            alertDialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show alert
            alertDialog.show();

           // openalert(findViewById(R.id.action_delete_event));
        }

        if ( id ==R.id.action_create_task){
            Intent intent = new Intent(AppointmentTabsActivity.this, CreateTaskActivity.class);
            Bundle bundle = getIntent().getExtras();
            intent.putExtra("aid", bundle.getSerializable("aid") );
            intent.putExtra("name", bundle.getString("name"));
            intent.putExtra("startingtime", bundle.getString("startingtime"));
            intent.putExtra("meetingpoint", bundle.getString("meetingpoint"));
            intent.putExtra("meetingtime", bundle.getString("meetingtime"));
            intent.putExtra("destination", bundle.getString("destination"));
            startActivityForResult(intent, 0);
        }
        if(id == R.id.leave_appointment) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Abfrage");

            alertDialogBuilder.setMessage("Möchten Sie diesen Termin verlassen?");

            alertDialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
            Bundle bundle = getIntent().getExtras();
            SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);

            UserInAppointment userInAppointment = new UserInAppointment(bundle.getInt("aid"), (String) prefs.getString("currentgid",""), prefs.getString("userid", ""), 0 );
            userInAppointment.setIsParticipant(0);
            SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context , null);
            sqLiteDBHandler.addIsInAppointment(userInAppointment);

            MyGcmSend myGcmSend = new MyGcmSend();
            myGcmSend.send("appointment", "participantchange", userInAppointment, context);

            sendLocalUpdateBroadcast();
            finish();}
            });

            alertDialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show alert
            alertDialog.show();



        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Send Local Broadcast to call GUI
     */
    private void sendLocalUpdateBroadcast() {
        Intent intent = new Intent("updategroupappointments");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Create Receivers to enable GUI calls
     */
    public void createReceiver() {
        returntogroupgeneral = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                GroupTabsActivity.pointerforfinish.finish();
                finish();
            }
        };
        returntogrouptabs = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver();
    }

    /**
     * Register Receiver
     */
    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(returntogroupgeneral, new IntentFilter("returntogeneralgroups"));
            LocalBroadcastManager.getInstance(this).registerReceiver(returntogrouptabs, new IntentFilter("returntogrouptabs"));
            isReceiverRegistered = true;
        }
    }

    /**
     * Unregister Receiver
     */
    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(returntogroupgeneral);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(returntogrouptabs);
            isReceiverRegistered = false;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver();
    }

    public void openalert(View v){
        final View addView = getLayoutInflater().inflate(R.layout.activity_appointment_tabs, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Abfrage");
        alertDialogBuilder.setView(addView);

        alertDialogBuilder.setMessage("Möchten Sie dienen Termin löschen?");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
                SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                String gid = prefs.getString("currentgid", "");
                Bundle bundle = getIntent().getExtras();
                int aid = (int) bundle.getSerializable("aid");
                sqLiteDBHandler.deleteAppoinment(aid, gid);

                Appointment appointment = new Appointment(aid, gid, "0", "0", "0", "0", "0");
                MyGcmSend myGcmSend = new MyGcmSend();
                myGcmSend.send("appointment", "deleteappointment", appointment, context);
                finish();
            }
        });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();


    }

}
