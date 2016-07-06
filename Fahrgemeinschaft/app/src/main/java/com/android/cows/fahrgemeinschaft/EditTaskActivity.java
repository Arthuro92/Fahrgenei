package com.android.cows.fahrgemeinschaft;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "EditTaskActivity";

    public TextView terminName;
    public TextView taskDescription;
    public TextView taskAssignee;
    public Button saveTaskData;
    private Context context = GlobalAppContext.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);

        final TextView terminName = (TextView) findViewById(R.id.input_taskname);
        final TextView taskDescription = (TextView) findViewById(R.id.input_description);
        final TextView taskAssignee = (TextView) findViewById(R.id.input_assignee);
        final Button savaTaskData = (Button) findViewById(R.id.saveTaskButton);



        Bundle bundle = getIntent().getExtras();

        terminName.setText(bundle.getString("taskname"));
        taskDescription.setText(bundle.getString("taskdescription"));
        taskAssignee.setText(bundle.getString("taskincharge"));


        savaTaskData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //@TODO SQLITE DB und SERVER DB MUESSEN DEN EINTRAG FÜR DEN TERMIN ÄNDERN
                Intent intent = new Intent(context, TaskDetailActivity.class);
                Log.i("Taskdaten",taskDescription +" "+taskAssignee.getText());
                intent.putExtra("taskname", terminName.getText().toString()  );
                intent.putExtra("taskdescription", taskDescription.getText().toString()  );
                intent.putExtra("taskincharge", taskAssignee.getText().toString()  );
                startActivity(intent);
            }
        });
    }
}