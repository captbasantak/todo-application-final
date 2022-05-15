package com.example.finaltodoapplicaction.Model;

public class ToDoModel {
    private int id, status;
    private String tasks;
    private String dateTask;

    public int getId()
        {
             return id;
        }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getDateTask() {
        return dateTask;
    }

    public void setDateTask(String dateTask) {
        this.dateTask = dateTask;
    }

}
