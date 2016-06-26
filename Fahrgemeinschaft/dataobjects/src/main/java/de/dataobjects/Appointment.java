package de.dataobjects;


import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Arthur on 25.05.2016.
 */
public class Appointment {

    private int aid;
    private String gid;
    private String name;
    private String abfahrzeit;
    private String treffpunkt_zeit;
    private String treffpunkt;
    private String zielort;
    private int isParticipant;

    public String getZielort() {
        return zielort;
    }

    public void setZielort(String zielort) {
        this.zielort = zielort;
    }

    public Appointment(int aid, String gid, String name, String abfahrzeit, String treffpunkt_zeit, String treffpunkt, String zielort, int isParticipant) {
        this.aid = aid;
        this.gid = gid;
        this.name = name;
        this.abfahrzeit = abfahrzeit;
        this.treffpunkt_zeit = treffpunkt_zeit;
        this.treffpunkt = treffpunkt;
        this.zielort = zielort;
        this.isParticipant = isParticipant;
    }

    public Appointment() {
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

    public String getJsonInString() {
        return new Gson().toJson(this);
    }
}
