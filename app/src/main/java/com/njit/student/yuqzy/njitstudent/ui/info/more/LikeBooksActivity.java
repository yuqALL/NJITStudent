package com.njit.student.yuqzy.njitstudent.ui.info.more;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.BookDetailRealm;
import com.njit.student.yuqzy.njitstudent.database.NewsDetail;
import com.njit.student.yuqzy.njitstudent.model.LikeNews;
import com.njit.student.yuqzy.njitstudent.model.NormalItem;
import com.njit.student.yuqzy.njitstudent.ui.adapter.BookDetailAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.LikeBooksAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.NormalAdapter;
import com.njit.student.yuqzy.njitstudent.ui.info.library.BookDetailActivity;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class LikeBooksActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Realm  realm;
    private RealmQuery<BookDetailRealm> query;
    private RealmResults<BookDetailRealm> results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_like_news);
        toolbar=(Toolbar)findViewById(R.id.title);
        setSupportActionBar(toolbar);
        toolbar.setTitle("我的收藏");
        setDisplayHomeAsUpEnabled(true);

        recyclerView=(RecyclerView) findViewById(R.id.like_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<BookDetailRealm> list=getBookRealm();
        LikeBooksAdapter adapter=new LikeBooksAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }

    protected void setDisplayHomeAsUpEnabled(boolean enable) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<BookDetailRealm> getBookRealm(){
        query = realm.where(BookDetailRealm.class);
        results = query.equalTo("personXH",SettingsUtil.getXueHao()).findAll();
        List<BookDetailRealm> list=new ArrayList<>();
        for(BookDetailRealm item:results){
            list.add(item);
        }
        return list;
    }

    private void initTheme() {
        int themeIndex = SettingsUtil.getTheme();
        switch (themeIndex) {
            case 0:
                setTheme(R.style.LapisBlueTheme);
                break;
            case 1:
                setTheme(R.style.PaleDogwoodTheme);
                break;
            case 2:
                setTheme(R.style.GreeneryTheme);
                break;
            case 3:
                setTheme(R.style.PrimroseYellowTheme);
                break;
            case 4:
                setTheme(R.style.FlameTheme);
                break;
            case 5:
                setTheme(R.style.IslandParadiseTheme);
                break;
            case 6:
                setTheme(R.style.KaleTheme);
                break;
            case 7:
                setTheme(R.style.PinkYarrowTheme);
                break;
            case 8:
                setTheme(R.style.NiagaraTheme);
                break;

        }
    }
}
