package com.njit.student.yuqzy.njitstudent.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */

public class UrlAll implements Serializable{
    List<UrlItem> urlItems;

    public List<UrlItem> getUrlItems() {
        return urlItems;
    }

    public void setUrlItems(List<UrlItem> urlItems) {
        this.urlItems = urlItems;
    }
}
