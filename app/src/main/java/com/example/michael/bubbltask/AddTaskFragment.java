package com.example.michael.bubbltask;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
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

    private Button submitTaskButton;

    private DatePickerDialog.OnDateSetListener dateDialogListener;
    private Calendar calendar;

    private int month;
    private int day;
    private int year;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_task_fragment, container, false);
        initComponents();
        initListeners();
        return view;
    }

    private void initComponents() {
        taskTextView = (TextView) view.findViewById(R.id.taskNameTextView);
        submitTaskButton = (Button) view.findViewById(R.id.submitTaskButton);
        dateTextView = view.findViewById(R.id.dateTextView);
        timeDueTextView = view.findViewById(R.id.timeDueTextView);
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
                    Bundle taskBundle = new Bundle();
                    taskBundle.putString("task", taskName);
                    taskBundle.putString("date", dateDue);
                    taskBundle.putString("time", timeDue);
                    getFragmentManager().popBackStack();
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

                dateDialog.show();
            }
        });

        dateDialogListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int newYear, int newMonth, int newDay) {
                year = newYear;
                day = newDay;
                month = newMonth + 1; //Month is 0 based

                SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy");
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");

                String dateStr = String.valueOf(month + "/" + day + "/" + year);

                Date date;
                try {
                    date = sdf.parse(dateStr);
                    String dayName = dateFormat.format(date);
                    String monthName = monthFormat.format(date);
                    dateTextView.setText(dayName + ", " + monthName + " " + day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        };
    }
}



