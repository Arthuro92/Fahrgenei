package com.example.repositories.keys;

import java.io.Serializable;

/**
 * Created by Lennart on 22.06.2016.
 * Key Class for Appointments
 */
public class AppointmentId implements Serializable {

    private String gid;
    private int aid;

    public AppointmentId() {
    }

    public AppointmentId(String gid, int aid) {
        this.gid = gid;
        this.aid = aid;
    }

    public String getGid() {
        return gid;
    }

    public int getAid() {
        return aid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppointmentId that = (AppointmentId) o;

        if (aid != that.aid) return false;
        return gid.equals(that.gid);

    }

    @Override
    public int hashCode() {
        int result = gid.hashCode();
        result = 31 * result + aid;
        return result;
    }
}
