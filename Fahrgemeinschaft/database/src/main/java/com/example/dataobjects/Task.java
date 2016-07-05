package com.example.dataobjects;

import com.example.repositories.keys.TaskId;
import com.google.gson.Gson;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * Created by lenna on 23.05.2016.
 */
@Entity
@IdClass(TaskId.class)
public class Task implements Serializable {

    @Id
    private String taskId;

    @Id
    private int aid;
    
    @Id
    private String gid;

    @Column
    private String taskName;

    @Column
    private String taskdescription;

    @Column
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (aid != task.aid) return false;
        if (!taskId.equals(task.taskId)) return false;
        if (!gid.equals(task.gid)) return false;
        if (!taskName.equals(task.taskName)) return false;
        if (!taskdescription.equals(task.taskdescription)) return false;
        return responsible.equals(task.responsible);

    }

    @Override
    public int hashCode() {
        int result = taskId.hashCode();
        result = 31 * result + aid;
        result = 31 * result + gid.hashCode();
        result = 31 * result + taskName.hashCode();
        result = 31 * result + taskdescription.hashCode();
        result = 31 * result + responsible.hashCode();
        return result;
    }
}
