package com.example.michael.bubbltask;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.example.michael.bubbltask.data.TaskModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private List<TaskModel> taskList;

//    @BindView(R.id.floatingActionButton)
//    private FloatingActionButton addTaskButton;

    @BindView(R.id.listView)
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActiveAndroid.initialize(this);
        ButterKnife.bind(this);
    }
}
