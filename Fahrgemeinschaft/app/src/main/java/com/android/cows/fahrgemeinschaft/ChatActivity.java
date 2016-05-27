package com.android.cows.fahrgemeinschaft;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.android.cows.fahrgemeinschaft.dataobjects.Chat;
import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.observer.NotificationObserver;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatActivity extends AppCompatActivity {
    private static boolean activeActivity;
    private ListView lv;
    private static ArrayList<Chat> alc = setCalFromDatabase();
    private ChatMessageAdapter cma;
    private MyGcmSend<Chat> mgsc = new MyGcmSend<Chat>();

    private static ArrayList<Chat> setCalFromDatabase() {
        //todo remove adds and add db method
        ArrayList<Chat> alcdb = new ArrayList<Chat>();
        return alcdb;
    }
    //todo find way to notifydatasetchange
    public static void setCalFromObserver(Chat c) {
            alc.add(c);
    }

    private void sendChatMessage() {
        EditText et = (EditText) findViewById(R.id.edit_text_message);
        String time = DateFormat.getDateTimeInstance().format(new Date());
        if(!et.getText().toString().equals("")) {
            //todo unique username chat
            Chat c = new Chat("User1", time, et.getText().toString());
            alc.add(c);
            cma.notifyDataSetChanged();
            mgsc.sendP("chat", "chat", c, ChatActivity.this);
        }
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
        Button send = (Button) findViewById(R.id.send_message_button);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendChatMessage();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        activeActivity = true;
    }
}
