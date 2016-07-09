package com.android.cows.fahrgemeinschaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import de.dataobjects.Task;

public class TaskDetailActivity extends AppCompatActivity {

    private static final String TAG = "TaskDetailActivity";

    public TextView taskName;
    public TextView taskDescr;
    public TextView taskAssignee;
    public Toolbar toolbar;
    private BroadcastReceiver returntogroupgeneral;
    private boolean isReceiverRegistered;
    private Context context = GlobalAppContext.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        TextView taskName = (TextView) findViewById(R.id.taskName);
        TextView taskDescr = (TextView) findViewById(R.id.taskDescr);
        TextView taskAssignee = (TextView) findViewById(R.id.taskAssignee);

    /*    String taskname = "Aufgabenname";
        String taskdescr = "Dies ist nur ein Test. Damit ich den TextView mal Testen kann";
        String taskassignee = "Irina";*/


        Bundle bundle = getIntent().getExtras();
        // String test = bundle.getString("test");

        //taskName.setText(bundle.getString("taskname"));
        taskDescr.setText(bundle.getString("taskdescription"));
        taskAssignee.setText(bundle.getString("taskincharge"));
        setTitle(bundle.getString("taskname"));
        createReceiver();
    }

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
            Intent intent = new Intent(context, EditTaskActivity.class);
            int aid = (int) bundle.getSerializable("aid");
            int tid = (int)  bundle.getSerializable("tid");
            intent.putExtra("tid", tid );
            intent.putExtra("aid", aid );
            intent.putExtra("taskdescription", bundle.getSerializable("taskdescription") );
            intent.putExtra("taskincharge", bundle.getString("taskincharge"));
            intent.putExtra("taskname", bundle.getString("taskname"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }

        if (id == R.id.action_delete_event) {
            //@TODO Lenni oder David! Bitte vom Server löschen.
            SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
            SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
            String gid = prefs.getString("currentgid", "");
            Bundle bundle = getIntent().getExtras();

            int aid = (int) bundle.getSerializable("aid");
            int tid = (int) bundle.getSerializable("tid");

            Task task = new Task(tid, aid, gid, "", "", "");

            sqLiteDBHandler.deleteTask(tid, gid, aid);
            MyGcmSend myGcmSend = new MyGcmSend();
            myGcmSend.send("task", "deletetask",task ,context) ;

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void createReceiver() {
        returntogroupgeneral = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(context).registerReceiver(returntogroupgeneral, new IntentFilter("returntogeneralgroups"));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(returntogroupgeneral);
            isReceiverRegistered = false;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver();
    }
}
