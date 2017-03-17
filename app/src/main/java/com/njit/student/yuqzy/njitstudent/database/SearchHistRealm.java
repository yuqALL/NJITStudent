package com.njit.student.yuqzy.njitstudent.database;

import com.njit.student.yuqzy.njitstudent.model.SearchHistItem;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

public class SearchHistRealm extends RealmObject {
    private RealmList<SearchHistItem> searchItems = null;
    private String personXH = "";

    public RealmList<SearchHistItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(RealmList<SearchHistItem> searchItems) {
        this.searchItems = searchItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
