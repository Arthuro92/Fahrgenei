package com.android.cows.fahrgemeinschaft;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GeneralTabsActivity extends AppCompatActivity {



    ViewPager viewPagerGeneral;
    TabLayout tabLayoutGeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_group_tabs);



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

            }});
        }

    public void createGroup(View view) {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }
}
