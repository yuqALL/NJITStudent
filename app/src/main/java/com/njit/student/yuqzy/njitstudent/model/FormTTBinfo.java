package com.njit.student.yuqzy.njitstudent.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/10.
 * 调、停、补信息
 */

public class FormTTBinfo implements Serializable{
    private String id;//编号
    private String courseName;//课程名称
    private String preStudyState;//原上课时间地点
    private String nowStudyState;//现上课时间地点
    private String postTime;//申请时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getPreStudyState() {
        return preStudyState;
    }

    public void setPreStudyState(String preStudyState) {
        this.preStudyState = preStudyState;
    }

    public String getNowStudyState() {
        return nowStudyState;
    }

    public void setNowStudyState(String nowStudyState) {
        this.nowStudyState = nowStudyState;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
}
