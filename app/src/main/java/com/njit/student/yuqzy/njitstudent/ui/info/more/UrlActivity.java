package com.njit.student.yuqzy.njitstudent.ui.info.more;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.BookDetailRealm;
import com.njit.student.yuqzy.njitstudent.database.Url;
import com.njit.student.yuqzy.njitstudent.model.UrlAll;
import com.njit.student.yuqzy.njitstudent.ui.adapter.LikeBooksAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.UrlAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class UrlActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();

        setContentView(R.layout.activity_urls);
        toolbar=(Toolbar)findViewById(R.id.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("校园服务");
        setDisplayHomeAsUpEnabled(true);

        listView=(ListView) findViewById(R.id.url_list);
        UrlAll urlAll=(UrlAll) getIntent().getSerializableExtra("data");
        UrlAdapter adapter=new UrlAdapter(this,urlAll.getUrlItems());
        listView.setAdapter(adapter);
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
