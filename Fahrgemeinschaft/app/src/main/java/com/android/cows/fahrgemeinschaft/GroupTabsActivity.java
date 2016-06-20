package com.android.cows.fahrgemeinschaft;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class GroupTabsActivity extends AppCompatActivity {


    ViewPager viewPagerGroup;
    TabLayout tabLayoutGroup;
    Toolbar toolbar;

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
        if (id ==R.id.action_add_person){
            Intent intent = new Intent(GroupTabsActivity.this, InviteUser.class);
            Bundle bundle = GroupTabsActivity.this.getIntent().getExtras();
            intent.putExtra("gid", bundle.getString("gid"));

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
