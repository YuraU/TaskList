package com.yura.tasklist.mvp.presenters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.orm.SugarRecord;
import com.yura.tasklist.mvp.model.Task;
import com.yura.tasklist.UpdateReceiver;
import com.yura.tasklist.mvp.views.TaskListView;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class TaskListPresenter extends BasePresenter<TaskListView>{

    List<Task> taskList = new ArrayList<>();

    public void newTask(){
        getViewState().addTask();
    }

    public void editTask(final Task task){
        getViewState().editTask(task);
    }

    public void deleteTask(Context context, final Task task){
        if(task.getDate() != null){
            Intent startIntent = new Intent(context, UpdateReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) (long) task.getId(), startIntent,PendingIntent.FLAG_CANCEL_CURRENT );
            AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            int ALARM_TYPE = AlarmManager.RTC;

            alarmManager.setExact(ALARM_TYPE, task.getDate().getTimeInMillis(), pendingIntent);
        }

        SugarRecord.delete(task);
    }

    public void getTasks(){
        taskList = SugarRecord.listAll(Task.class);

        getViewState().refreshTaskList(taskList);
    }

}
