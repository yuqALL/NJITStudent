package com.njit.student.yuqzy.njitstudent;

import android.app.Application;
import android.content.Context;


public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        if (!BuildConfig.DEBUG) {
            AppExceptionHandler.getInstance().setCrashHanler(this);
        }

    }

    public static Context getContext() {
        return mContext;
    }

}
