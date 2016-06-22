package com.example.dataobjects;

import com.example.repositories.keys.AppointmentId;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * Created by Arthur on 25.05.2016.
 */
@Entity
@IdClass(AppointmentId.class)
public class Appointment implements Serializable {

    @Id
    @Column
    private int aid;

    @Id
    @Column
    private String gid;

    @Column
    private String name;

    @Column
    private Date abfahrzeit;

    @Column
    private Date treffpunkt_zeit;

    @Column
    private String treffpunkt;

    @Column
    private String zielort;

    @Column
    private int isParticipant;

    public Appointment(int aid, String gid, String name, Date abfahrzeit, Date treffpunkt_zeit, String treffpunkt, String zielort, int isParticipant) {
        this.aid = aid;
        this.gid = gid;
        this.name = name;
        this.abfahrzeit = abfahrzeit;
        this.treffpunkt_zeit = treffpunkt_zeit;
        this.treffpunkt = treffpunkt;
        this.zielort = zielort;
        this.isParticipant = isParticipant;
    }


    public String getZielort() {
        return zielort;
    }

    public void setZielort(String zielort) {
        this.zielort = zielort;
    }

    public int getIsParticipant() {
        return isParticipant;
    }

    public void setIsParticipant(int isParticipant) {
        this.isParticipant = isParticipant;
    }

    public Date getAbfahrzeit() {
        return abfahrzeit;
    }

    public void setAbfahrzeit(Date abfahrzeit) {
        this.abfahrzeit = abfahrzeit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public Date getTreffpunkt_zeit() {
        return treffpunkt_zeit;
    }

    public void setTreffpunkt_zeit(Date treffpunkt_zeit) {
        this.treffpunkt_zeit = treffpunkt_zeit;
    }

    public String getTreffpunkt() {
        return treffpunkt;
    }

    public void setTreffpunkt(String treffpunkt) {
        this.treffpunkt = treffpunkt;
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
