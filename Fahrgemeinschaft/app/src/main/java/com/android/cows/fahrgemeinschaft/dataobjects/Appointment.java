package com.android.cows.fahrgemeinschaft.dataobjects;

import java.util.Date;

/**
 * Created by Arthur on 25.05.2016.
 */
public class Appointment {

    private String tid;
    private String name;
    private Date abfahrzeit;
    private Date treffpunkt_zeit;
    private String treffpunkt;
    private String zielort;

    public String getZielort() {
        return zielort;
    }

    public void setZielort(String zielort) {
        this.zielort = zielort;
    }

    public Appointment(String tid, String name, Date abfahrzeit, Date treffpunkt_zeit, String treffpunkt, String zielort) {
        this.tid = tid;
        this.name = name;
        this.abfahrzeit = abfahrzeit;
        this.treffpunkt_zeit = treffpunkt_zeit;
        this.treffpunkt = treffpunkt;
        this. zielort = zielort;
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

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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
}
