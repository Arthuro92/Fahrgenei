package com.example.repositories.keys;

import java.io.Serializable;

/**
 * Created by Lennart on 22.06.2016.
 * Key class for UserInGroupid
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInGroupId that = (UserInGroupId) o;

        if (!uid.equals(that.uid)) return false;
        return gid.equals(that.gid);

    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + gid.hashCode();
        return result;
    }
}
