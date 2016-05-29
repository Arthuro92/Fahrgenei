package com.android.cows.fahrgemeinschaft;

import android.content.Context;
import android.content.SharedPreferences;
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
    private Context con = GlobalAppContext.getAppContext();
    private LayoutInflater lain = LayoutInflater.from(getContext());
    private Chat chat;
    private View cmv;

    /**
     * Gets the User by accessing the shared preferences
     * @return user String
     */
    private String getChatUser() {
        SharedPreferences sp = this.con.getSharedPreferences("com.android.cows.fahrgemeinschaft", Context.MODE_PRIVATE);
        return sp.getString("username", "Blubb");
    }


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
        if(this.chat.getChatMessageFrom().equals(getChatUser())) {
            this.cmv= lain.inflate(R.layout.chat_message_layout_out, parent, false);
        } else {
            this.cmv= lain.inflate(R.layout.chat_message_layout, parent, false);
        }
        return setChatMessageView(this.cmv);
    }

    public ChatMessageAdapter(Context context, ArrayList<Chat> resource) {
        super(context, R.layout.chat_message_layout, resource);
    }
}
