package com.njit.student.yuqzy.njitstudent.database;

import com.njit.student.yuqzy.njitstudent.model.HotRecommendItem;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

public class HotRecRealm extends RealmObject {
    private RealmList<HotRecommendItem> hotItems=null;
    private String personXH="";

    public RealmList<HotRecommendItem> getHotItems() {
        return hotItems;
    }

    public void setHotItems(RealmList<HotRecommendItem> hotItems) {
        this.hotItems = hotItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
