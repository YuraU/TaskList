package com.yura.tasklist;

import android.support.multidex.MultiDexApplication;

import com.orm.SugarContext;

public class TaskApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        SugarContext.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        SugarContext.terminate();
    }
}
