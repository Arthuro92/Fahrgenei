package de.dataobjects;

import com.google.gson.Gson;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Arthur on 25.05.2016.
 */
public class Appointment implements Serializable, Comparable {

    private int aid;

    private String gid;

    private String name;

    private String abfahrzeit;

    private String treffpunkt_zeit;

    private String treffpunkt;

    private String zielort;

    private int freeSeats;

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

    public String getJsonInString() {
        return new Gson().toJson(this);
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

    @Override
    public int compareTo(Object o) {
        if(o instanceof Appointment) {
            Appointment appointment = (Appointment) o;
            DateFormat format = new SimpleDateFormat("d/m/yyyy-HH-mm", Locale.ENGLISH);
            try {
                Date date1 = format.parse(this.getTreffpunkt_zeit());
                Date date2 = format.parse(appointment.getTreffpunkt_zeit());
                return date1.compareTo(date2);
            } catch(ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
