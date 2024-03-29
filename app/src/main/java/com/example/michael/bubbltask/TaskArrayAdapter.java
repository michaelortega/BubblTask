package com.example.michael.bubbltask;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.example.michael.bubbltask.data.TaskModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TaskArrayAdapter extends ArrayAdapter<TaskModel> {
    private Context context;
    private int layoutResource;
    private List<TaskModel> list;

    public TaskArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<TaskModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.list_view_item_task, parent, false);

        TextView taskTextView = (TextView) view.findViewById(R.id.taskTextView);
        taskTextView.setText(list.get(position).getTaskName());

        TextView dueTextView = (TextView) view.findViewById(R.id.dueTextView);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    FloatingViewService.getInstance().decreaseSize();
                    new Delete().from(TaskModel.class).where("task_name = ?", list.get(position).getTaskName()).execute();
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getContext(), AlarmReceiver.class);
                    PendingIntent pIntent = PendingIntent.getBroadcast(getContext(), list.get(position).getAlarmID(), intent, PendingIntent.FLAG_ONE_SHOT);
                    alarmManager.cancel(pIntent);
                    list.remove(position);
                    notifyDataSetChanged();
                }

            }
        });


        // get string of current date
        Calendar currCal = Calendar.getInstance();
        Date currentDate = currCal.getTime();
        String currentDateStr = dateToString(currentDate);

        // get string of current date plus one day (Tomorrow)
        String currentTomStr = tomorrowString(currCal);


        long selectedTimeStamp = list.get(position).getTimeStamp();

        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTimeInMillis(selectedTimeStamp);

        Date selectedDateObj = selectedCalendar.getTime();
        String selectedDate = dateToString(selectedDateObj);


        String selectedTime = list.get(position).getTime();

        if (selectedDate.equals(currentDateStr)) {
            dueTextView.setText("Today  " + selectedTime);
        } else if (selectedDate.equals(currentTomStr)) {
            dueTextView.setText("Tomorrow  " + selectedTime);
        } else {
            dueTextView.setText(selectedDate + " " + selectedTime);
        }

        return view;
    }

    private String dateToString(Date date) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("M/d/yyyy");
        return outputFormat.format(date);
    }

    private String tomorrowString(Calendar currentDay) {
        Calendar tomorrow = currentDay;
        tomorrow.set(Calendar.DAY_OF_WEEK, currentDay.get(Calendar.DAY_OF_WEEK) + 1);
        Date tomorrowDateObj = tomorrow.getTime();
        return dateToString(tomorrowDateObj);
    }
}
