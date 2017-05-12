package com.yura.tasklist.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.yura.tasklist.mvp.model.Task;

import java.util.List;

public interface TaskListView extends MvpView{

        void addTask();

        void editTask(final Task task);

        void refreshTaskList(List<Task> tasks);

    }
