package com.android.cows.fahrgemeinschaft;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.cows.fahrgemeinschaft.cryptography.AsymmetricEncryptionClient;

public class GeneralTabsActivity extends AppCompatActivity {


    ViewPager viewPagerGeneral;
    TabLayout tabLayoutGeneral;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_group_tabs);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        viewPagerGeneral = (ViewPager) findViewById(R.id.viewpagerGeneral);

        final ViewPagerAdapterGeneralTabs viewPagerAdapter = new ViewPagerAdapterGeneralTabs(getSupportFragmentManager());
        viewPagerGeneral.setAdapter(viewPagerAdapter);


        tabLayoutGeneral = (TabLayout) findViewById(R.id.tablayoutGeneral);
        tabLayoutGeneral.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayoutGeneral.setupWithViewPager(viewPagerGeneral);
        tabLayoutGeneral.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPagerGeneral.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


        if (id == R.id.action_addUser) {
            Intent intent = new Intent(this, CreateGroupActivity.class);
            startActivity(intent);
        }

        if (id== R.id.action_search){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
