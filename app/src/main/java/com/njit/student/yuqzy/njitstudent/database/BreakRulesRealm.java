package com.njit.student.yuqzy.njitstudent.database;

import com.njit.student.yuqzy.njitstudent.model.DebtInfoItem;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

public class BreakRulesRealm extends RealmObject{
    private RealmList<DebtInfoItem> debtItems=null;
    private String personXH="";


    public RealmList<DebtInfoItem> getDebtItems() {
        return debtItems;
    }

    public void setDebtItems(RealmList<DebtInfoItem> debtItems) {
        this.debtItems = debtItems;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
