//package com.android.cows.fahrgemeinschaft;
//
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import com.android.cows.fahrgemeinschaft.adapters.ChatMessageAdapter;
//import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
//import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;
//
//import java.text.DateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import de.dataobjects.Chat;
//
//
//public class ChatActivity extends AppCompatActivity {
//    //version 1
//    private static final int NID = 987654321;
//    public static boolean activeActivity = false;
//    public ArrayList<Chat> arrayListChat;
//    private ChatMessageAdapter chatMessageAdapter;
//    private ListView listView;
//    private ChatReceiver chatReceiver;
//
//    /**
//     * Gets the User by accessing the shared preferences
//     * @return user String
//     */
//    private String getChatUser() {
//        SharedPreferences sharedPreferences = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
//        return sharedPreferences.getString("username", "Blubb");
//    }
//
//    /**
//     * Gets the gid
//     * @return gid
//     */
//    private String getGid() {
//        SharedPreferences sharedPreferences = this.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
//        return sharedPreferences.getString("currentgid","");
//    }
//
//    /**
//     * Adds chatMessage to local database
//     * @param chatMessage a Chat object to be added
//     */
//    private void addChatMessageDB(Chat chatMessage) {
//        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);
//        sqLiteDBHandler.addChatMessage(chatMessage);
//    }
//
//    /**
//     * Checks text against a regular expression
//     * @param text a String the regular expression is checked against
//     * @return true if regular expression holds, false else
//     */
//    private boolean checkRegEx(String text) {
//        Pattern pattern = Pattern.compile("^\\s*$");
//        Matcher matcher = pattern.matcher(text);
//        return matcher.find();
//    }
//
//    /**
//     * Sends a Chat object to the server
//     * @param chatMessage
//     */
//    private void sendChatMessage(Chat chatMessage) {
//        MyGcmSend<Chat> myGcmSend = new MyGcmSend<Chat>();
//        addChatMessageDB(chatMessage);
//        this.arrayListChat.add(chatMessage);
//        this.chatMessageAdapter.notifyDataSetChanged();
//        this.listView.setSelection(listView.getAdapter().getCount() - 1);
//        myGcmSend.send("chat", "chat", chatMessage, ChatActivity.this);
//    }
//
//    /**
//     * Generates chat message from input
//     */
//    private void getChatMessage() {
//        EditText editText = (EditText) findViewById(R.id.edit_text_message);
//        String time = DateFormat.getDateTimeInstance().format(new Date());
//        if(!checkRegEx(editText.getText().toString())) {
//            sendChatMessage(new Chat(getChatUser(), time, editText.getText().toString(), getGid()));
//        }
//    }
//
//    /**
//     * Adds Chat object to ArrayList from Intent
//     * @param intent an Intent with an Extra to be added
//     */
//    public void setArrayListFromExtra(Intent intent) {
//        Chat chatMessage = (Chat) intent.getSerializableExtra("Chat");
//        if(chatMessage.getGid().equals(getGid())) {
//            this.arrayListChat.add(chatMessage);
//            this.chatMessageAdapter.notifyDataSetChanged();
//            this.listView.setSelection(listView.getAdapter().getCount() - 1);
//        }
//    }
//
//    /**
//     * Sets gui objects and displays chat messages
//     */
//    private void setChatView() {
//        this.chatMessageAdapter = new ChatMessageAdapter(this, arrayListChat);
//        this.listView = (ListView) findViewById(R.id.chat_list_view);
//        this.listView.setAdapter(chatMessageAdapter);
//        Button send = (Button) findViewById(R.id.send_message_button);
//        send.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                getChatMessage();
//            }
//        });
//    }
//
//    /**
//     * Gets chat history from local database
//     * @return an ArrayList containing the history
//     */
//    private ArrayList<Chat> getArrayListFromDB() {
//        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this, null);
//        return sqLiteDBHandler.getChatMessages(getGid());
//    }
//
//    /**
//     * Sets a Receiver to get new chat messages
//     */
//    private void setChatReceiver() {
//        this.chatReceiver = new ChatReceiver(this);
//        IntentFilter intentFilter = new IntentFilter("com.android.cows.fahrgemeinschaft.UPDATECHAT");
//        registerReceiver(this.chatReceiver, intentFilter);
//    }
//
//    /**
//     * Sets the ArrayList when app is created
//     * @param savedInstanceState
//     */
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//        this.arrayListChat = getArrayListFromDB();
//        setChatView();
//        this.listView.setSelection(listView.getAdapter().getCount() - 1);
//        System.out.println("CHATACTIVITY CREATED");
//    }
//
//    /**
//     * Cancels first notification and sets activeActivity to true/active, also starts receiver
//     */
//    @Override
//    protected void onStart() {
//        super.onStart();
//        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        nm.cancel(NID);
//        activeActivity = true;
//        setChatReceiver();
//        System.out.println("CHATACTIVITY STARTED");
//    }
//
//    /**
//     * Sets activeActivity to false/not active on app stop, also unregisters receiver
//     */
//    @Override
//    protected void onStop() {
//        super.onStop();
//        activeActivity = false;
//        unregisterReceiver(this.chatReceiver);
//        System.out.println("CHATACTIVITY STOPPED");
//    }
//}
