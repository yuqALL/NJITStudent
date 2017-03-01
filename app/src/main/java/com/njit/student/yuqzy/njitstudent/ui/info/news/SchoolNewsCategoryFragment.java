package com.njit.student.yuqzy.njitstudent.ui.info.news;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.NormalItem;
import com.njit.student.yuqzy.njitstudent.ui.adapter.NormalAdapter;
import com.njit.student.yuqzy.njitstudent.ui.base.BaseContentFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.njit.student.yuqzy.njitstudent.AppGlobal.SCHOOL_NOTIFICATION;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.SCHOOL_XUESHUHUODONG;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.USERAGENT_DESKTOP;


public class SchoolNewsCategoryFragment extends BaseContentFragment {

    private RecyclerView recyclerView;
    private NormalAdapter adapter;
    private String baseUrl = "";
    private boolean isLoading = false;
    private String usedUrl;
    private String preUrl="";
    private boolean scrollTop=false;
    private Subscription subscription;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void initViews() {
        super.initViews();
        baseUrl = getArguments().getString("url");
        usedUrl=baseUrl;
        recyclerView = findView(R.id.rv_gank);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NormalAdapter(getActivity(), null);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    scrollTop=false;
                    isLoading = true;
                    Log.e("onScrollStateChanged x",usedUrl);
                    getSchoolNewsFromServer();

                }else if(!recyclerView.canScrollVertically(-1) && !isLoading)
                {
                    //RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                    scrollTop=true;
                    usedUrl=baseUrl;
                    Log.e("onScrollStateChanged s",usedUrl);
                    isLoading = true;
                    getSchoolNewsFromServer();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }


    private void getSchoolNewsFromServer() {
        showRefreshing(true);
        String url=usedUrl;
        subscription = Observable.just(url).subscribeOn(Schedulers.io())
                .map(new Func1<String, List<NormalItem>>() {
                         @Override
                         public List<NormalItem> call(String url) {
                             List<NormalItem> normalItems = new ArrayList<>();
                             try {

                                 Document doc = Jsoup.connect(url).userAgent(USERAGENT_DESKTOP).timeout(10000).get();
                                 String container;
                                 String srcFlag="li";
                                 String dateFlag;
                                 String nextFlag="a[href].Next";
                                 if(url.indexOf(SCHOOL_NOTIFICATION)>=0||url.indexOf(SCHOOL_XUESHUHUODONG)>=0)
                                 {
                                     container="div.conter";
                                     dateFlag="span.date";
                                 }else{
                                     container="div.pageconter";
                                     dateFlag="span.y2";
                                 }
                                 Element list = doc.select(container).first();
                                 Elements items = list.select(srcFlag);

                                 for (Element element : items) {
                                     NormalItem item = new NormalItem();
                                     item.setName(element.select("a[href]").attr("title"));

                                     item.setUpdateTime(element.select(dateFlag).text());

                                     item.setUrl(element.select("a[href]").attr("abs:href"));

                                     normalItems.add(item);
                                 }

                                 Elements nextUrls = list.select(nextFlag);
                                 if(nextUrls.size()>0) {
                                     for (Element nextUrl : nextUrls) {
                                         if (nextUrl.text().trim().equals("下页")) {
                                             usedUrl = nextUrl.attr("abs:href");
                                         }
                                     }
                                 }
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                             return normalItems;
                         }
                     }

                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NormalItem>>() {
                                      @Override
                                      public void onCompleted() {
                                          isLoading = false;
                                      }

                                      @Override
                                      public void onError(Throwable e) {
                                          isLoading = false;
                                          showRefreshing(false);
                                      }

                                      @Override
                                      public void onNext(List<NormalItem> normalItems) {
                                          showRefreshing(false);

                                          if (adapter.getData() == null || adapter.getData().size() == 0 ||scrollTop) {
                                              adapter.setNewData(normalItems);
                                          } else {
                                              Log.e("on next",preUrl+"\n"+usedUrl);
                                              if(preUrl!=usedUrl) {
                                                  adapter.addData(adapter.getData().size(), normalItems);
                                                  preUrl=usedUrl;
                                              }
                                          }
                                      }
                                  }

                        );
    }

    @Override
    protected void lazyFetchData() {
        adapter.setNewData(null);
        getSchoolNewsFromServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

}
