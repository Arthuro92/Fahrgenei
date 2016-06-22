package com.example.repositories.keys;

import java.io.Serializable;

/**
 * Created by Lennart on 22.06.2016.
 */
public class UserInGroupId implements Serializable {

    private String uid;
    private String gid;

    public UserInGroupId() {
    }

    public UserInGroupId(String uid, String gid) {
        this.uid = uid;
        this.gid = gid;
    }

    public String getUid() {
        return uid;
    }

    public String getGid() {
        return gid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }
}
