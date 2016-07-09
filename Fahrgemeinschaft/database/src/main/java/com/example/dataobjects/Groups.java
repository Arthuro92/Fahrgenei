package com.example.dataobjects;

import com.google.gson.Gson;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Lennart on 09.05.2016.
 */
@Entity
public class Groups {

    @Id
    private String gid;

    @Column
    private String name;

    @Column
    private String adminid;

    @Column
    private String adminname;

    @Column
    private String substitute;

    public Groups() {
    }

    public Groups(String name, String adminid, String adminname, String gid) {
        this.name = name;
        this.adminid = adminid;
        this.adminname = adminname;
        this.gid = gid.replaceAll("\\s","");
    }

    public String getSubstitute() {
        return substitute;
    }

    public void setSubstitute(String substitute) {
        this.substitute = substitute;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid = gid.replaceAll("\\s","");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getJsonInString() {
        return new Gson().toJson(this);
    }
}
