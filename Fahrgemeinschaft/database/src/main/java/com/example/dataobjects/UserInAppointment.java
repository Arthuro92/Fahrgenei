package com.example.dataobjects;

import com.example.repositories.keys.UserInAppointmentId;
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
@IdClass(UserInAppointmentId.class)
public class UserInAppointment implements Serializable {
    @Id
    private int aid;

    @Id
    private String gid;

    @Id
    private String uid;

    @Column
    private int isParticipant;

    @Column
    boolean isDriver = false;

    public UserInAppointment() {
    }

    public UserInAppointment(int aid, String gid, String uid, int isParticipant) {
        this.aid = aid;
        this.gid = gid;
        this.uid = uid;
        this.isParticipant = isParticipant;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIsParticipant() {
        return isParticipant;
    }

    public void setIsParticipant(int isParticipant) {
        this.isParticipant = isParticipant;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(boolean driver) {
        isDriver = driver;
    }

    public String getJsonInString() {
        return new Gson().toJson(this);
    }

}
