package com.njit.student.yuqzy.njitstudent.ui.info.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.BookDetailRealm;
import com.njit.student.yuqzy.njitstudent.database.BookItem;
import com.njit.student.yuqzy.njitstudent.model.BookDetailGson;
import com.njit.student.yuqzy.njitstudent.ui.adapter.BookDetailAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.njit.student.yuqzy.njitstudent.utils.ShareUtils;
import com.njit.student.yuqzy.njitstudent.utils.WebUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
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

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ListView lv_book_detail;
    private String url,name,other;
    private Realm realm;
    private RealmQuery<BookDetailRealm> query;
    private RealmResults<BookDetailRealm> results;
    private Subscription subscription;
    private ImageView imgLike, imgIE,imgShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_book_details);
        Intent intent=getIntent();
        url = intent.getStringExtra("url");
        name = intent.getStringExtra("name");
        other = intent.getStringExtra("other");
        toolbar = (Toolbar) findViewById(R.id.title);
        setSupportActionBar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        imgLike = (ImageView) findViewById(R.id.img_like);
        imgIE = (ImageView) findViewById(R.id.img_open_ie);
        imgShare=(ImageView)findViewById(R.id.img_share);
        imgLike.setOnClickListener(this);
        imgIE.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        lv_book_detail = (ListView) findViewById(R.id.lv_book_detail);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        query = realm.where(BookDetailRealm.class);
        results = query.equalTo("url", url).findAll();
        if (results.size() > 0) {
            imgLike.setImageResource(R.drawable.ic_like2);
            BookDetailRealm bookDetail = results.first();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < bookDetail.getContent().size(); i++) {
                list.add(bookDetail.getContent().get(i).getItem());
            }
            BookDetailAdapter adapter = new BookDetailAdapter(BookDetailActivity.this, list);
            lv_book_detail.setAdapter(adapter);

        } else {
            getBookDetailFromServer(url);
        }
        //getBookDetailFromServer("http://opac.lib.njit.edu.cn/opac/item.php?marc_no=0000523241");

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
    List<String> itemList;
    private void getBookDetailFromServer(String url) {

        subscription = Observable.just(url).subscribeOn(Schedulers.io())
                .map(new Func1<String, List<String>>() {
                         @Override
                         public List<String> call(String url) {
                             itemList = new ArrayList<>();
                             int index = 0;
                             String host = "http://opac.lib.njit.edu.cn/opac/ajax_douban.php?isbn=";
                             try {

                                 Document doc = Jsoup.connect(url).timeout(10000).userAgent(USERAGENT_DESKTOP).get();
                                 Elements ISBN = doc.select("div#mainbox").select("div#container").select("div#content_item").select("div#tabs1").select("div#tabs-1").select("div#book_info").select("div#item_detail").select("dl.booklist");
                                 for (Element e : ISBN) {
                                     if (e.select("dt").first().text().contains("ISBN及定价:") && e.select("dd").first().text().contains("/")) {
                                         String isbn = e.select("dd").first().text().split("/")[0];
                                         isbn = isbn.replaceAll("-", "");
                                         host += isbn;
                                         Log.e("isbn", host);
                                     }
                                 }
                                 BookDetailGson book = null;
                                 DefaultHttpClient client = new DefaultHttpClient();
                                 HttpGet httpGet = new HttpGet(host);
                                 HttpResponse httpResponse;
                                 httpResponse = client.execute(httpGet);
                                 Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                                 if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                     HttpEntity entity = httpResponse.getEntity();

                                     String HTML = EntityUtils.toString(entity);
                                     Log.e("HTML", HTML);
                                     Gson gson = new Gson();
                                     book = gson.fromJson(HTML, new TypeToken<BookDetailGson>() {
                                     }.getType());
                                     Log.e("parse json", book.getImage() + "");

                                 }
                                 Element article = doc.select("div#mainbox").select("div#container").select("div#content_item").first();
                                 Element content = article.select("div#tabs1").select("div#tabs-1").select("div#book_info").first();
                                 Element table = article.select("div#tabs2").select("div#tab_item").select("table#item").first();
                                 //Log.e("content",content.html());
                                 //先解析书目图片
                                 if (book != null && (book.getImage() != null || book.getImage() != "")) {
                                     itemList.add("<image>" + book.getImage());
                                 }
                                 //解析内容
                                 Elements list = content.select("div#item_detail").select("dl.booklist");
//                                 Log.e("list",list.html());
                                 for (Element e : list) {
                                     Log.e("list", e.html());
                                     if (e.attr("id").contains("douban_content") || e.select("dt").text().contains("豆瓣简介")) {
                                         Log.e("do here", e.text());
                                         itemList.add(e.select("dt").text() + "\n" + book.getSummary().replaceAll("<br />", "") + "\n" + book.getAuthor_intro().replaceAll("<br/>", ""));
                                     } else {
                                         itemList.add(e.select("dt").text() + "\n" + e.select("dd").text().replaceAll("br", "\n"));
                                     }
                                 }
                                 //解析table
                                 Elements tr = table.select("tr");
                                 for (int i = 1; i < tr.size(); i++) {
                                     Elements td = tr.get(i).select("td");
                                     itemList.add("\n");
                                     itemList.add("索书号:  " + td.get(0).text());
                                     itemList.add("条码号:  " + td.get(1).text());
                                     itemList.add("年卷期:  " + td.get(2).text());
                                     itemList.add("馆藏地:  " + td.get(3).text());
                                     itemList.add("书刊状态:  " + td.get(4).text());
                                 }


                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                             return itemList;
                         }
                     }

                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onError(Throwable e) {
                               }

                               @Override
                               public void onNext(List<String> itemList) {
                                   BookDetailAdapter adapter = new BookDetailAdapter(BookDetailActivity.this, itemList);
                                   lv_book_detail.setAdapter(adapter);
                               }
                           }

                );
    }


    public static String br2nl(String html) {
        if (html == null) return html;
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        document.select("br").append("/n");
        String s = document.html()
                .replaceAll("&nbsp;", " ");
        return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_like:
                if (results.size() <= 0) {

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            BookItem item;
                            RealmList<BookItem> value = new RealmList<>();
                            BookDetailRealm bookDetail = bgRealm.createObject(BookDetailRealm.class);

                            for (int i = 0; i < itemList.size(); i++) {
                                item = bgRealm.createObject(BookItem.class);
                                item.setItem(itemList.get(i));
                                value.add(item);
                            }
                            bookDetail.setUrl(url);
                            bookDetail.setName(name);
                            bookDetail.setOther(other);
                            bookDetail.setPersonXH(SettingsUtil.getXueHao());
                            bookDetail.setContent(value);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Log.e("Star!", "Realm.Transaction.OnSuccess");
                            imgLike.setImageResource(R.drawable.ic_like2);
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Log.e("Star!", "Realm.Transaction.OnError");
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
                WebUtils.openExternal(this, url);
                break;
            case R.id.img_share:
                ShareUtils.shareText(this,name+"\n"+other+"\n"+url);
                break;
        }
    }
}
