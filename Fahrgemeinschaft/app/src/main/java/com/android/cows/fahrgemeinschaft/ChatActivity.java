package com.android.cows.fahrgemeinschaft;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.android.cows.fahrgemeinschaft.dataobjects.Chat;
import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatActivity extends AppCompatActivity {
    private static final int NID = 987654321;
    public static boolean activeActivity = false;
    private ArrayList<Chat> arrayListChat = new ArrayList<Chat>();
    private ChatMessageAdapter chatMessageAdapter;
    private ListView lv;
//    private SQLiteDBHandler dbh;

//    private ArrayList<Chat> setAlcFromDatabase() {
//        return this.dbh.getChatMessages();
//    }

    /**
     * Gets the User by accessing the shared preferences
     * @return user String
     */
    private String getChatUser() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "Blubb");
    }

    /**
     * Sends a Chat object to the server
     * @param chatMessage
     */
    private void sendChatMessage(Chat chatMessage) {
        MyGcmSend<Chat> myGcmSend = new MyGcmSend<Chat>();
        this.arrayListChat.add(chatMessage);
        this.chatMessageAdapter.notifyDataSetChanged();
        myGcmSend.sendP("chat", "chat", chatMessage, ChatActivity.this);
    }

    /**
     * Generates chat message from input
     */
    private void getChatMessage() {
        EditText editText = (EditText) findViewById(R.id.edit_text_message);
        String time = DateFormat.getDateTimeInstance().format(new Date());
        if(!editText.getText().toString().equals("")) {
            sendChatMessage(new Chat(getChatUser(), time, editText.getText().toString()));
        }
    }

    /**
     * Adds Chat object to ArrayList from Intent
     * @param intent an Intent with an Extra to be added
     */
    private void setArrayListFromExtra(Intent intent) {
        Chat chatMessage = (Chat) intent.getSerializableExtra("Chat");
        this.arrayListChat.add(chatMessage);
        this.chatMessageAdapter.notifyDataSetChanged();
    }

    /**
     * Sets gui objects and displays chat messages
     */
    private void setChatView() {
        this.chatMessageAdapter = new ChatMessageAdapter(this, arrayListChat);
        this.lv = (ListView) findViewById(R.id.chat_list_view);
        this.lv.setAdapter(chatMessageAdapter);
        Button send = (Button) findViewById(R.id.send_message_button);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getChatMessage();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setChatView();
//        dbh = new SQLiteDBHandler(this, null);
//        alc = setAlcFromDatabase();
//        this.cma.notifyDataSetChanged();
        //todo check if intent has extra
        setArrayListFromExtra(getIntent());
        System.out.println("CHATACTIVITY CREATED");
    }

    @Override
    protected void onStart() {
        super.onStart();
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NID);
        activeActivity = true;
        System.out.println("CHATACTIVITY STARTED");
    }

    @Override
    protected void onStop() {
        super.onStop();
        activeActivity = false;
        System.out.println("CHATACTIVITY STOPPED");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setArrayListFromExtra(intent);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NID);
        System.out.println("NEWINTENT TRIGGERED");
    }
}
