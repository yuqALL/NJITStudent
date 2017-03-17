package com.njit.student.yuqzy.njitstudent.Event;

import com.njit.student.yuqzy.njitstudent.model.FormKB;
import com.njit.student.yuqzy.njitstudent.model.FormSJK;
import com.njit.student.yuqzy.njitstudent.model.FormTTBinfo;

import java.util.List;

/**
 * Created by Administrator on 2017/2/10.
 */

public class CourseEvent {
    String personXH;
    String yearAterm;
    String type = "class";

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

    List<FormKB> formKBs;
    List<FormSJK> formSJKs;
    List<FormTTBinfo> formTTBinfos;

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }

    public CourseEvent(String personXH, String yearAterm, String type, List<FormKB> formKBs, List<FormSJK> formSJKs, List<FormTTBinfo> formTTBinfos) {
        this.personXH = personXH;
        this.yearAterm = yearAterm;
        this.type = type;
        this.formKBs = formKBs;
        this.formSJKs = formSJKs;
        this.formTTBinfos = formTTBinfos;
    }

    public List<FormKB> getFormKBs() {

        return formKBs;
    }

    public void setFormKBs(List<FormKB> formKBs) {
        this.formKBs = formKBs;
    }

    public List<FormSJK> getFormSJKs() {
        return formSJKs;
    }

    public void setFormSJKs(List<FormSJK> formSJKs) {
        this.formSJKs = formSJKs;
    }

    public List<FormTTBinfo> getFormTTBinfos() {
        return formTTBinfos;
    }

    public void setFormTTBinfos(List<FormTTBinfo> formTTBinfos) {
        this.formTTBinfos = formTTBinfos;
    }
}
