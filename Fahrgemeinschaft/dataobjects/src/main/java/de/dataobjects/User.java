package de.dataobjects;

import com.google.gson.Gson;

/**
 * Created by lenna on 23.05.2016.
 */
public class User {
    private String id;
    private String token;
    private String name;
    private String email;

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
