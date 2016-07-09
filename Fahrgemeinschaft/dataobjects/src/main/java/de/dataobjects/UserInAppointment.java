package de.dataobjects;

    import com.google.gson.Gson;

import java.io.Serializable;


/**
 * Created by Lennart on 17.06.2016.
 */

public class UserInAppointment implements Serializable {
    private int aid;

    private String gid;

    private String uid;

    private int isParticipant;

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
