package com.njit.student.yuqzy.njitstudent.Event;

import com.njit.student.yuqzy.njitstudent.model.DebtInfoItem;
import com.njit.student.yuqzy.njitstudent.model.SearchHistItem;

import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 */

public class SearchHistEvent {
    private List<SearchHistItem> searchItems = null;
    private String personXH = "";

    public SearchHistEvent(List<SearchHistItem> searchItems, String personXH) {
        this.searchItems = searchItems;
        this.personXH = personXH;
    }

    public List<SearchHistItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(List<SearchHistItem> searchItems) {
        this.searchItems = searchItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
