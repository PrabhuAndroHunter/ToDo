package com.pub.todo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 31/1/18.
 */

public class TaskManager {
    private String date;
    private List<Task> taskList = new ArrayList <Task>();

    public TaskManager(String date, List <Task> taskList) {
        this.date = date;
        this.taskList = taskList;
    }

    public String getDate() {
        return date;
    }

    public List <Task> getTaskList() {
        return taskList;
    }
}
