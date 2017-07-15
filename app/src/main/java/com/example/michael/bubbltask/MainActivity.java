package com.example.michael.bubbltask;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.example.michael.bubbltask.data.TaskModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private List<TaskModel> taskList;

    @BindView(R.id.floatingActionButton)
    public FloatingActionButton addTaskButton;

    @BindView(R.id.listView)
    public ListView listView;

    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActiveAndroid.initialize(this);
        ButterKnife.bind(this);
        fabListener();

    }

    private void fabListener() {
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment();
            }
        });
    }

    private void changeFragment() {
        Log.i("TEST","add task fragment");
        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.c, new AddTaskFragment());
        fragmentTransaction.addToBackStack("addFrag");
        fragmentTransaction.commit();
    }


//    @Override
//    public void onBackPressed() {
//        int fragments = getFragmentManager().getBackStackEntryCount();
//        if (fragments == 1) {
//            fragmentTransaction.remove().commit();
//        }
//        super.onBackPressed();
//    }
}
