package com.njit.student.yuqzy.njitstudent.database;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/28.
 */

public class BookDetailRealm extends RealmObject {
    RealmList<BookItem> content;
    String personXH;
    String other;

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RealmList<BookItem> getContent() {
        return content;
    }

    public void setContent(RealmList<BookItem> content) {
        this.content = content;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
