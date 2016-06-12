package com.android.cows.fahrgemeinschaft.dataobjects;

import java.util.List;

/**
 * Created by Lennart on 09.05.2016.
 */
public class Group {

    private String name;
    private int membercount;
    private String adminid;
    private String adminname;
    private List<User> userlist;
    private String gid;
    private int isJoined;

    public Group(String name, int membercount, String adminid, String adminname, String gid, int isJoined) {
        this.name = name;
        this.membercount = membercount;
        this.adminid = adminid;
        this.adminname = adminname;
        this.gid = gid;
        this.isJoined = isJoined;
    }

    public int getisJoined() {
        return isJoined;
    }

    public void setJoined(int joined) {
        isJoined = joined;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
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

    public List<User> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<User> userlist) {
        this.userlist = userlist;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }
}
