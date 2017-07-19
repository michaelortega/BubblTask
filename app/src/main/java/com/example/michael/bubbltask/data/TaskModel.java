package com.example.michael.bubbltask.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;


@Table(name = "Tasks")
public class TaskModel extends Model {
    @Column(name = "task_name")
    public String taskName;
    @Column(name = "date")
    public String date;
    @Column(name = "time")
    public String time;


    @Column(name = "stamp")
    public long timeStamp;

    @Column(name = "alarm_id")
    public int alarmID;

    public int getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(int alarmID) {
        this.alarmID = alarmID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    //return a list of all the task obj in the db ordered by timeStamp
    public static List<TaskModel> getAllTasks() {
        return new Select()
                .from(TaskModel.class)
                .orderBy("stamp ASC")
                .execute();
    }



    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

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

    public String getTaskName() {
        return taskName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
