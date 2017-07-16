package com.example.michael.bubbltask.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;


@Table(name = "Tasks")
public class TaskModel extends Model{
    @Column(name = "task_name")
    public String taskName;
    @Column(name = "date")
    public String date;
    @Column(name = "time")
    public String time;

    //return a list of all the task obj in the db


    public static List<TaskModel>getAllTasks(){
        return new Select().from(TaskModel.class).execute();
    }
//    public static TaskModel getTaskName(){
//        return new Select().from(TaskModel.class).orderBy("RANDOM()").executeSingle();
//    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TaskModel() {
        super();
    }
}
