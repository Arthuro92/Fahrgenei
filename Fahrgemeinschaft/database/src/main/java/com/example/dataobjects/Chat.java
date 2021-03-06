package com.example.dataobjects;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by david on 26.05.2016.
 */
public class Chat implements Serializable{
    private String chatMessageFrom;
    private String chatMessageTime;
    private String chatMessageText;
    private String gid;

    public Chat() {
    }

    public Chat(String chatMessageFrom, String chatMessageTime, String chatMessageText, String gid) {
        this.chatMessageFrom = chatMessageFrom;
        this.chatMessageTime = chatMessageTime;
        this.chatMessageText = chatMessageText;
        this.gid = gid;
    }

    public String getChatMessageFrom() {
        return chatMessageFrom;
    }

    public String getChatMessageTime() {
        return chatMessageTime;
    }

    public String getChatMessageText() {
        return chatMessageText;
    }

    public void setChatMessageFrom(String chatMessageFrom) {
        this.chatMessageFrom = chatMessageFrom;
    }

    public void setChatMessageTime(String chatMessageTime) {
        this.chatMessageTime = chatMessageTime;
    }

    public void setChatMessageText(String chatMessageText) {
        this.chatMessageText = chatMessageText;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getJsonInString() {
        return new Gson().toJson(this);
    }
}
