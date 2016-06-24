package com.example.repositories.keys;

import java.io.Serializable;

/**
 * Created by Lennart on 22.06.2016.
 */
public class UserInAppointmentId implements Serializable {

    private int aid;
    private String gid;
    private String uid;

    public UserInAppointmentId() {
    }

    public UserInAppointmentId(String gid, int aid, String uid) {
        this.gid = gid;
        this.aid = aid;
        this.uid = uid;
    }

    public String getGid() {
        return gid;
    }

    public int getAid() {
        return aid;
    }

    public String getUid() {
        return uid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInAppointmentId that = (UserInAppointmentId) o;

        if (aid != that.aid) return false;
        if (!gid.equals(that.gid)) return false;
        return uid.equals(that.uid);

    }

    @Override
    public int hashCode() {
        int result = aid;
        result = 31 * result + gid.hashCode();
        result = 31 * result + uid.hashCode();
        return result;
    }
}
