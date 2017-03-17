package com.njit.student.yuqzy.njitstudent.Event;

import com.njit.student.yuqzy.njitstudent.model.DebtInfoItem;
import com.njit.student.yuqzy.njitstudent.model.HotRecommendItem;

import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 */

public class HotRecEvent {
    private List<HotRecommendItem> hotItems = null;
    private String personXH = "";

    public HotRecEvent(List<HotRecommendItem> hotItems, String personXH) {
        this.hotItems = hotItems;
        this.personXH = personXH;
    }

    public List<HotRecommendItem> getHotItems() {
        return hotItems;
    }

    public void setHotItems(List<HotRecommendItem> hotItems) {
        this.hotItems = hotItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
