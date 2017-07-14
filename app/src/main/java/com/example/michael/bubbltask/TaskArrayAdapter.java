package com.example.michael.bubbltask;


import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.michael.bubbltask.data.TaskModel;

import java.util.List;


public class TaskArrayAdapter extends ArrayAdapter<TaskModel> {
    private Context context;
    private int layoutResource;
    private List<TaskModel> list;

    public TaskArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull TaskModel[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layoutResource, parent, false);

        TextView taskTextView = (TextView) view.findViewById(R.id.taskTextView);
        TextView dueTextView = (TextView) view.findViewById(R.id.dueTextView);

        FloatingActionButton addTaskButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
}
