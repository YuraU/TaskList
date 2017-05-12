package com.yura.tasklist.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.yura.tasklist.R;
import com.yura.tasklist.mvp.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.DateAdapterViewHolder>{

    public interface TaskAdapterListener{

        void onTaskClick(Task task);
        void onTaskDeleteClick(Task task);

    }

    private static Context context;
    private List<Task> mTasks = new ArrayList<>();
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    private TaskAdapterListener listener;

    public TaskAdapter(Context context, TaskAdapterListener listener) {
        this.context = context;
        this.listener = listener;
    }


    @Override
    public DateAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new DateAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DateAdapterViewHolder holder, int position) {
        viewBinderHelper.bind(holder.swipeView, String.valueOf(mTasks.get(position).hashCode()));
        holder.bind(mTasks.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mTasks == null ? 0 : mTasks.size();
    }

    public void addItem(Task item) {
        this.mTasks.add(item);
        notifyDataSetChanged();
    }

    public void setupItems(List<Task> tasks) {
        this.mTasks.clear();
        this.mTasks.addAll(tasks);
        notifyDataSetChanged();
    }

    public void clear(){
        this.mTasks.clear();
        notifyDataSetChanged();
    }

    public List<Task> getDates() {
        return mTasks;
    }

    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates(inState);
    }

    public class DateAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public SwipeRevealLayout swipeView;
        public View view_bg;
        public View view_fg;

        public DateAdapterViewHolder(View view){
            super(view);
            this.swipeView = (SwipeRevealLayout)view.findViewById(R.id.swipe_layout);
            this.view_bg = view.findViewById(R.id.rl_bg);
            this.view_fg = view.findViewById(R.id.rl_fg);
            this.name = (TextView) view.findViewById(R.id.tvName);
        }

        public void bind(final Task task, final int position) {
            view_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTasks.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    if(listener != null)
                        listener.onTaskDeleteClick(task);
                }
            });

            view_fg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                        listener.onTaskClick(task);
                }
            });

            name.setText(task.getMsg());
        }
    }
}