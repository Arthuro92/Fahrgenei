package com.example.dataobjects;

import com.example.repositories.keys.AppointmentId;
import com.google.gson.Gson;

import java.io.Serializable;

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
    private String abfahrzeit;

    @Column
    private String treffpunkt_zeit;

    @Column
    private String treffpunkt;

    @Column
    private String zielort;

    @Column
    private int freeSeats;

    @Column
    private int members;

    public Appointment(int aid, String gid, String name, String abfahrzeit, String treffpunkt_zeit, String treffpunkt, String zielort) {
        this.aid = aid;
        this.gid = gid;
        this.name = name;
        this.abfahrzeit = abfahrzeit;
        this.treffpunkt_zeit = treffpunkt_zeit;
        this.treffpunkt = treffpunkt;
        this.zielort = zielort;
    }

    public Appointment() {
    }

    public String getZielort() {
        return zielort;
    }

    public void setZielort(String zielort) {
        this.zielort = zielort;
    }

    public String getAbfahrzeit() {
        return abfahrzeit;
    }

    public void setAbfahrzeit(String abfahrzeit) {
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

    public String getTreffpunkt_zeit() {
        return treffpunkt_zeit;
    }

    public void setTreffpunkt_zeit(String treffpunkt_zeit) {
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

    public int getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(int freeSeats) {
        this.freeSeats = freeSeats;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getJsonInString() {
        return new Gson().toJson(this);
    }
}
