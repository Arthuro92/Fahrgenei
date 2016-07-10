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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.cows.fahrgemeinschaft.adapters.TaskAdapter;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.util.ArrayList;

import de.dataobjects.Groups;
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
        setHasOptionsMenu(true);

        return contentViewGruppenAufgaben;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadTaskList();
        createReceiver();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem adduser = menu.findItem(R.id.action_edit_event);
        MenuItem deletegrp = menu.findItem(R.id.action_delete_event);
        MenuItem addtask = menu.findItem(R.id.action_create_task);

        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String gid = prefs.getString("currentgid", "");
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);

        Groups groups = sqLiteDBHandler.getGroup(gid);

        if(!groups.getAdminid().equals(prefs.getString("userid", "")) && groups.getSubstitute() == null ||groups.getSubstitute() != null && !groups.getSubstitute().equals(prefs.getString("userid",""))) {
            adduser.setVisible(false);
            deletegrp.setVisible(false);
            addtask.setVisible(false);
        } else {
            adduser.setVisible(false);
            deletegrp.setVisible(false);
            addtask.setVisible(true);
        }
    }

    /**
     * Create ListView with TaskList
     * @param tasktArrayList tasks
     */
    public void createTasks(ArrayList<Task> tasktArrayList) {
        Log.i(TAG, "create Tasks");
        this.taskAdapter = new TaskAdapter(getActivity(), R.layout.item_row_task, tasktArrayList);
        this.listView = (ListView) getActivity().findViewById(R.id.taskListView);
        this.listView.setAdapter(taskAdapter);
    }

    /**
     * create receiver
     */
    public void createReceiver() {
        updateTaskList = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("update in task fired");
                loadTaskList();
            }
        };
        registerReceiver();
    }

    /**
     * register receiver
     */
    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateTaskList, new IntentFilter("createdTask"));
            isReceiverRegistered = true;
        }
    }

    /**
     * unregister receiver
     */
    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateTaskList);
            isReceiverRegistered = false;
        }
    }

    /**
     * load all tasks for specific appointment
     */
    private void loadTaskList() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(context, null);
        SharedPreferences prefs = context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        String gid = prefs.getString("currentgid", "");
        int aid = prefs.getInt("currentaid", 0);
        Log.i(TAG, "CURRENT AID " + aid);
        ArrayList<Task> tsklist = sqLiteDBHandler.getTasks(aid, gid);
        createTasks(tsklist);
    }
}


