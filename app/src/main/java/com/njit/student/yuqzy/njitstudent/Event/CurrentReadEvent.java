package com.njit.student.yuqzy.njitstudent.Event;

import com.njit.student.yuqzy.njitstudent.model.CurrentReadItem;

import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 */

public class CurrentReadEvent {
    private List<CurrentReadItem> crItems = null;
    private String personXH = "";

    public CurrentReadEvent(List<CurrentReadItem> crItems, String personXH) {
        this.crItems = crItems;
        this.personXH = personXH;
    }

    public List<CurrentReadItem> getCrItems() {

        return crItems;
    }

    public void setCrItems(List<CurrentReadItem> crItems) {
        this.crItems = crItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
