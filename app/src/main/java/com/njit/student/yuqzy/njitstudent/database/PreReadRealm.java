package com.njit.student.yuqzy.njitstudent.database;

import com.njit.student.yuqzy.njitstudent.model.PreReadItem;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

public class PreReadRealm extends RealmObject {
    private RealmList<PreReadItem> preItems = null;
    private String personXH = "";

    public RealmList<PreReadItem> getPreItems() {
        return preItems;
    }

    public void setPreItems(RealmList<PreReadItem> preItems) {
        this.preItems = preItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
