package com.example.dataobjects;

import com.google.gson.Gson;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lenna on 23.05.2016.
 */

@Entity
@Table(name="User")
public class User {

    @Id
    private String id;

    @Column
    private String token;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private boolean driver = false;

    @Column
    private int freeSeats = 0;

    public User() {
    }

    public User(String id, String token, String name, String email, boolean driver, int freeSeats) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.email = email;
        this.driver = driver;
        this.freeSeats = freeSeats;
    }

    public boolean isDriver() {
        return driver;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }

    public int getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(int freeSeats) {
        this.freeSeats = freeSeats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJsonInString() {
        return new Gson().toJson(this);
    }
}
