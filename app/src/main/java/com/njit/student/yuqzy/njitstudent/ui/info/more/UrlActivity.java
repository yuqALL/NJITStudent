package com.njit.student.yuqzy.njitstudent.ui.info.more;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.njit.student.yuqzy.njitstudent.Event.BreakRulesEvent;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.BookDetailRealm;
import com.njit.student.yuqzy.njitstudent.database.BreakRulesRealm;
import com.njit.student.yuqzy.njitstudent.database.Url;
import com.njit.student.yuqzy.njitstudent.model.UrlAll;
import com.njit.student.yuqzy.njitstudent.model.UrlAllEvent;
import com.njit.student.yuqzy.njitstudent.model.UrlItem;
import com.njit.student.yuqzy.njitstudent.net.NetWork;
import com.njit.student.yuqzy.njitstudent.ui.adapter.LikeBooksAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.UrlAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.njit.student.yuqzy.njitstudent.utils.ShowLoadDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class UrlActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView img_refresh;
    private ListView listView;
    private String title;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        initTheme();

        setContentView(R.layout.activity_urls);
        EventBus.getDefault().register(this);
        realm.getDefaultInstance();
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        toolbar = (Toolbar) findViewById(R.id.title);
        img_refresh=(ImageView)findViewById(R.id.img_refresh);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.url_list);
        initView();
    }

    private void initView() {
        if (title.equals("校园服务")) {
            String[] name = getResources().getStringArray(R.array.service_page_name);
            String[] value = new String[]{
                    "http://xhbbs.njit.edu.cn/bbs/portal.php", "http://202.119.160.238/Repair/", "http://zxxt.njit.edu.cn/",
                    "http://jwc.njit.edu.cn/xl_list.jsp?urltype=tree.TreeTempUrl&wbtreeid=1129", "http://202.119.160.168/Query/Query.jsp",
                    "http://www.njit.edu.cn/ggfw/bccx.htm", "http://map.njit.edu.cn/#", "http://tv.njit.edu.cn/", "http://210.29.166.210/",
                    "http://al.njit.edu.cn/login.aspx", "http://ecard.njit.edu.cn/", "http://net.njit.edu.cn:8080/Self/idstarlogin"
            };
            UrlAllEvent all = new UrlAllEvent();
            List<UrlItem> listUrl = new ArrayList<>();
            for (int i = 0; i < name.length; i++) {
                UrlItem item = new UrlItem();
                item.setType(1);
                item.setName(name[i]);
                item.setUrl(value[i]);
                listUrl.add(item);
            }
            all.setUrlItems(listUrl);

            UrlAdapter adapter = new UrlAdapter(this, all.getUrlItems());
            listView.setAdapter(adapter);
        } else if (title.equals("快速链接")) {
            img_refresh.setVisibility(View.VISIBLE);
            img_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowLoadDialog.show(UrlActivity.this);
                    NetWork.SchoolLink();
                }
            });
            if(!MoreInfoFragment.parseUrl){
                ShowLoadDialog.dismiss();
                if(getUrlRealm()!=null) {
                    UrlAdapter adapter = new UrlAdapter(this, getUrlRealm().getUrlItems());
                    listView.setAdapter(adapter);
                }
            }else {
                ShowLoadDialog.show(this);
            }
        }
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

    private UrlAllEvent getUrlRealm() {
        realm=Realm.getDefaultInstance();
        RealmQuery<UrlAll> query = realm.where(UrlAll.class);
        RealmResults<UrlAll> results = query
                .findAll();
        if (results.size() > 0) {
            UrlAll value = results.first();
            UrlAllEvent event = new UrlAllEvent();
            List<UrlItem> list=new ArrayList<>();
            for(UrlItem item:value.getUrlItems()){
                list.add(item);
            }
            event.setUrlItems(list);
            return event;
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UrlAllEvent event) {
        ShowLoadDialog.dismiss();
        //存储到数据库
        if(title.equals("快速链接")){
            UrlAdapter adapter = new UrlAdapter(this, event.getUrlItems());
            listView.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
