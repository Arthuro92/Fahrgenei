package com.android.cows.fahrgemeinschaft;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.cows.fahrgemeinschaft.adapters.TaskAdapter;

import java.util.ArrayList;

import de.dataobjects.Task;
//NOT IN USE ANYMORE? I THINK YES
public class FragmentGruppenAufgabenActivity extends Fragment {

    View contentViewGruppenAufgaben;
    private static final String TAG = "TaskOverview";
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentViewGruppenAufgaben = inflater.inflate(R.layout.activity_fragment_gruppen_aufgabe, null);
        return contentViewGruppenAufgaben;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Task tsk1 = new Task(1, 1, "grp1", "Trikots waschen", "Trikots müssen gewaschen und zum nächsten Spiel mitgebracht werden", "Irina");
        Task tsk2 = new Task(2, 1, "grp1", "Brötchen mitbringen", "Beim nächsten Hallenturnier bieten wir belegte Brötchen an ", "Lenni");
        Task tsk3 = new Task(3, 1, "grp1", "Leibchen waschen", "Die Leibchen müssen auch gewaschen werden.", "Arthur");
        ArrayList<Task> tsklist = new ArrayList<Task>();
        tsklist.add(tsk1);
        tsklist.add(tsk2);
        tsklist.add(tsk3);
        TaskAdapter taskAdapter = new TaskAdapter( getActivity() ,R.layout.item_row_task, tsklist);
        listView = (ListView) view.findViewById(R.id.taskListView);
        listView.setAdapter(taskAdapter);
        //createTaskOverview(tsklist);
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Creating for each Group a linearLayout
     *
     * @param tsklist list of Groups which should be displayed
     */
   /* public void createTaskOverview(final List<Task> tsklist) {
        Log.i(TAG, "createGroup");

        RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.relativelayoutGruppenAufgabe);

        LinearLayout[] verticalLayoutMain = new LinearLayout[tsklist.size()];
        LinearLayout[] verticalHeadlineLayout = new LinearLayout[tsklist.size()];
        LinearLayout[] verticalContentLayout1 = new LinearLayout[tsklist.size()];
        LinearLayout[] verticalContentLayout2 = new LinearLayout[tsklist.size()];
        RelativeLayout[] relativeLayoutWrapper = new RelativeLayout[tsklist.size()];

        int i = 0;
        while (i < tsklist.size()) {

            relativeLayoutWrapper[i] = new RelativeLayout(getActivity());
            relativeLayoutWrapper[i].setId(tsklist.size() + 1 + i);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0, 0, 0, 30);
            if (i > 0) {
                params2.addRule(RelativeLayout.BELOW, tsklist.size() + i);
                relativeLayoutWrapper[i].setLayoutParams(params2);
            } else {
                relativeLayoutWrapper[i].setLayoutParams(params2);
            }

            relativeLayoutWrapper[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, tsklist.get(v.getId() - tsklist.size() - 1).getAufgabenName());
                    Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                    intent.putExtra("taskname", tsklist.get(v.getId() - tsklist.size() - 1).getAufgabenName());
                    intent.putExtra("taskdescription", tsklist.get(v.getId() - tsklist.size() - 1).getAufgabenBeschreibung());
                    intent.putExtra("taskincharge", tsklist.get(v.getId() - tsklist.size() - 1).getBearbeiter());
                    //based on item add info to intent
                    startActivity(intent);
                }
            });


            verticalLayoutMain[i] = new LinearLayout(getActivity());
            verticalLayoutMain[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 2200);
            verticalLayoutMain[i].setLayoutParams(params);

            verticalLayoutMain[i].setOrientation(LinearLayout.VERTICAL);
            verticalLayoutMain[i].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.darkblueGrey));
            verticalLayoutMain[i].setBackgroundResource(R.drawable.boxes_background);

            verticalHeadlineLayout[i] = new LinearLayout(getActivity());
            verticalHeadlineLayout[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            verticalHeadlineLayout[i].setOrientation(LinearLayout.VERTICAL);


            verticalContentLayout1[i] = new LinearLayout(getActivity());
            verticalContentLayout1[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            verticalContentLayout1[i].setOrientation(LinearLayout.VERTICAL);

            verticalContentLayout2[i] = new LinearLayout(getActivity());
            verticalContentLayout2[i].setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            verticalContentLayout2[i].setOrientation(LinearLayout.VERTICAL);


            TextView nametxt = new TextView(getActivity());
            TextView membercounttxt = new TextView(getActivity());
            TextView admintxt = new TextView(getActivity());

            verticalHeadlineLayout[i].setPadding(50, 10, 0, 0);
            verticalContentLayout1[i].setPadding(50, 0, 0, 0);
            verticalContentLayout2[i].setPadding(50, 0, 0, 25);

            nametxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            membercounttxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            admintxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

            nametxt.setText(getString(R.string.taskName, tsklist.get(i).getAufgabenName()));
            membercounttxt.setText(getString(R.string.taskDescr, tsklist.get(i).getAufgabenBeschreibung()));
            admintxt.setText(getString(R.string.taskBearbeiter, tsklist.get(i).getBearbeiter()));

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
    }*/

}
