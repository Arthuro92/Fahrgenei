package com.example.repositories.keys;

import java.io.Serializable;

/**
 * Created by Lennart on 22.06.2016.
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
}
