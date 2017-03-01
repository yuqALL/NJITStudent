package com.njit.student.yuqzy.njitstudent.database;

import com.njit.student.yuqzy.njitstudent.model.BookShelfBean;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/3/1.
 */

public class BookShelfRealm extends RealmObject {

    private RealmList<BookShelfBean> content;
    private String personXH;

    public RealmList<BookShelfBean> getContent() {
        return content;
    }

    public void setContent(RealmList<BookShelfBean> content) {
        this.content = content;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
