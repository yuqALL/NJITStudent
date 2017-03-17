package com.njit.student.yuqzy.njitstudent.database;

import com.njit.student.yuqzy.njitstudent.model.CurrentReadItem;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

public class CurrentReadRealm extends RealmObject {
    private RealmList<CurrentReadItem> crItems = null;
    private String personXH = "";


    public RealmList<CurrentReadItem> getCrItems() {

        return crItems;
    }

    public void setCrItems(RealmList<CurrentReadItem> crItems) {
        this.crItems = crItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
