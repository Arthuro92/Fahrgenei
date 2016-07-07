package com.android.cows.fahrgemeinschaft;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;

import de.dataobjects.Appointment;
import de.dataobjects.Task;

public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "EditTaskActivity";

    public EditText terminName;
    public EditText taskDescription;
    public EditText taskAssignee;
    public Button saveTaskData;
    private Context context = GlobalAppContext.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        final TextView taskName = (TextView) findViewById(R.id.input_taskname);
        final TextView taskDescription = (TextView) findViewById(R.id.input_description);
        final TextView taskAssignee = (TextView) findViewById(R.id.input_assignee);
        final Button savaTaskData = (Button) findViewById(R.id.saveTaskButton);



        Bundle bundle = getIntent().getExtras();

        taskName.setText(bundle.getString("taskname"));
        taskDescription.setText(bundle.getString("taskdescription"));
        taskAssignee.setText(bundle.getString("taskincharge"));
        final int aid = (int) bundle.getSerializable("aid");
        final int tid = (int) bundle.getSerializable("tid");


        savaTaskData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //@TODO SQLITE DB und SERVER DB MUESSEN DEN EINTRAG FÜR DEN TERMIN ÄNDERN
                //newtask

                //Läuft  leider ab hier noch nicht
                MyGcmSend gcmsend = new MyGcmSend();
                SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
                Task tsk = new Task(tid, aid, prefs.getString("currentgid",""),  taskName.getText().toString(), taskDescription.getText().toString(), taskAssignee.getText().toString());


                Intent intent = new Intent(context, TaskDetailActivity.class);
                Log.i("Taskdaten",taskDescription +" "+taskAssignee.getText());
                intent.putExtra("taskname", taskName.getText().toString()  );
                intent.putExtra("taskdescription", taskDescription.getText().toString()  );
                intent.putExtra("taskincharge", taskAssignee.getText().toString()  );
                gcmsend.send("task", "newtask", tsk, context);

                startActivity(intent);
                finish();
            }
        });
    }
}