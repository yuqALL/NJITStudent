package com.njit.student.yuqzy.njitstudent.Event;

import com.njit.student.yuqzy.njitstudent.model.CurrentReadItem;
import com.njit.student.yuqzy.njitstudent.model.PreReadItem;

import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 */

public class PreReadEvent {
    private List<PreReadItem> preItems = null;
    private String personXH = "";

    public PreReadEvent(List<PreReadItem> preItems, String personXH) {
        this.preItems = preItems;
        this.personXH = personXH;
    }

    public List<PreReadItem> getPreItems() {
        return preItems;
    }

    public void setPreItems(List<PreReadItem> preItems) {
        this.preItems = preItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
