package com.njit.student.yuqzy.njitstudent.model;

/**
 * Created by yuqzy
 */

public class UpdateInfo {


    /**
     * app : 敏学网
     * versionCode :
     * versionName : 1.0
     * updateTime :
     * information :
     * foreUpdate : false
     * url :
     */

    private String app;
    private int versionCode;
    private String versionName;
    private String updateTime;
    private String information;
    private boolean foreUpdate;
    private String url;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public boolean isForeUpdate() {
        return foreUpdate;
    }

    public void setForeUpdate(boolean foreUpdate) {
        this.foreUpdate = foreUpdate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
