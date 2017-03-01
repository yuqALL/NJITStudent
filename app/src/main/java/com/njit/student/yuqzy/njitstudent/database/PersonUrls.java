package com.njit.student.yuqzy.njitstudent.database;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/9.
 */

public class PersonUrls extends RealmObject {
    RealmList<Url> urls;
    private String personXH;

    public RealmList<Url> getUrls() {
        return urls;
    }

    public void setUrls(RealmList<Url> urls) {
        this.urls = urls;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
