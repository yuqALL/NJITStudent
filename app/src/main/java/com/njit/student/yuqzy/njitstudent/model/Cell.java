package com.njit.student.yuqzy.njitstudent.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouchaoyuan on 2017/1/14.
 */

public class Cell implements Serializable {

    private List<List<Integer>> week = null;
    private List<String> courseName = null;//课程
    private List<String> placeName = null;//地点
    private int length = 0;//课程时长
    private int x, y;
    private List<String> teacher = null;
    private List<String> courseType = null;
    private List<String> courseWeek;

    public List<String> getCourseChangeCode() {
        return courseChangeCode;
    }

    public void setCourseChangeCode(List<String> courseChangeCode) {
        this.courseChangeCode = courseChangeCode;
    }

    private List<String> courseChangeCode;
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


    public List<List<Integer>> getWeek() {
        return week;
    }

    public void setWeek(List<List<Integer>> week) {
        this.week = week;
    }

    public List<String> getCourseName() {
        return courseName;
    }

    public void setCourseName(List<String> courseName) {
        this.courseName = courseName;
    }

    public List<String> getPlaceName() {
        return placeName;
    }

    public void setPlaceName(List<String> placeName) {
        this.placeName = placeName;
    }

    public List<String> getTeacher() {
        return teacher;
    }

    public void setTeacher(List<String> teacher) {
        this.teacher = teacher;
    }

    public List<String> getCourseType() {
        return courseType;
    }

    public void setCourseType(List<String> courseType) {
        this.courseType = courseType;
    }

    public List<String> getCourseWeek() {
        return courseWeek;
    }

    public void setCourseWeek(List<String> courseWeek) {
        this.courseWeek = courseWeek;
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
