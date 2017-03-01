package com.njit.student.yuqzy.njitstudent.database;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/9.
 */

public class PersonScore extends RealmObject {
    RealmList<ScoreData> scoreDatas;
    private String personXH;
    private String yearAterm;

    public String getYearAterm() {
        return yearAterm;
    }

    public void setYearAterm(String yearAterm) {
        this.yearAterm = yearAterm;
    }

    public RealmList<ScoreData> getScoreDatas() {
        return scoreDatas;
    }

    public void setScoreDatas(RealmList<ScoreData> scoreDatas) {
        this.scoreDatas = scoreDatas;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
