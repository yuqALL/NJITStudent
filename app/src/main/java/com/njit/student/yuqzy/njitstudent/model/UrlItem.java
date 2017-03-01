package com.njit.student.yuqzy.njitstudent.model;


import java.io.Serializable;

public class UrlItem implements Serializable{
    private int type;
    private String title;
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UrlItem(int type, String name, String url) {
        this.type = type;
        this.name = name;
        this.url = url;
    }

    public UrlItem(int type, String title) {

        this.type = type;
        this.title = title;
    }

    public int getType() {

        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
