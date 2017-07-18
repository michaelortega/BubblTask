package com.example.michael.bubbltask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Michael on 7/17/2017.
 */

public class AddTaskActivity extends Activity {
    private View view;

    private TextView taskTextView;
    private TextView dateTextView;
    private TextView timeDueTextView;
    private FloatingActionButton fab;

    private Button submitTaskButton;

    private TimePickerDialog.OnTimeSetListener timeDialogListener;
    private DatePickerDialog.OnDateSetListener dateDialogListener;
    private Calendar calendar;
    private Calendar calToPass;


    private String date;
    private String time;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);

        initComponents();
        initListeners();

    }

    private void initComponents() {
        taskTextView = findViewById(R.id.taskNameTextView);
        submitTaskButton = findViewById(R.id.submitTaskButton);
        dateTextView = findViewById(R.id.dateTextView);
        timeDueTextView =findViewById(R.id.timeDueTextView);
        calToPass = Calendar.getInstance();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initListeners() {
        submitTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = taskTextView.getText().toString();
                String dateDue = dateTextView.getText().toString();
                String timeDue = timeDueTextView.getText().toString();
                if (taskName.matches("")) {
                    Toast.makeText(AddTaskActivity.this, "Please enter a task name.", Toast.LENGTH_LONG).show();
                } else if (dateDue.matches("")) {
                    Toast.makeText(AddTaskActivity.this, "Please enter a date.", Toast.LENGTH_LONG).show();
                } else if (timeDue.matches("")) {
                    Toast.makeText(AddTaskActivity.this, "Please enter a time.", Toast.LENGTH_LONG).show();
                } else {

                    Intent intent = new Intent();
                    intent.putExtra("task",taskName);
                    intent.putExtra("date",date); // switch this to decide formatting on date 10/10/10 or dateOfWeek , month date(dueDate)
                    intent.putExtra("time",time);
                    intent.putExtra("cal", calToPass);
                    setResult(RESULT_OK,intent);
                    finish();





//                    ((MainActivity)getActivity()).showFAB();
                }
            }
        });


        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);


                DatePickerDialog dateDialog = new DatePickerDialog(AddTaskActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        dateDialogListener,
                        year, month, day);
                dateDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                dateDialog.show();
            }
        });

        dateDialogListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int newMonth, int day) {

                int month = newMonth + 1; //Month is zero based
                calToPass.set(Calendar.YEAR,year);
                calToPass.set(Calendar.MONTH,newMonth);
                calToPass.set(Calendar.DAY_OF_MONTH,day);
                SimpleDateFormat inputFormat = new SimpleDateFormat("MM/d/yyyy");
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");

                String dateStr = String.valueOf(month + "/" + day + "/" + year);
                date = dateStr;

                Date date;
                try {
                    date = inputFormat.parse(dateStr);
                    String dayName = dateFormat.format(date);
                    String monthName = monthFormat.format(date);
                    dateTextView.setText(dayName + ", " + monthName + " " + day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        };

        timeDueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                //24 hour time - true
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this, timeDialogListener, hour, min, false);
                timePickerDialog.show();
            }
        });

        timeDialogListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                //24 hour time
                String timeString = hour + ":" + min;
                time = timeString;

                calToPass.set(Calendar.HOUR_OF_DAY,hour);
                calToPass.set(Calendar.MINUTE,min);

                timeDueTextView.setText(getCivilianTimeStr(timeString));

            }
        };

    }

    public static String  getCivilianTimeStr(String militaryTimeStr){
        String result ="";
        Date date;
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm aa");
        try {
            date = inputFormat.parse(militaryTimeStr);
            result = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
            return result;
    }




}
