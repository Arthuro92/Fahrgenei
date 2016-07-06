package com.android.cows.fahrgemeinschaft;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

public class AppointmentTabsActivity extends AppCompatActivity {


    ViewPager viewPagerAppointment;
    TabLayout tabLayoutAppointment;

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

        System.out.println("Audgabe" + bundle.getString("startingtime"));
        System.out.println("Audgabe" + bundle.getString("meetingpoint"));
        System.out.println("Audgabe" + bundle.getString("meetingtime"));
        System.out.println("Audgabe" + bundle.getString("destination"));

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment_detail, menu);
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
            //@TODO Lenni oder David! Bitte vom Server löschen.
            SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
            SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
            //String gid = prefs.getString("currentgid", "");
            Bundle bundle = getIntent().getExtras();
            int a = (int) bundle.getSerializable("aid");
            sqLiteDBHandler.deleteAppoinment(a);
            Intent intent = new Intent(AppointmentTabsActivity.this, GroupTabsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
