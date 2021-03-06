package com.android.cows.fahrgemeinschaft;

import android.app.Activity;
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

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.gcm.TopicSubscriber;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import de.dataobjects.Groups;
import de.dataobjects.UserInGroup;

public class GroupTabsActivity extends AppCompatActivity {


    ViewPager viewPagerGroup;
    TabLayout tabLayoutGroup;
    Toolbar toolbar;
    public static Activity pointerforfinish;
    private BroadcastReceiver returntogroupgeneral;
    private boolean isReceiverRegistered;
    private Context context = GlobalAppContext.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pointerforfinish = this;
        setContentView(R.layout.activity_group_tabs);

        //NEW
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);


        viewPagerGroup = (ViewPager) findViewById(R.id.viewpagerGroup);

        final ViewPagerAdapterGroupTerminTabs viewPagerAdapter = new ViewPagerAdapterGroupTerminTabs(getSupportFragmentManager());
        viewPagerGroup.setAdapter(viewPagerAdapter);


        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            String intentaction = bundle.getString("calledfromchatobserver", "normalcall");
            if (intentaction.equals("calledfromchatobserver")) {
                viewPagerGroup.setCurrentItem(3);
            }
        }

        tabLayoutGroup = (TabLayout) findViewById(R.id.tablayoutGroup);
        tabLayoutGroup.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayoutGroup.setupWithViewPager(viewPagerGroup);
        tabLayoutGroup.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPagerGroup.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });
        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        setTitle(prefs.getString("currentgroupname", ""));
        createReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_detail, menu);
        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String gid = prefs.getString("currentgid", "");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);

        Groups groups = sqLiteDBHandler.getGroup(gid);
        if(!groups.getAdminid().equals(prefs.getString("userid", "")) && groups.getSubstitute() == null ||groups.getSubstitute() != null && !groups.getSubstitute().equals(prefs.getString("userid",""))) {
            MenuItem adduser = menu.findItem(R.id.action_add_person);
            MenuItem deletegrp = menu.findItem(R.id.action_delete_group);
            MenuItem leavegrp = menu.findItem(R.id.leave_group);
            MenuItem addtask = menu.findItem(R.id.action_add_task);
            MenuItem addevent = menu.findItem(R.id.action_add_event);

            adduser.setVisible(false);
            deletegrp.setVisible(false);
            leavegrp.setVisible(true);
            addtask.setVisible(false);
            addevent.setVisible(false);
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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add_person){
            Intent intent = new Intent(GroupTabsActivity.this, InviteUser.class);
            startActivity(intent);
        }
        if (id == R.id.action_add_event){
            Intent intent = new Intent(GroupTabsActivity.this, CreateAppointmentActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_delete_group ){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Abfrage");

            alertDialogBuilder.setMessage("Möchten Sie diese Gruppe löschen?");
            // set positive button: Yes message
            alertDialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
                    SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                    String gid = prefs.getString("currentgid", "");
                    Groups groups = sqLiteDBHandler.getGroup(gid);
                    sqLiteDBHandler.deleteGroup(gid);
                    Intent intent = new Intent(GroupTabsActivity.this, GeneralTabsActivity.class);
                    startActivity(intent);
                    TopicSubscriber.unsubscribeFromTopic(gid);
                    MyGcmSend myGcmSend = new MyGcmSend();
                    myGcmSend.send("group", "deletegroup", groups, context);
                }
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
        if(id == R.id.action_add_task) {
            Intent intent = new Intent(GroupTabsActivity.this, CreateTaskActivity.class);
            startActivity(intent);
        }

        if(id == R.id.leave_group) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Abfrage");

            alertDialogBuilder.setMessage("Möchten Sie diese Gruppe verlassen?");
            // set positive button: Yes message
            alertDialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    System.out.println("LEAVING GROUP");
                    SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
                    SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                    String gid = prefs.getString("currentgid", "");
                    sqLiteDBHandler.deleteGroup(gid);

                    TopicSubscriber.unsubscribeFromTopic(gid);
                    UserInGroup userInGroup = new UserInGroup(prefs.getString("userid", ""), gid, 0);
                    MyGcmSend myGcmSend = new MyGcmSend();
                    myGcmSend.send("group", "deleteuseringroup", userInGroup, context);
                    Intent intent = new Intent(GroupTabsActivity.this, GeneralTabsActivity.class);
                    startActivity(intent);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * create receiver for calls to gui
     */
    public void createReceiver() {
        returntogroupgeneral = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver();
    }

    /**
     * register reciever
     */
    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(context).registerReceiver(returntogroupgeneral, new IntentFilter("returntogeneralgroups"));
            isReceiverRegistered = true;
        }
    }

    /**
     * unregister receiver
     */
    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(returntogroupgeneral);
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

}
