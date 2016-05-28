package com.android.cows.fahrgemeinschaft;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.android.cows.fahrgemeinschaft.dataobjects.Chat;
import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatActivity extends AppCompatActivity {
    private static final int NID = 987654321;
    private MyGcmSend<Chat> mgsc = new MyGcmSend<Chat>();
    private ArrayList<Chat> alc = setAlcFromDatabase();
    private Chat c;
    private ChatMessageAdapter cma;
    private ListView lv;
    private boolean activeActivity;


    private ArrayList<Chat> setAlcFromDatabase() {
        //todo remove adds and add db method
        ArrayList<Chat> alcdb = new ArrayList<Chat>();
        return alcdb;
    }

    private void setAlcFromExtra(Intent i) {
        this.c = (Chat) i.getSerializableExtra("Chat");
        this.alc.add(this.c);
        this.cma.notifyDataSetChanged();
    }

    private void sendChatMessage(Chat c) {
        this.alc.add(c);
        this.cma.notifyDataSetChanged();
        this.mgsc.sendP("chat", "chat", c, ChatActivity.this);
    }

    private void getChatMessage() {
        EditText et = (EditText) findViewById(R.id.edit_text_message);
        String time = DateFormat.getDateTimeInstance().format(new Date());
        //todo unique username chat
        if(!et.getText().toString().equals("")) {
            sendChatMessage(new Chat("User1", time, et.getText().toString()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.cma = new ChatMessageAdapter(this, alc);
        setAlcFromDatabase();
        //todo check if intent has extra 
        setAlcFromExtra(getIntent());
        this.lv = (ListView) findViewById(R.id.chat_list_view);
        this.lv.setAdapter(cma);
        Button send = (Button) findViewById(R.id.send_message_button);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getChatMessage();
            }
        });
        System.out.println("CHATACTIVITY CREATED");
    }

    @Override
    protected void onStart() {
        super.onStart();
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NID);
        this.activeActivity = true;
        System.out.println("CHATACTIVITY STARTED");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setAlcFromExtra(intent);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NID);
        System.out.println("NEWINTENT TRIGGERED");
    }
}
