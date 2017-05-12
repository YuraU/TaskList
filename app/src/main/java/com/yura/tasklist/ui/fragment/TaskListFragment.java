package com.yura.tasklist.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.yura.tasklist.R;
import com.yura.tasklist.mvp.model.Task;
import com.yura.tasklist.adapter.TaskAdapter;
import com.yura.tasklist.databinding.FragmentTaskListBinding;
import com.yura.tasklist.mvp.presenters.TaskListPresenter;
import com.yura.tasklist.mvp.views.TaskListView;

import java.util.List;

public class TaskListFragment extends MvpAppCompatFragment implements TaskListView {

    public interface TaskListFragmentListener{
        void onAddTaskClick();
        void onTaskClick(Task task);
    }

    @InjectPresenter
    TaskListPresenter taskListPresenter;

    private TaskListFragmentListener listener;
    private FragmentTaskListBinding binder;
    private TaskAdapter taskAdapter;

    public static TaskListFragment getInstance(TaskListFragmentListener listener){
        TaskListFragment taskListFragment = new TaskListFragment();
        taskListFragment.listener = listener;

        return taskListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_list, container, false);

        View view = binder.getRoot();

        binder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskListPresenter.newTask();
            }
        });

        adapterSetup();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        taskListPresenter.getTasks();
    }

    private void adapterSetup() {
        if(taskAdapter == null) {
            taskAdapter = new TaskAdapter(getContext(), new TaskAdapter.TaskAdapterListener() {
                @Override
                public void onTaskClick(final Task task) {
                    taskListPresenter.editTask(task);
                }

                @Override
                public void onTaskDeleteClick(final Task task) {
                    taskListPresenter.deleteTask(getContext(), task);
                }
            });
        }

        binder.taskList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binder.taskList.setAdapter(taskAdapter);
    }

    @Override
    public void addTask() {
        if(listener != null)
            listener.onAddTaskClick();
    }

    @Override
    public void editTask(final Task task) {
        if(listener != null)
            listener.onTaskClick(task);
    }


    public void refreshTaskList(List<Task> tasks) {
        taskAdapter.setupItems(tasks);
    }


}
