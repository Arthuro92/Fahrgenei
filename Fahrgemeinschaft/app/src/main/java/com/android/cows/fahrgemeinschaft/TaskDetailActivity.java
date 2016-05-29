package com.android.cows.fahrgemeinschaft;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

import org.w3c.dom.Text;

public class TaskDetailActivity extends AppCompatActivity {

    private static final String TAG = "TaskDetailActivity";

    public TextView taskName;
    public TextView taskDescr;
    public TextView taskAssignee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        TextView taskName = (TextView) findViewById(R.id.taskName);
        TextView taskDescr = (TextView) findViewById(R.id.taskDescr);
        TextView taskAssignee = (TextView) findViewById(R.id.taskAssignee);

    /*    String taskname = "Aufgabenname";
        String taskdescr = "Dies ist nur ein Test. Damit ich den TextView mal Testen kann";
        String taskassignee = "Irina";*/


        Bundle bundle = getIntent().getExtras();
       // String test = bundle.getString("test");

        taskName.setText(bundle.getString("test"));
        taskDescr.setText(bundle.getString("test2"));
        taskAssignee.setText(bundle.getString("test3"));


    }
}
