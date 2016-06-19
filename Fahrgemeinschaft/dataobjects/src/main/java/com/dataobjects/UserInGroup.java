package com.dataobjects;

import com.google.gson.Gson;

/**
 * Created by Lennart on 17.06.2016.
 */
public class UserInGroup {
    private String uid;
    private String gid;
    private int isJoined;

    public UserInGroup(String uid, String gid, int isJoined) {
        this.uid = uid;
        this.gid = gid;
        this.isJoined = isJoined;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getIsJoined() {
        return isJoined;
    }

    public void setIsJoined(int isJoined) {
        this.isJoined = isJoined;
    }

    public String getJsonInString() {
        return new Gson().toJson(this);
    }
}
