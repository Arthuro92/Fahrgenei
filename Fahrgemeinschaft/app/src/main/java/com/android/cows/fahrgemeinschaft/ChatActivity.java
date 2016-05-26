package com.android.cows.fahrgemeinschaft;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.android.cows.fahrgemeinschaft.dataobjects.Chat;
import com.android.cows.fahrgemeinschaft.observer.NotificationObserver;
import java.util.ArrayList;


public class ChatActivity extends AppCompatActivity {
    private static boolean activeActivity;
    private ListView lv;
    private static ArrayList<Chat> alc = setCalFromDatabase();
    private ChatMessageAdapter cma;

    private static ArrayList<Chat> setCalFromDatabase() {
        //todo remove adds and add db method
        ArrayList<Chat> alcdb = new ArrayList<Chat>();
        alcdb.add(new Chat("user1", "8.19", "Hallo"));
        alcdb.add(new Chat("user2", "8.20", "Hallo"));
        return alcdb;
    }

    public static void setCalFromObserver(Chat c) {
            alc.add(c);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NotificationObserver.NID);
        System.out.println("CHATACTIVITY STARTED");
        this.setCalFromDatabase();
        lv = (ListView) findViewById(R.id.chat_list_view);
        cma = new ChatMessageAdapter(this, this.alc);
        lv.setAdapter(cma);
    }

    @Override
    protected void onStart() {
        super.onStart();
        activeActivity = true;
    }
}
