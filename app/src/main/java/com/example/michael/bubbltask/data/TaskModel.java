package com.example.michael.bubbltask.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;


@Table(name = "Tasks")
public class TaskModel extends Model{
    @Column(name = "task_name")
    private String taskName;
    @Column(name = "due")
    private int timeDue;

    //return a list of all the task obj in the db

    public String getTaskName() {
        return taskName;
    }

    public int getTimeDue() {
        return timeDue;
    }

    public static List<TaskModel>getAllTasks(){
        return new Select().from(TaskModel.class).execute();
    }

}
