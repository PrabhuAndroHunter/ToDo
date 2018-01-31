package com.pub.todo.model;

/**
 * Created by prabhu on 29/1/18.
 */

public class Task {
    private String title, description, date;
    private int taskStatus, id;

    public static final int TASK_COMPLETED = 1;
    public static final int TASK_IN_COMPLETED = 0;

    public Task(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.taskStatus = TASK_IN_COMPLETED;
    }

    public Task(int id, String title, String description, String date, int taskStatus) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.taskStatus = taskStatus    ;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public boolean isTaskCompleted() {
        if (this.taskStatus == 1)
            return true;
        else
            return false;
    }
}
