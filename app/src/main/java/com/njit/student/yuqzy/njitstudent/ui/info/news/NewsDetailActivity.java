package com.njit.student.yuqzy.njitstudent.ui.info.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.NewsDetail;
import com.njit.student.yuqzy.njitstudent.database.NewsDetailItem;
import com.njit.student.yuqzy.njitstudent.ui.adapter.NewsDetailAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.njit.student.yuqzy.njitstudent.utils.ShareUtils;
import com.njit.student.yuqzy.njitstudent.utils.WebUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.njit.student.yuqzy.njitstudent.AppGlobal.USERAGENT_DESKTOP;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private Subscription subscription;
    private NewsDetailAdapter adapter;
    private ImageView imgLike, imgIE, imgShare;
    private Realm realm;
    private RealmQuery<NewsDetail> query;
    private RealmResults<NewsDetail> results;
    private String host;
    private String title;
    private String updateTime;
    private List<NewsDetailItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_news_detail);
        toolbar = (Toolbar) findViewById(R.id.title);
        setSupportActionBar(toolbar);
        setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.lv_news_info);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        imgLike = (ImageView) findViewById(R.id.img_like);
        imgIE = (ImageView) findViewById(R.id.img_open_ie);
        imgShare = (ImageView) findViewById(R.id.img_share);
        imgLike.setOnClickListener(this);
        imgIE.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        Intent intent = getIntent();
        host = intent.getStringExtra("host");
        title = intent.getStringExtra("title");
        updateTime = intent.getStringExtra("time");
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        query = realm.where(NewsDetail.class);
        results = query.equalTo("host", host).findAll();
        if (results.size() > 0) {
            imgLike.setImageResource(R.drawable.ic_like2);
            NewsDetail newsDetail = results.first();
            list = new ArrayList<>();
            for (int i = 0; i < newsDetail.getValue().size(); i++) {
                list.add(newsDetail.getValue().get(i));
            }
            adapter = new NewsDetailAdapter(NewsDetailActivity.this, list);
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        } else {
            parseNews(host);
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

    private void parseNews(String url) {
        if (url.contains("download.jsp?")) {
            WebUtils.openExternal(this, url);
            return;
        }
        subscription = Observable.just(url).subscribeOn(Schedulers.io())
                .map(new Func1<String, List<NewsDetailItem>>() {
                         @Override
                         public List<NewsDetailItem> call(String url) {
                             list = new ArrayList<>();
                             try {

                                 Document doc = Jsoup.connect(url).userAgent(USERAGENT_DESKTOP).timeout(10000).get();

                                 Elements forms = doc.select("form");
                                 Element content = null;
                                 if (forms.size() > 0) {
                                     for (Element e : forms) {
                                         if (e.attr("name").equals("_newscontent_fromname")) {
                                             content = e;
                                         }
                                     }
                                 }

                                 if (content != null) {
                                     Element title = null;
                                     Element info = null;
                                     Element value = null;
                                     Elements links = null;
                                     if (content.select("div.link_16").size() > 0) {
                                         title = content.select("div.link_16").first();
                                         info = content.select("div.link_1").first();
                                     } else if (content.select("div.c_title").size() > 0) {
                                         title = content.select("div.c_title").first();
                                         info = content.select("div.xinxi").first();
                                     } else if (content.select("h2.title").size() > 0) {
                                         title = content.select("h2.title").first();
                                         info = content.select("div.author").first();
                                     }
                                     if (title != null && info != null) {
                                         NewsDetailItem item1 = new NewsDetailItem();
                                         item1.setType(4);
                                         item1.setValue(title.text());
                                         list.add(item1);
                                         NewsDetailItem item2 = new NewsDetailItem();
                                         item2.setType(5);
                                         item2.setValue(info.text());
                                         list.add(item2);
                                     }

                                     if (content.select("div#vsb_content_500").size() > 0) {
                                         value = content.select("div#vsb_content_500").first();
                                     } else if (content.select("div#vsb_content").size() > 0) {
                                         value = content.select("div#vsb_content").first();
                                     }

                                     if (content.select("a").size() > 0) {
                                         links = content.select("a");
                                     }

                                     if (value != null) {
                                         //Log.e("value", value.html());
                                         Elements pList = value.select("p");
                                         //Log.e("value", pList.size() + "");
                                         for (Element e : pList) {
                                             //抓取图片
                                             if (e.select("img").size() > 0) {
                                                 for (Element img : e.select("img")) {
                                                     NewsDetailItem item = new NewsDetailItem();
                                                     item.setType(2);
                                                     item.setValue(img.text());
                                                     item.setLink(img.attr("abs:src"));
                                                     list.add(item);
                                                     //Log.e("item", item.toString());
                                                 }
                                             }
                                             //抓取文字

                                             if (e.select("span").select("a").size() > 0) {
                                                 NewsDetailItem item = new NewsDetailItem();
                                                 item.setType(3);
                                                 item.setValue(e.select("span").select("a").text());
                                                 item.setLink(e.select("span").select("a").attr("abs:href"));
                                                 list.add(item);
                                                 //Log.e("item", item.toString());
                                             }
                                             if (e.text() != "") {
                                                 NewsDetailItem item = new NewsDetailItem();
                                                 item.setType(1);
                                                 item.setValue(e.text());
                                                 list.add(item);
                                                 //Log.e("item", item.toString());
                                             }

                                         }

                                     }

                                     if (links != null && links.size() > 0) {
                                         for (Element e : links) {
                                             if (e.text() != "" && e.attr("abs:href") != "" && e.text() != "大" && e.text() != "中" && e.text() != "小" && e.text() != "打印" && e.text() != "收藏" && e.text() != "关闭") {
                                                 NewsDetailItem item = new NewsDetailItem();
                                                 item.setType(3);
                                                 item.setValue(e.text());
                                                 item.setLink(e.attr("abs:href"));
                                                 list.add(item);
                                                 //Log.e("item", item.toString());
                                             }
                                         }
                                     }


                                 } else {
                                     WebUtils.openExternal(NewsDetailActivity.this, url);
                                 }


                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                             return list;
                         }
                     }

                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NewsDetailItem>>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onNext(List<NewsDetailItem> normalItems) {
                                   adapter = new NewsDetailAdapter(NewsDetailActivity.this, normalItems);
                                   listView.setAdapter(adapter);
                                   progressBar.setVisibility(View.GONE);
                               }
                           }

                );

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_like:

                if (results.size() <= 0) {

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            NewsDetailItem item;
                            RealmList<NewsDetailItem> value = new RealmList<>();
                            NewsDetail newsDetail = bgRealm.createObject(NewsDetail.class);
                            //Log.e("NewsDetailItem", list.size() + "");
                            //Log.e("NewsDetailItem", host + ":" + title + updateTime);
                            for (int i = 0; i < list.size(); i++) {
                                item = bgRealm.createObject(NewsDetailItem.class);
                                item.setType(list.get(i).getType());
                                item.setValue(list.get(i).getValue());
                                item.setLink(list.get(i).getLink());
                                //Log.e("NewsDetailItem", list.get(i).toString());
                                value.add(item);
                            }
                            newsDetail.setHost(host);
                            newsDetail.setTitle(title);
                            newsDetail.setUpdatetime(updateTime);
                            newsDetail.setValue(value);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            //Log.e("Star!", "Realm.Transaction.OnSuccess");
                            imgLike.setImageResource(R.drawable.ic_like2);
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            //Log.e("Star!", "Realm.Transaction.OnError");
                            Toast.makeText(getApplicationContext(), "收藏失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            results.deleteAllFromRealm();
                            imgLike.setImageResource(R.drawable.ic_like);
                        }
                    });
                }
                break;
            case R.id.img_open_ie:
                WebUtils.openExternal(this, host);
                break;
            case R.id.img_share:
                ShareUtils.shareText(this, title + "\n" + host);
                break;
        }
    }
}
