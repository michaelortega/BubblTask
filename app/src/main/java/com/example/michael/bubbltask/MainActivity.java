package com.example.michael.bubbltask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

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

    private int alarmID;

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    public static int bubbleHeight = 240;
    public static int bubbleWidth = 240;
    public static boolean isAddingTask;


    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActiveAndroid.initialize(this);
        ButterKnife.bind(this);
        setmContext(this);
        fabListener();
        initSwipeCreator();
        displayTasks();



        SharedPreferences sharedPreferences = getSharedPreferences("ID",Context.MODE_PRIVATE);
        alarmID = sharedPreferences.getInt("alarm_id",0);
        if (alarmID == Integer.MAX_VALUE) {
                alarmID = 0;
            }

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
                        Log.i("id", "ALARM ID TO REMOVE"+task.alarmID);
                        Toast.makeText(MainActivity.this,"Task removed",Toast.LENGTH_SHORT).show();
                        deleteFromDB(task.getTaskName());
                        cancelAlarm(task.getAlarmID());
                }
                return false;
            }
        });


    }

    private void deleteFromDB(String taskName) {
        new Delete().from(TaskModel.class).where("task_name = ?", taskName).execute();
        displayTasks();
    }



    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    private void fabListener() {
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, 1);
                hideFAB();
                isAddingTask = true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();


            }

        } else {


            super.onActivityResult(requestCode, resultCode, data);
            showFAB();
            if (data != null) {
                Calendar calendar = (Calendar) data.getSerializableExtra("cal");
                calendar.set(Calendar.SECOND, 0);

                String taskName = data.getStringExtra("task");
                String date = data.getStringExtra("date");
                String time = AddTaskActivity.getCivilianTimeStr(data.getStringExtra("time"));

                Log.e("test", taskName + " " + date + " " + time);
                Log.i("test", String.valueOf(calendar.getTimeInMillis()));
                Log.i("test", "TIME  " + time);



                alarmID = ++alarmID;

                saveSettings();
                addToDB(taskName, date, time, calendar, alarmID);
                setAlarm(calendar.getTimeInMillis(), taskName, alarmID);
                Toast.makeText(MainActivity.this, "Task saved", Toast.LENGTH_LONG).show();

            }
        }
    }


    public void hideFAB() {
        addTaskButton.hide();
    }

    public void showFAB() {
        addTaskButton.show();
    }


    @Override
    protected void onResume() {
        super.onResume();


        if (FloatingViewService.getInstance() != null) {
            if (FloatingViewService.getInstance().getmFloatingView() != null) {
                FloatingViewService.getInstance().destroyBubble();
            }
        }


    }


    @Override
    protected void onPause() {

        //only show bubble when onPause is called
        if (!isAddingTask) {
            displayBubble();
        }
        isAddingTask = false;
        super.onPause();

    }

    private void setAlarm(long timeInMillis, String taskName, int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alertIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        alertIntent.putExtra("task", taskName);
        Log.i("id", "alarmID CREATED WITH ID:  " + id);
        alertIntent.putExtra("alarmID", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, alertIntent, PendingIntent.FLAG_ONE_SHOT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    private void addToDB(String taskName, String date, String time, Calendar calendar, int id) {
        TaskModel newTaskModel = new TaskModel();
        newTaskModel.date = date;
        newTaskModel.taskName = taskName;
        newTaskModel.time = time;
        newTaskModel.timeStamp = calendar.getTimeInMillis();
        newTaskModel.alarmID = id;
        newTaskModel.save();
        displayTasks();
    }


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

    private void displayBubble() {
        startService(new Intent(MainActivity.this, FloatingViewService.class));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
    }



    private void saveSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("ID",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("alarm_id", alarmID);
        editor.commit();



    }

    private void cancelAlarm(int alarmID) {




        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, alarmID, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.cancel(pIntent);
    }
}
