package com.njit.student.yuqzy.njitstudent.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.njit.student.yuqzy.njitstudent.database.ScoreData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */

public class ScoreList {
    private List<ScoreData> dataList;
    private String personXH;
    private String yearAterm;

    public ScoreList(List<ScoreData> dataList, String personXH, String yearAterm) {
        this.dataList = dataList;
        this.personXH = personXH;
        this.yearAterm = yearAterm;
    }

    public void setDataList(List<ScoreData> dataList) {

        this.dataList = dataList;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }

    public String getYearAterm() {
        return yearAterm;
    }

    public void setYearAterm(String yearAterm) {
        this.yearAterm = yearAterm;
    }

    public List<ScoreData> getDataList() {

        return dataList;
    }

}
