package com.njit.student.yuqzy.njitstudent.Event;

/**
 * Created by liyu on 2016/11/29.
 */

public class FavorChangedEvent {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FavorChangedEvent(String path) {
        this.path=path;
    }
}
