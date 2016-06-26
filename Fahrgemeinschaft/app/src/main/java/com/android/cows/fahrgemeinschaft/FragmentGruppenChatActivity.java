package com.android.cows.fahrgemeinschaft;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.cows.fahrgemeinschaft.adapters.ChatMessageAdapter;
import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;
import com.android.cows.fahrgemeinschaft.sqlite.database.SQLiteDBHandler;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.dataobjects.Chat;

public class FragmentGruppenChatActivity extends Fragment {

    View contentViewGruppenChat;
    //new new new
    private static final int NID = 987654321;
    public static boolean activeActivity = false;
    private ArrayList<de.dataobjects.Chat> arrayListChat;
    private ChatMessageAdapter chatMessageAdapter;
    private ListView lv;



    /**
     * Gets the User by accessing the shared preferences
     *
     * @return user String
     */
    private String getChatUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "Blubb");
    }

    /**
     * Adds chatMessage to local database
     *
     * @param chatMessage a Chat object to be added
     */
    private void addChatMessageDB(de.dataobjects.Chat chatMessage) {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        sqLiteDBHandler.addChatMessage(chatMessage);
    }

    /**
     * Sends a Chat object to the server
     *
     * @param chatMessage
     */
    private void sendChatMessage(de.dataobjects.Chat chatMessage) {
        MyGcmSend<Chat> myGcmSend = new MyGcmSend<de.dataobjects.Chat>();
        addChatMessageDB(chatMessage);
        this.arrayListChat.add(chatMessage);
        this.chatMessageAdapter.notifyDataSetChanged();
        myGcmSend.send("chat", "chat", chatMessage, getActivity());
    }

    /**
     * Generates chat message from input
     */
    private void getChatMessage() {
        EditText editText = (EditText) getActivity().findViewById(R.id.edit_text_message);
        String time = DateFormat.getDateTimeInstance().format(new Date());
        if (!editText.getText().toString().equals("")) {
            sendChatMessage(new de.dataobjects.Chat(getChatUser(), time, editText.getText().toString()));
        }

        editText.setText("");

    }

    /**
     * Adds Chat object to ArrayList from Intent
     *
     * @param intent an Intent with an Extra to be added
     */
    private void setArrayListFromExtra(Intent intent) {
        de.dataobjects.Chat chatMessage = (de.dataobjects.Chat) intent.getSerializableExtra("Chat");
        this.arrayListChat.add(chatMessage);
        this.chatMessageAdapter.notifyDataSetChanged();
    }

    /**
     * Sets gui objects and displays chat messages
     */
    private void setChatView() {
        this.chatMessageAdapter = new ChatMessageAdapter(getActivity(), arrayListChat);
        this.lv = (ListView) getActivity().findViewById(R.id.chat_list_view);
        this.lv.setAdapter(chatMessageAdapter);
        Button send = (Button) getActivity().findViewById(R.id.send_message_button);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getChatMessage();
            }
        });
    }

    /**
     * Gets chat history from local database
     *
     * @return an ArrayList containing the history
     */
    private ArrayList<de.dataobjects.Chat> getArrayListFromDB() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(getActivity(), null);
        return sqLiteDBHandler.getChatMessages();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentViewGruppenChat = inflater.inflate(R.layout.activity_fragment_gruppen_chat, null);


        return contentViewGruppenChat;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.arrayListChat = getArrayListFromDB();
        setChatView();
        System.out.println("CHATACTIVITY CREATED");
    }

    /**
     * Cancels first notification and sets activeActivity to true/active
     */
    @Override
    public void onStart() {
        super.onStart();
        NotificationManager nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NID);
        activeActivity = true;
        System.out.println("CHATACTIVITY STARTED");
    }

    /**
     * Sets activeActivity to false/not active on app stop
     */
    @Override
    public void onStop() {
        super.onStop();
        activeActivity = false;
        System.out.println("CHATACTIVITY STOPPED");
    }

    /**
     * Handles new Intents while Activity is active
     * @param intent an Intent triggered while Activity in foreground
     */
    //todo implement this
   /* @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.hasExtra("Chat")) {
            setArrayListFromExtra(intent);
        }
        NotificationManager nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NID);
        System.out.println("NEWINTENT TRIGGERED");
    }*/
}
