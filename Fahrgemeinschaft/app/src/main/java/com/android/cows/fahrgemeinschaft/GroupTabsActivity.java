package com.android.cows.fahrgemeinschaft;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class GroupTabsActivity extends AppCompatActivity {


    ViewPager viewPagerGroup;
    TabLayout tabLayoutGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_tabs);


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
    }
}
