package de.dataobjects;

import com.google.gson.Gson;


/**
 * Created by Lennart on 09.05.2016.
 */

public class Groups {

    private String gid;

    private String name;

    private int membercount;

    private String adminid;

    private String adminname;

    public Groups() {
    }

    public Groups(String name, int membercount, String adminid, String adminname, String gid) {
        this.name = name;
        this.membercount = membercount;
        this.adminid = adminid;
        this.adminname = adminname;
        this.gid = gid.replaceAll("\\s","");
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

    public int getMembercount() {
        return membercount;
    }

    public void setMembercount(int membercount) {
        this.membercount = membercount;
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
