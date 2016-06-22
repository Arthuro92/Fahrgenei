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

    public User() {
    }

    public User(String id, String token, String name, String email) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.email = email;
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
