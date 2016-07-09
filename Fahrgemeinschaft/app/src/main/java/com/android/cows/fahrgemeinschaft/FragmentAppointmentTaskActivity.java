package com.android.cows.fahrgemeinschaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.cows.fahrgemeinschaft.adapters.TaskAdapter;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

import de.dataobjects.Task;

public class FragmentAppointmentTaskActivity extends Fragment {

    View contentViewGruppenAufgaben;
    private static final String TAG = "TaskOverview";
    private TaskAdapter taskAdapter;
    private ListView listView;
    private BroadcastReceiver updateTaskList;
    private boolean isReceiverRegistered;
    private Context context = GlobalAppContext.getAppContext();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentViewGruppenAufgaben = inflater.inflate(R.layout.activity_fragment_gruppen_aufgabe, null);


        return contentViewGruppenAufgaben;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadTaskList();
        createReceiver();




        /**
        TaskAdapter taskAdapter = new TaskAdapter( getActivity() ,R.layout.item_row_task, tsklist);
        listView = (ListView) view.findViewById(R.id.taskListView);
        listView.setAdapter(taskAdapter);
        //createTaskOverview(tsklist);*/
        super.onViewCreated(view, savedInstanceState);
    }

    public void createTasks(ArrayList<Task> tasktArrayList) {
        Log.i(TAG, "createAppointments");
        this.taskAdapter = new TaskAdapter(getActivity(), R.layout.item_row_task, tasktArrayList);
        this.listView = (ListView) getActivity().findViewById(R.id.taskListView);
        this.listView.setAdapter(taskAdapter);
    }
    public void createReceiver() {
        updateTaskList = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("update in appointment fired");
                loadTaskList();
            }
        };
        registerReceiver();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateTaskList, new IntentFilter("createdTask"));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateTaskList);
            isReceiverRegistered = false;
        }
    }

    private void loadTaskList() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String gid = prefs.getString("currentgid", "");

        //Task tsk1 = new Task(1, 1, "grp1", "Trikots waschen", "Trikots müssen gewaschen und zum nächsten Spiel mitgebracht werden", "Irina");
        //Task tsk2 = new Task(2, 1, "grp1", "Brötchen mitbringen", "Beim nächsten Hallenturnier bieten wir belegte Brötchen an ", "Lenni");
        //Task tsk3 = new Task(3, 1, "grp1", "Leibchen waschen", "Die Leibchen müssen auch gewaschen werden.", "Arthur");

//        Bundle bundle = getActivity().getIntent().getExtras();
//        int aid = (int) bundle.getSerializable("aid");

        int aid = prefs.getInt("currentaid",0);
        Log.i(TAG, "CURRENT AID " + aid);
        ArrayList<Task> tsklist = sqLiteDBHandler.getTasks(aid, gid );
        //Log.i("Taskdaten:", tsklist.get(0).getTaskName().toString());
        //= new ArrayList<Task>();
        //tsklist.add(tsk1);
        //tsklist.add(tsk2);
        //tsklist.add(tsk3);

        if (tsklist.size() > 0) {
            createTasks(tsklist);
        } else {
            CharSequence text = "Keine Aufgaben verfügbar!";
            Toast toast = Toast.makeText(FragmentAppointmentTaskActivity.this.getActivity(), text, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}


