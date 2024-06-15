 package com.example.whattodo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattodo.AddNewTask;
import com.example.whattodo.MainActivity;
import com.example.whattodo.R;
import com.example.whattodo.databasehandler.DatabaseHandler;
import com.example.whattodo.model.TodoModel;

import java.util.List;

public  class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<TodoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public TodoAdapter(DatabaseHandler db, MainActivity activity)
    {
        this.db = db;
        this.activity = activity;
    }

    private Boolean toBool(int n)
    {
        return n!=0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTask(List<TodoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return todoList.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout,parent,false);
        return new ViewHolder(itemView);
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();

        final TodoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBool(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    db.updateStatus(item.getId(),1);
                } else{
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }

    public void deleteItem(int position){
        TodoModel item= todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    public void editItem(int position){
        TodoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
    }

    public Context getContext() {
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
            CheckBox task;
            ViewHolder(View view){
                super(view);
                task = view.findViewById(R.id.ToDoCheckBox);
            }
        }
}
