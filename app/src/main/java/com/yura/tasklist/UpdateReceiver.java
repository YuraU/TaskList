package com.yura.tasklist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orm.SugarRecord;
import com.yura.tasklist.mvp.model.Task;
import com.yura.tasklist.ui.activity.MainActivity;

public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        long taskId = intent.getLongExtra("taskId", -1);

        if(taskId != -1){
            Task task = SugarRecord.findById(Task.class, taskId);
            if(task != null){
                Intent i = new Intent(context, MainActivity.class);
                final PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), i, 0);

                Notification n=new Notification.Builder(context)
                        .setContentTitle("Task list")
                        .setContentText(task.getMsg())
                        .setContentIntent(null)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .build();

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, n);

                SugarRecord.delete(task);
            }
        }
    }

}