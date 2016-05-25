package com.android.cows.fahrgemeinschaft;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.dataobjects.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupOverview extends AppCompatActivity {
    private static final String TAG = "GroupOverview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_overview);
        Group grp1 = new Group("grp1", 1, "lennart", "lennart");
        Group grp2 = new Group("grp2", 2, "david", "david");
        Group grp3 = new Group("grp3", 3, "irina", "irina");
        Group grp4 = new Group("grp4", 4, "irina", "irina");
        Group grp5 = new Group("grp5", 5, "irina", "irina");
        Group grp6 = new Group("grp6", 6, "irina", "irina");
        Group grp7 = new Group("grp7", 7, "irina", "irina");
        Group grp8 = new Group("grp8", 8, "irina", "irina");
        List<Group> grplist = new ArrayList<>();
        grplist.add(grp1);
        grplist.add(grp2);
        grplist.add(grp3);
        grplist.add(grp4);
        grplist.add(grp5);
        grplist.add(grp6);
        grplist.add(grp7);
        grplist.add(grp8);

        createGroupOverview(grplist);


    }

    /**
     * Creating for each Group a linearLayout
     * @param grplist list of Groups which should be displayed
     */
    public void createGroupOverview(final List<Group> grplist) {
        Log.i(TAG, "createGroup");

        //TODO maybe better solution for it?

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);

        LinearLayout[] verticalLayoutMain = new LinearLayout[grplist.size()];
        LinearLayout[] verticalHeadlineLayout = new LinearLayout[grplist.size()];
        LinearLayout[] verticalContentLayout1 = new LinearLayout[grplist.size()];
        LinearLayout[] verticalContentLayout2 = new LinearLayout[grplist.size()];
        RelativeLayout[] relativeLayoutWrapper = new RelativeLayout[grplist.size()];

        int i = 0;
        while(i < grplist.size()) {

            relativeLayoutWrapper[i] = new RelativeLayout(this);
            relativeLayoutWrapper[i].setId(grplist.size()+1+i);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0,0,0,30);
            if(i > 0) {
                params2.addRule(RelativeLayout.BELOW, grplist.size()+i);
                relativeLayoutWrapper[i].setLayoutParams(params2);
            } else {
                relativeLayoutWrapper[i].setLayoutParams(params2);
            }

            relativeLayoutWrapper[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, grplist.get(v.getId()-grplist.size()-1).getName());
                }
            });


            verticalLayoutMain[i] = new LinearLayout(this);
            verticalLayoutMain[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,2200);
            verticalLayoutMain[i].setLayoutParams(params);

            verticalLayoutMain[i].setOrientation(LinearLayout.VERTICAL);
            verticalLayoutMain[i].setBackgroundColor(ContextCompat.getColor(this, R.color.blue_grey_500));

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

            nametxt.setText(getString(R.string.grpname, grplist.get(i).getName()));
            membercounttxt.setText(getString(R.string.membercount, Integer.toString(grplist.get(i).getMembercount())));
            admintxt.setText(getString(R.string.grpadmin, grplist.get(i).getAdminname()));

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