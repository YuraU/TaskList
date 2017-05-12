package com.yura.tasklist.mvp.views;

import com.arellomobile.mvp.MvpView;

public interface TaskEditView extends MvpView {

    void closeTask();

    void showTaskTime();

    void showTaskPosition();
}
