package com.njit.student.yuqzy.njitstudent.Event;

import com.njit.student.yuqzy.njitstudent.model.OrderBookItem;
import com.njit.student.yuqzy.njitstudent.model.PreReadItem;

import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 */

public class OrderBookEvent {
    private List<OrderBookItem> orderItems=null;
    private String personXH="";

    public OrderBookEvent(List<OrderBookItem> orderItems, String personXH) {
        this.orderItems = orderItems;
        this.personXH = personXH;
    }

    public List<OrderBookItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderBookItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
