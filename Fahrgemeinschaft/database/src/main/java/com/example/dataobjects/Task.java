package com.example.dataobjects;

import com.google.gson.Gson;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by lenna on 23.05.2016.
 */
@Entity
public class Task {

    @Id
    private String aufgabenId;

    @Column
    private String aufgabenName;

    @Column
    private String aufgabenBeschreibung;

    @Column
    private String bearbeiter;

    public Task() {
    }

    public Task(String aufgabenId, String aufgabenName, String aufgabenBeschreibung, String bearbeiter) {
        this.aufgabenId = aufgabenId;
        this.aufgabenName = aufgabenName;
        this.aufgabenBeschreibung = aufgabenBeschreibung;
        this.bearbeiter = bearbeiter;
    }

    public String getAufgabenName() {
        return aufgabenName;
    }

    public void setAufgabenName(String aufgabenName) {
        this.aufgabenName = aufgabenName;
    }

    public String getAufgabenId() {
        return aufgabenId;
    }

    public void setAufgabenId(String aufgabenId) {
        this.aufgabenId = aufgabenId;
    }

    public String getAufgabenBeschreibung() {
        return aufgabenBeschreibung;
    }

    public void setAufgabenBeschreibung(String aufgabenBeschreibung) {
        this.aufgabenBeschreibung = aufgabenBeschreibung;
    }

    public String getBearbeiter() {
        return bearbeiter;
    }

    public void setBearbeiter(String bearbeiter) {
        this.bearbeiter = bearbeiter;
    }

    public String getJsonInString() {
        return new Gson().toJson(this);
    }
}