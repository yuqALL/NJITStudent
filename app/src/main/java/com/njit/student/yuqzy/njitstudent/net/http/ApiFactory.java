package com.njit.student.yuqzy.njitstudent.net.http;

import com.njit.student.yuqzy.njitstudent.net.http.api.AppController;

public class ApiFactory {
    protected static final Object monitor = new Object();

    private static AppController appController;


    public static AppController getAppController() {
        if (appController == null) {
            synchronized (monitor) {
                appController = RetrofitManager.getInstance().create(AppController.class);
            }
        }
        return appController;
    }
}
