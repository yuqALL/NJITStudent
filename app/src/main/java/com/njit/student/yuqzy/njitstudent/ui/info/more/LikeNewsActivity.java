package com.njit.student.yuqzy.njitstudent.ui.info.more;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.LikeNews;
import com.njit.student.yuqzy.njitstudent.ui.adapter.NormalAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;

public class LikeNewsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_like_news);
        toolbar=(Toolbar)findViewById(R.id.title);
        setSupportActionBar(toolbar);
        toolbar.setTitle("我的收藏");
        setDisplayHomeAsUpEnabled(true);

        recyclerView=(RecyclerView) findViewById(R.id.like_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LikeNews likeNews=(LikeNews) getIntent().getSerializableExtra("data");
        NormalAdapter adapter=new NormalAdapter(this,likeNews.getList());
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
