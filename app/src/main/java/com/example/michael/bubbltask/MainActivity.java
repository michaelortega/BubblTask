package com.example.michael.bubbltask;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.michael.bubbltask.data.TaskModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnDataPass {
    private List<TaskModel> taskList;

    @BindView(R.id.floatingActionButton)
    public FloatingActionButton addTaskButton;

    @BindView(R.id.listView)
    public ListView listView;

    private FragmentTransaction fragmentTransaction;

    private FragmentManager fragmentManager;


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
                addTaskButton.setVisibility(View.GONE);
            }
        });
    }

    private void changeFragment() {
        Log.i("TEST","add task fragment");
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.c, new AddTaskFragment());
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }



    public void hideFAB(){
        addTaskButton.hide();
    }

    public void showFAB(){
        addTaskButton.show();
    }

//    @Override
//    public void onBackPressed() {
//        int fragments = getFragmentManager().getBackStackEntryCount();
//        if (fragments == 1) {
//            fragmentTransaction.remove().commit();
//        }
//        super.onBackPressed();
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(MainActivity.this,"MAIN",Toast.LENGTH_LONG).show();
    }

    @Override
    public void passTask(String taskName, String date, String time) {
        Log.e("e",taskName + time + date);
    }


}
