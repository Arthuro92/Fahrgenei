package com.android.cows.fahrgemeinschaft;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.cows.fahrgemeinschaft.dataobjects.Chat;
import java.util.ArrayList;

/**
 * Created by david on 26.05.2016.
 */
public class ChatMessageAdapter extends ArrayAdapter{
    private Chat chat;
    private LayoutInflater lain = LayoutInflater.from(getContext());
    private View cmv;

    private View setChatMessageView(View cmv) {
        TextView cmf = (TextView) cmv.findViewById(R.id.chat_message_from);
        TextView cmti = (TextView) cmv.findViewById(R.id.chat_message_time);
        TextView cmte = (TextView) cmv.findViewById(R.id.chat_message_text);
        cmf.setText(chat.getChatMessageFrom());
        cmti.setText(chat.getChatMessageTime());
        cmte.setText(chat.getChatMessageText());
        return cmv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.chat = (Chat) getItem(position);
        //todo user email or
        if(this.chat.getChatMessageFrom().equals("Don")) {
            cmv= lain.inflate(R.layout.chat_message_layout_out, parent, false);
        } else {
            cmv= lain.inflate(R.layout.chat_message_layout, parent, false);
        }
        return setChatMessageView(cmv);
    }

    public ChatMessageAdapter(Context context, ArrayList<Chat> resource) {
        super(context, R.layout.chat_message_layout, resource);
    }
}
