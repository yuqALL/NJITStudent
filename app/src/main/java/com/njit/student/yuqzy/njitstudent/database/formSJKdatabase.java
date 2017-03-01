package com.njit.student.yuqzy.njitstudent.database;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/10.
 */

public class formSJKdatabase extends RealmObject{
    private String courseName;//课程名称
    private String teacher;//教师
    private String credit;//学分
    private String time;//起止周
    private String studyTime;//上课时间
    private String studyPlace;//上课地点

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

    public String getStudyPlace() {
        return studyPlace;
    }

    public void setStudyPlace(String studyPlace) {
        this.studyPlace = studyPlace;
    }
}
