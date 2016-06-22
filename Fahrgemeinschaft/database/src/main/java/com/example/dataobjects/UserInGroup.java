package com.example.dataobjects;

import com.example.repositories.keys.UserInGroupId;
import com.google.gson.Gson;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * Created by Lennart on 17.06.2016.
 */
@Entity
@IdClass(UserInGroupId.class)
public class UserInGroup implements Serializable {

    @Id
    private String uid;

    @Id
    private String gid;

    @Column
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
