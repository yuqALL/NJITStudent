package com.njit.student.yuqzy.njitstudent.model;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/3/1.
 */

public class UrlAll extends RealmObject{
    RealmList<UrlItem> urlItems;

    public RealmList<UrlItem> getUrlItems() {
        return urlItems;
    }

    public void setUrlItems(RealmList<UrlItem> urlItems) {
        this.urlItems = urlItems;
    }
}
