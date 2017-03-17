package com.njit.student.yuqzy.njitstudent.Event;

import com.njit.student.yuqzy.njitstudent.model.BookShelfItem;
import com.njit.student.yuqzy.njitstudent.model.DebtInfoItem;

import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 */

public class BookShelfEvent {
    private List<List<BookShelfItem>> bookList = null;
    private List<String> shelfName;
    private List<String> shelfUrl;
    private String personXH = "";

    public List<List<BookShelfItem>> getBookList() {
        return bookList;
    }

    public void setBookList(List<List<BookShelfItem>> bookList) {
        this.bookList = bookList;
    }

    public List<String> getShelfName() {
        return shelfName;
    }

    public void setShelfName(List<String> shelfName) {
        this.shelfName = shelfName;
    }

    public List<String> getShelfUrl() {
        return shelfUrl;
    }

    public void setShelfUrl(List<String> shelfUrl) {
        this.shelfUrl = shelfUrl;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }
}
