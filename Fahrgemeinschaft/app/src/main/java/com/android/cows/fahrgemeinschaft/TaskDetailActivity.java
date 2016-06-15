package com.android.cows.fahrgemeinschaft;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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

        taskName.setText(bundle.getString("taskname"));
        taskDescr.setText(bundle.getString("taskdescription"));
        taskAssignee.setText(bundle.getString("taskincharge"));


    }
}
