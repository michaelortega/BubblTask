package com.example.michael.bubbltask;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;

/**
 * Created by Michael on 7/14/2017.
 */

public class AddTaskFragment extends Fragment {
    @BindView(R.id.submitTaskButton)
    Button submitTaskButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        initListener();
        return inflater.inflate(R.layout.add_task_fragment,container,false);
    }



    private void initListener() {
        submitTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle taskBundle = new Bundle();
                
            }
        });
    }


}
