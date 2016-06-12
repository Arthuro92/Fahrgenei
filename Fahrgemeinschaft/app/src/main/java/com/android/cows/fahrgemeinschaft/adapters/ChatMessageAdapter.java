package com.android.cows.fahrgemeinschaft.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.R;
import com.android.cows.fahrgemeinschaft.dataobjects.Chat;
import java.util.ArrayList;

/**
 * Created by david on 26.05.2016.
 */
public class ChatMessageAdapter extends ArrayAdapter {
    //new new new new
    private Context context = GlobalAppContext.getAppContext();
    private LayoutInflater layoutInflater = LayoutInflater.from(getContext());

    /**
     * Gets the User by accessing the shared preferences
     * @return user String
     */
    private String getChatUser() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "Blubb");
    }

    /**
     * Sets the view for one Listelement
     * @param chatMessageView a View to display the chatMessage
     * @param chatMessage a Chat object to be displayed
     * @return a view ready to be displayed
     */
    private View setChatMessageView(View chatMessageView, Chat chatMessage) {
        TextView chatMessageFrom = (TextView) chatMessageView.findViewById(R.id.chat_message_from);
        TextView chatMessageTime = (TextView) chatMessageView.findViewById(R.id.chat_message_time);
        TextView chatMessageText = (TextView) chatMessageView.findViewById(R.id.chat_message_text);
        chatMessageFrom.setText(chatMessage.getChatMessageFrom());
        chatMessageTime.setText(chatMessage.getChatMessageTime());
        chatMessageText.setText(chatMessage.getChatMessageText());
        return chatMessageView;
    }

    /**
     * Sets the LayoutInflater to the base View to display the chatMessage depending on the sender of the message
     * @param position an Integer referencing the position of the current Chat object in the ArrayList
     * @param convertView
     * @param parent
     * @return the fully set displayable view for one list element
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat chatMessage = (Chat) getItem(position);
        View chatMessageView;
        if(chatMessage.getChatMessageFrom().equals(getChatUser())) {
            chatMessageView = this.layoutInflater.inflate(R.layout.chat_message_layout_out, parent, false);
        } else {
            chatMessageView = this.layoutInflater.inflate(R.layout.chat_message_layout, parent, false);
        }
        return setChatMessageView(chatMessageView, chatMessage);
    }

    /**
     * Constructs a ChatMessageAdapter
     * @param context a Context the Adapter is constructed from
     * @param resource an ArrayList to be handled and displayed by the Adapter
     */
    public ChatMessageAdapter(Context context, ArrayList<Chat> resource) {
        super(context, R.layout.chat_message_layout, resource);
    }
}
