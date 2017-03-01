package com.njit.student.yuqzy.njitstudent.database;

import com.njit.student.yuqzy.njitstudent.model.OrderBookItem;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

public class OrderBookRealm extends RealmObject{
    private RealmList<OrderBookItem> orderItems=null;
    private String personXH="";


    public RealmList<OrderBookItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(RealmList<OrderBookItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
