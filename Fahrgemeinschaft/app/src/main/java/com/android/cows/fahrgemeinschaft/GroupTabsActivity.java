package com.android.cows.fahrgemeinschaft;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

public class GroupTabsActivity extends AppCompatActivity {


    ViewPager viewPagerGroup;
    TabLayout tabLayoutGroup;
    Toolbar toolbar;
    private Context context = GlobalAppContext.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_tabs);

        //NEW
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);


        viewPagerGroup = (ViewPager) findViewById(R.id.viewpagerGroup);

        final ViewPagerAdapterGroupTerminTabs viewPagerAdapter = new ViewPagerAdapterGroupTerminTabs(getSupportFragmentManager());
        viewPagerGroup.setAdapter(viewPagerAdapter);


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

        SharedPreferences prefs = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        setTitle(prefs.getString("currentgroupname", ""));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_detail, menu);
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
        if (id ==R.id.action_delete_group){
            //@TODO Lenni oder David! Bitte vom Server löschen.
            SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
            SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
            String gid = prefs.getString("currentgid", "");
            sqLiteDBHandler.deleteUserInGroup(gid ,prefs.getString("userid", "") );
            Intent intent = new Intent(GroupTabsActivity.this, GeneralTabsActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_add_task) {
            Intent intent = new Intent(GroupTabsActivity.this, CreateTaskActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
