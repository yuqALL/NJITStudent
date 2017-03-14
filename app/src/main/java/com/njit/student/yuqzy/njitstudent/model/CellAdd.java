package com.njit.student.yuqzy.njitstudent.model;

import java.io.Serializable;
import java.util.List;


public class CellAdd implements Serializable{

    private List<Integer> week=null;
    private String courseName=null;//课程
    private String placeName=null;//地点
    private int length=0;//课程时长
    private int x,y;
    private String teacher=null;
    private String courseType=null;
    private String courseWeek;

    public List<Integer> getWeek() {
        return week;
    }

    public void setWeek(List<Integer> week) {
        this.week = week;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseWeek() {
        return courseWeek;
    }

    public void setCourseWeek(String courseWeek) {
        this.courseWeek = courseWeek;
    }

    public String getCourseChangeCode() {
        return courseChangeCode;
    }

    public void setCourseChangeCode(String courseChangeCode) {
        this.courseChangeCode = courseChangeCode;
    }

    private String courseChangeCode;
    String[] originText;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }



    public String[] getOriginText() {
        return originText;
    }

    public void setOriginText(String[] originText) {
        this.originText = originText;
    }
}
