package com.example.michael.bubbltask;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.activeandroid.ActiveAndroid;

import com.activeandroid.query.Delete;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.michael.bubbltask.data.TaskModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private List<TaskModel> taskList;

    @BindView(R.id.floatingActionButton)
    public FloatingActionButton addTaskButton;

    public SwipeMenuListView listView;

    private Context mContext;
    private FragmentTransaction fragmentTransaction;

    private FragmentManager fragmentManager;
    private View context;
    private int id = 0;


    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActiveAndroid.initialize(this);
        ButterKnife.bind(this);
        setmContext(this);
        fabListener();
        initSwipeCreator();
        displayTasks();


    }

    private void initSwipeCreator() {
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Edit");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_white_18dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                TaskModel task = taskList.get(position);
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        //delete
                        Log.e("e", "TO DELETE:: " + task.taskName);
                        deleteFromDB(task.getTaskName());
                }
                return false;
            }
        });


    }

    private void deleteFromDB(String taskName) {
        new Delete().from(TaskModel.class).where("task_name = ?", taskName).execute();
        displayTasks();
    }


    /////////
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    private void fabListener() {
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //changeFragment();
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, 1);
                hideFAB();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showFAB();
        if (data != null) {
            String taskName = data.getStringExtra("task");
            String date = data.getStringExtra("date");
            String time = data.getStringExtra("time");
            Log.e("test", taskName + " " + date + " " + time);
            Calendar calendar = (Calendar) data.getSerializableExtra("cal");
            calendar.set(Calendar.SECOND,0);
            Log.i("test", String.valueOf(calendar.getTimeInMillis()));
            addToDB(taskName, date, time);
            setAlarm(calendar.getTimeInMillis(),taskName);
        }
    }


    public void hideFAB() {
        addTaskButton.hide();
    }

    public void showFAB() {
        addTaskButton.show();
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(MainActivity.this,"MAIN",Toast.LENGTH_LONG).show();
//    }


    private void setAlarm(long timeInMillis, String taskName) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alertIntent = new Intent(MainActivity.this,AlarmReceiver.class);
        alertIntent.putExtra("task", taskName);

        id = ++id + 4;
        alertIntent.putExtra("id", id);

        PendingIntent pendingIntent =  PendingIntent.getBroadcast(this,id,alertIntent,PendingIntent.FLAG_ONE_SHOT);

       alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis,pendingIntent);
    }

    private void addToDB(String taskName, String date, String time) {
        TaskModel newTaskModel = new TaskModel();
        newTaskModel.date = date;
        newTaskModel.taskName = taskName;
        newTaskModel.time = time;
        newTaskModel.save();
        displayTasks();
    }


//    @Override
//    public void passTask(String taskName, String date, String time, Calendar calendar) {
//        Toast.makeText(MainActivity.this, (taskName + time + date), Toast.LENGTH_LONG).show();
//        addToDB(taskName, date, time);
//        setAlarm(); // // TODO: 7/16/2017
//    }


    public void displayTasks() {
        taskList = new ArrayList<>();
        List<TaskModel> modelList = TaskModel.getAllTasks();
        Log.e("e", "SIZE OF MODEL LIST :  " + modelList.size());
        for (TaskModel task : modelList) {
            taskList.add(task);
        }
        Log.e("e", "SIZE OF TASK LIST :  " + taskList.size());
        TaskArrayAdapter adapter = new TaskArrayAdapter(this, R.layout.list_view_item_task, taskList);
        listView.setAdapter(adapter);


    }

}
