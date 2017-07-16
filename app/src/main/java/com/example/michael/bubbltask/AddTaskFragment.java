package com.example.michael.bubbltask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Michael on 7/14/2017.
 */

public class AddTaskFragment extends Fragment {
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

    private OnDataPass dataPasser;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity){
            activity = (Activity) context;
            dataPasser = (OnDataPass) activity;
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_task_fragment, container, false);
        initComponents();
        initListeners();
        return view;
    }

    private void initComponents() {
        taskTextView = view.findViewById(R.id.taskNameTextView);
        submitTaskButton = view.findViewById(R.id.submitTaskButton);
        dateTextView = view.findViewById(R.id.dateTextView);
        timeDueTextView = view.findViewById(R.id.timeDueTextView);
        ((MainActivity) getActivity()).hideFAB();
        calToPass = Calendar.getInstance();

    }

    private void initListeners() {
        submitTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = taskTextView.getText().toString();
                String dateDue = dateTextView.getText().toString();
                String timeDue = timeDueTextView.getText().toString();
                if (taskName.matches("")) {
                    Toast.makeText(getActivity(), "Please enter a task name.", Toast.LENGTH_LONG).show();
                } else if (dateDue.matches("")) {
                    Toast.makeText(getActivity(), "Please enter a date.", Toast.LENGTH_LONG).show();
                } else if (timeDue.matches("")) {
                    Toast.makeText(getActivity(), "Please enter a time.", Toast.LENGTH_LONG).show();
                } else {

//                    //send to activity
//                    Bundle taskBundle = new Bundle();
//                    taskBundle.putString("task", taskName);
//                    taskBundle.putString("date", date);
//                    taskBundle.putString("time", time);
                   // Toast.makeText(getActivity(), "Task saved", Toast.LENGTH_LONG).show();
                    getFragmentManager().popBackStack();


                    passData(taskName,date,time,calToPass);
                    ((MainActivity)getActivity()).showFAB();
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


                DatePickerDialog dateDialog = new DatePickerDialog(getActivity(),
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), timeDialogListener, hour, min, false);
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

                SimpleDateFormat inputFormat = new SimpleDateFormat("HH:m");
                SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm aa");
                Date time;
                try {
                    time = inputFormat.parse(timeString);
                    timeDueTextView.setText(outputFormat.format(time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };

    }

   public void passData(String taskName, String date, String time, Calendar c){
       dataPasser.passTask(taskName,date,time,c);
       Log.e("e",String.valueOf(c.getTimeInMillis()));
   }

}



