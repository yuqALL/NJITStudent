package com.njit.student.yuqzy.njitstudent.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;


/**
 * Created by Administrator on 2017/2/10.
 */

public class CourseDatabase extends RealmObject implements Serializable,Parcelable{
    public static final Parcelable.Creator<CourseDatabase> CREATOR=null;
    private String personXH;
    private String yearAterm;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYearAterm() {
        return yearAterm;
    }

    public void setYearAterm(String yearAterm) {
        this.yearAterm = yearAterm;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }

    private RealmList<formKBdatabase> formKBdatabases;
    private RealmList<formSJKdatabase> formSJKdatabases;
    private RealmList<formTTBinfoDatabase> formTTBinfoDatabases;

    public RealmList<formKBdatabase> getFormKBdatabases() {
        return formKBdatabases;
    }

    public void setFormKBdatabases(RealmList<formKBdatabase> formKBdatabases) {
        this.formKBdatabases = formKBdatabases;
    }

    public RealmList<formSJKdatabase> getFormSJKdatabases() {
        return formSJKdatabases;
    }

    public void setFormSJKdatabases(RealmList<formSJKdatabase> formSJKdatabases) {
        this.formSJKdatabases = formSJKdatabases;
    }

    public RealmList<formTTBinfoDatabase> getFormTTBinfoDatabases() {
        return formTTBinfoDatabases;
    }

    public void setFormTTBinfoDatabases(RealmList<formTTBinfoDatabase> formTTBinfoDatabases) {
        this.formTTBinfoDatabases = formTTBinfoDatabases;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
