package com.yura.tasklist.mvp.presenters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.orm.SugarRecord;
import com.yura.tasklist.mvp.model.Task;
import com.yura.tasklist.UpdateReceiver;
import com.yura.tasklist.mvp.views.TaskEditView;

@InjectViewState
public class TaskEditPresenter extends BasePresenter<TaskEditView> {

    Task curTask;

    public void saveTask(Task task, Context context){

        SugarRecord.save(task);

        if(task.getDate() != null){
            setAlarm(context, task);
        }

        getViewState().closeTask();
    }

    public void onTimeRadioBtnClick(){
        getViewState().showTaskTime();
    }

    public void onMapRadioBtnClick(){
        getViewState().showTaskPosition();
    }

    private static void setAlarm(Context context, Task task) {

        Intent startIntent = new Intent(context, UpdateReceiver.class);
        startIntent.putExtra("taskId", task.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) (long) task.getId(), startIntent,PendingIntent.FLAG_UPDATE_CURRENT );
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        int ALARM_TYPE = AlarmManager.RTC;

        alarmManager.setExact(ALARM_TYPE, task.getDate().getTimeInMillis(), pendingIntent);
    }

}
