package com.example.repositories.keys;

import java.io.Serializable;

/**
 * Created by Lennart on 30.06.2016.
 */
public class TaskId implements Serializable {

    private String taskId;

    private int aid;

    private String gid;

    public TaskId() {
    }

    public TaskId(String taskId, int aid, String gid) {
        this.taskId = taskId;
        this.aid = aid;
        this.gid = gid;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskId taskId1 = (TaskId) o;

        if (aid != taskId1.aid) return false;
        if (!taskId.equals(taskId1.taskId)) return false;
        return gid.equals(taskId1.gid);
    }

    @Override
    public int hashCode() {
        int result = taskId.hashCode();
        result = 31 * result + aid;
        result = 31 * result + gid.hashCode();
        return result;
    }
}
