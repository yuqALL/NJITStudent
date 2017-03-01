package com.njit.student.yuqzy.njitstudent.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/6.
 */

public class Scores {
    private String XH;
    private String yearAterm;

    public String getYearAterm() {
        return yearAterm;
    }

    public void setYearAterm(String yearAterm) {
        this.yearAterm = yearAterm;
    }

    public String getXH() {
        return XH;
    }

    public void setXH(String XH) {
        this.XH = XH;
    }

    public Scores(String XH,String yearAterm, List<Map<String, String>> scores) {
        this.XH = XH;
        this.yearAterm=yearAterm;
        this.scores = scores;
    }

    public List<Map<String, String>> getScores() {
        return scores;
    }

    public void setScores(List<Map<String, String>> scores) {
        this.scores = scores;
    }

    private List<Map<String,String>> scores=new ArrayList<>();

}
