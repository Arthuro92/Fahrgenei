package de.dataobjects;

import com.google.gson.Gson;

/**
 * Created by lenna on 23.05.2016.
 */
public class Task {

    private String taskId;

    private int aid;

    private String gid;

    private String taskName;

    private String taskdescription;

    private String responsible;

    public Task() {
    }

    public Task(String taskId, int aid, String gid, String taskName, String taskdescription, String responsible) {
        this.taskId = taskId;
        this.aid = aid;
        this.gid = gid;
        this.taskName = taskName;
        this.taskdescription = taskdescription;
        this.responsible = responsible;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskdescription() {
        return taskdescription;
    }

    public void setTaskdescription(String taskdescription) {
        this.taskdescription = taskdescription;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getJsonInString() {
        return new Gson().toJson(this);
    }
}
