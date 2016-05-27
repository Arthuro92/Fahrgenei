package com.android.cows.fahrgemeinschaft.dataobjects;

import java.util.Date;

/**
 * Created by Arthur on 25.05.2016.
 */
public class Appointment2 {

    private String tid;
    private String name;
    private Date abfahrzeit;
    private Date treffpunkt_zeit;
    private String treffpunkt;

    public Appointment2(String tid, String name, Date abfahrzeit, Date treffpunkt_zeit, String treffpunkt) {
        this.tid = tid;
        this.name = name;
        this.abfahrzeit = abfahrzeit;
        this.treffpunkt_zeit = treffpunkt_zeit;
        this.treffpunkt = treffpunkt;
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
