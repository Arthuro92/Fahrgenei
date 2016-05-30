package com.android.cows.fahrgemeinschaft;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private ArrayList<Chat> arrayListChat;
    private ChatMessageAdapter chatMessageAdapter;
    private ListView lv;

    /**
     * Gets the User by accessing the shared preferences
     * @return user String
     */
    private String getChatUser() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "Blubb");
    }

    /**
     * Adds chatMessage to local database
     * @param chatMessage a Chat object to be added
     */
    private void addChatMessageDB(Chat chatMessage) {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);
        sqLiteDBHandler.addChatMessage(chatMessage);
    }

    /**
     * Sends a Chat object to the server
     * @param chatMessage
     */
    private void sendChatMessage(Chat chatMessage) {
        MyGcmSend<Chat> myGcmSend = new MyGcmSend<Chat>();
        addChatMessageDB(chatMessage);
        this.arrayListChat.add(chatMessage);
        this.chatMessageAdapter.notifyDataSetChanged();
        myGcmSend.send("chat", "chat", chatMessage, ChatActivity.this);
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

    /**
     * Gets chat history from local database
     * @return an ArrayList containing the history
     */
    private ArrayList<Chat> getArrayListFromDB() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);
        return sqLiteDBHandler.getChatMessages();
    }

    /**
     * Sets the ArrayList when app is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        arrayListChat = getArrayListFromDB();
        setChatView();
        if(getIntent().hasExtra("Chat")) {
            setArrayListFromExtra(getIntent());
        }
        System.out.println("CHATACTIVITY CREATED");
    }

    /**
     * Cancels first notification and sets activeActivity to true/active
     */
    @Override
    protected void onStart() {
        super.onStart();
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NID);
        activeActivity = true;
        System.out.println("CHATACTIVITY STARTED");
    }

    /**
     * Sets activeActivity to false/not active on app stop
     */
    @Override
    protected void onStop() {
        super.onStop();
        activeActivity = false;
        System.out.println("CHATACTIVITY STOPPED");
    }

    /**
     * Handles new Intents while Activity is active
     * @param intent an Intent triggered while Activity in foreground
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.hasExtra("Chat")) {
            setArrayListFromExtra(intent);
        }
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NID);
        System.out.println("NEWINTENT TRIGGERED");
    }
}
