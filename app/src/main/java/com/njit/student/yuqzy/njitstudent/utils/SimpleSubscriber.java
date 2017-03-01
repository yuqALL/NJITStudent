package com.njit.student.yuqzy.njitstudent.utils;

import rx.Subscriber;

public abstract class SimpleSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }
}
