package com.android.cows.fahrgemeinschaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dataobjects.Task;

public class CreateTaskActivity extends AppCompatActivity {
    private static final String TAG = "CreateTaskActivity";
    private BroadcastReceiver insertTaskSuccess;
    private BroadcastReceiver errorReceivingAppointment;
    private ProgressBar mRegistrationProgressBar;
    private boolean isReceiverRegistered;
    private Button createTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.createTaskProgBar);
        createTaskButton = (Button) findViewById(R.id.button123);
        mRegistrationProgressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createTask2(v);
            }
        });
    }

    public void createTask2(View view) {

        SharedPreferences prefs = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        EditText taskName = (EditText) findViewById(R.id.taskname);
        EditText taskDescription = (EditText) findViewById(R.id.taskdescription);
        //todo this has to come from the specific appointment
        Bundle bundle = getIntent().getExtras();
        int aid = (int) bundle.getSerializable("aid");
        String gid = prefs.getString("currentgid","");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);
        int tid = sqLiteDBHandler.getNextTaskID(gid, aid);
        if(!checkRegEx(taskName.getText().toString()) && !checkRegEx(taskDescription.toString())) {

          Task newTask; //= new Task("1", aid, gid, taskName.toString(), taskDescription.toString(), "");

            if(tid == 0) {
                Log.i(TAG, "no appointments, create appointment with id 1");
                newTask = new Task(1, aid, gid, taskName.getText().toString(), taskDescription.getText().toString(), "");
            } else {
                tid ++;
                Log.i(TAG, "Create Appointment with id " + aid);
               // String taskId = Integer.toString(tid);
                newTask = new Task(tid, aid, gid , taskName.getText().toString(), taskDescription.getText().toString(), "");
            }




            MyGcmSend gcmsend = new MyGcmSend();
            gcmsend.send("task", "newtask", newTask, this);
        Log.i(TAG, "New Task created and send to server, waiting now for response");
            createReceiver();
        }
    }

    /**
     * Checks text against a regular expression
     * @param text a String the regular expression is checked against
     * @return true if regular expression holds, false else
     */
    private boolean checkRegEx(String text) {
        Pattern pattern = Pattern.compile("^\\s*$");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    @SuppressWarnings("ConstantConditions")
    private void setLayoutInvisible() {
        findViewById(R.id.createAppointmentProgBar).setVisibility(ProgressBar.VISIBLE);
        findViewById(R.id.createappointbutton).setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("ConstantConditions")
    private void setLayoutVisible() {
        findViewById(R.id.createAppointmentProgBar).setVisibility(ProgressBar.GONE);
        findViewById(R.id.createappointbutton).setVisibility(View.VISIBLE);
    }


    private void createReceiver() {
        insertTaskSuccess = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver();
                Intent intent2 = new Intent(CreateTaskActivity.this, AppointmentTabsActivity.class);
                Bundle bundle = getIntent().getExtras();
                intent2.putExtra("aid", bundle.getSerializable("aid") );
                intent2.putExtra("name", bundle.getString("name"));
                intent2.putExtra("startingtime", bundle.getString("startingtime"));
                intent2.putExtra("meetingpoint", bundle.getString("meetingpoint"));
                intent2.putExtra("meetingtime", bundle.getString("meetingtime"));
                intent2.putExtra("destination", bundle.getString("destination"));
                startActivity(intent2);
                finish();
            }
        };

        errorReceivingAppointment = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                CharSequence text = "" + bundle.get("error");
                Toast toast = Toast.makeText(CreateTaskActivity.this, text, Toast.LENGTH_LONG);
                toast.show();
                setLayoutVisible();
                unregisterReceiver();
            }
        };
        registerReceiver();
    }


    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(insertTaskSuccess, new IntentFilter("createdTask"));
            LocalBroadcastManager.getInstance(this).registerReceiver(errorReceivingAppointment, new IntentFilter("ERRORTask"));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(insertTaskSuccess);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(errorReceivingAppointment);
            isReceiverRegistered = false;
        }
        //todo do we need unregistering for receiver?
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}
