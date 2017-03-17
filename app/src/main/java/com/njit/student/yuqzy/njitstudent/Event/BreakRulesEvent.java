package com.njit.student.yuqzy.njitstudent.Event;

import com.njit.student.yuqzy.njitstudent.model.DebtInfoItem;
import com.njit.student.yuqzy.njitstudent.model.OrderBookItem;

import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 */

public class BreakRulesEvent {
    private List<DebtInfoItem> debtItems = null;
    private String personXH = "";

    public BreakRulesEvent(List<DebtInfoItem> debtItems, String personXH) {
        this.debtItems = debtItems;
        this.personXH = personXH;
    }

    public List<DebtInfoItem> getDebtItems() {
        return debtItems;
    }

    public void setDebtItems(List<DebtInfoItem> debtItems) {
        this.debtItems = debtItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
