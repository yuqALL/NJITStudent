package com.njit.student.yuqzy.njitstudent.ui.info.news;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.NormalItem;
import com.njit.student.yuqzy.njitstudent.ui.adapter.NormalAdapter;
import com.njit.student.yuqzy.njitstudent.ui.base.BaseContentFragment;
import com.njit.student.yuqzy.njitstudent.utils.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.njit.student.yuqzy.njitstudent.AppGlobal.SCHOOL_INDEX_HOST;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.SCHOOL_NOTIFICATION;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.SCHOOL_XUESHUHUODONG;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.USERAGENT_DESKTOP;
import static java.lang.Double.isNaN;


public class SearchCategoryFragment extends BaseContentFragment {

    private RecyclerView recyclerView;
    private NormalAdapter adapter;
    private String baseUrl = "";
    private boolean isLoading = false;
    private String usedUrl;
    private String preUrl = "";
    private boolean scrollTop = false;
    private Subscription subscription;
    private String search = "";
    private String search_pre = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void initViews() {
        super.initViews();
        baseUrl = getArguments().getString("search_host");
        usedUrl = baseUrl;
        recyclerView = findView(R.id.rv_gank);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NormalAdapter(getActivity(), null);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    scrollTop = false;
                    isLoading = true;
                    Log.e("onScrollStateChanged x", usedUrl);
                    getSearchResultsFromServer();

                } else if (!recyclerView.canScrollVertically(-1) && !isLoading) {
                    //RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                    scrollTop = true;
                    usedUrl = baseUrl;
                    Log.e("onScrollStateChanged s", usedUrl);
                    isLoading = true;
                    getSearchResultsFromServer();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }


    public void setSearch(String search) {
        Log.e("mx", "set search" + search);
        search_pre=search;
        this.search = Base64.encode(search);

        Log.e("search text",this.search);
        if (adapter != null) {
            if (adapter.getItemCount() > 0) {
                adapter.setNewData(null);
            }
        }

        //getCookies();
        getSearchResultsFromServer();
    }

    private void getCookies() {
        subscription = Observable.just(baseUrl).subscribeOn(Schedulers.io()).map(new Func1<String, String>() {
            @Override
            public String call(String host) {
                //获得个人主界面的HTML
                try {

                    HttpClient client0 = new DefaultHttpClient();

                    HttpPost httPost0 = new HttpPost("http://www.njit.edu.cn/sezch.jsp?wbtreeid=1001");

                    HttpResponse httpResponse0 = client0.execute(httPost0);
                    CookieStore cookieStore = ((AbstractHttpClient) client0).getCookieStore();


                    DefaultHttpClient defaultclient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://www.njit.edu.cn/sezch.jsp?wbtreeid=1001");
                    HttpResponse httpResponse;
                    //设置post参数
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("lucenenewssearchkeyword", search));
                    params.add(new BasicNameValuePair("showkeycode",search));
                    params.add(new BasicNameValuePair("_lucenesearchtype", "1"));
                    params.add(new BasicNameValuePair("searchScope", "0"));
                    params.add(new BasicNameValuePair("x", "0"));
                    params.add(new BasicNameValuePair("y", "0"));
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    Log.e("cookie",cookieStore.toString());
                    defaultclient.setCookieStore(cookieStore);
                    Log.e("search",search);
//                    httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);
//                    httpGet.addHeader("Referer", "http://opac.lib.njit.edu.cn/reader/redr_info.php");
//                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = defaultclient.execute(httpPost);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));

                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        Log.e("second cookies", MAINBODYHTML);
                    }

                } catch (IOException e) {

                }
                return "";
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String cookies) {
            }
        });
    }

    private void getSearchResultsFromServer() {
        showRefreshing(true);
        String url = usedUrl;
        subscription = Observable.just(url).subscribeOn(Schedulers.io())
                .map(new Func1<String, List<NormalItem>>() {
                         @Override
                         public List<NormalItem> call(String url) {
                             List<NormalItem> normalItems = new ArrayList<>();
                             try {

                                 Document doc = Jsoup.connect(url).userAgent(USERAGENT_DESKTOP).timeout(10000).get();
                                 String container;
                                 String srcFlag = "li";
                                 String dateFlag;
                                 String nextFlag = "a[href].Next";
                                 if (url.indexOf(SCHOOL_NOTIFICATION) >= 0 || url.indexOf(SCHOOL_XUESHUHUODONG) >= 0) {
                                     container = "div.conter";
                                     dateFlag = "span.date";
                                 } else {
                                     container = "div.pageconter";
                                     dateFlag = "span.y2";
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
                                 if (nextUrls.size() > 0) {
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

                                   if (adapter.getData() == null || adapter.getData().size() == 0 || scrollTop) {
                                       adapter.setNewData(normalItems);
                                   } else {
                                       Log.e("on next", preUrl + "\n" + usedUrl);
                                       if (preUrl != usedUrl) {
                                           adapter.addData(adapter.getData().size(), normalItems);
                                           preUrl = usedUrl;
                                       }
                                   }
                               }
                           }

                );
    }

    @Override
    protected void lazyFetchData() {
        adapter.setNewData(null);
        //getSearchResultsFromServer();
        //Log.e("test utf", UTF8Encode("国家")+"\n"+UTF8Decode());

        getCookies();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    String _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    // private method for UTF-8 encoding
    private String UTF8Encode(String s) {


        s = s.replace("\r\n", "\n");
        int[] utftext = new int[s.length() * 3];
        int utftextlen = 0;
        for (int n = 0; n < s.length(); n++) {
            int c = s.charAt(n);
            if (c < 128) {
                utftext[utftextlen++] = c;
                Log.e("utf", n + "  try:" + c);
            } else if ((c > 127) && (c < 2048)) {
                utftext[utftextlen++] = (c >> 6) | 192;
                utftext[utftextlen++] = (c & 63) | 128;
                Log.e("utf", n + "  try:" + ((c >> 6) | 192));
                Log.e("utf", n + "  try:" + ((c & 63) | 128));
            } else {
                utftext[utftextlen++] = (c >> 12) | 224;
                utftext[utftextlen++] = ((c >> 6) & 63) | 128;
                utftext[utftextlen++] = (c & 63) | 128;
                Log.e("utf", n + "  try:" + ((c >> 12) | 224));
                Log.e("utf", n + "  try:" + (((c >> 6) & 63) | 128));
                Log.e("utf", n + "  try:" + ((c & 63) | 128));
            }

        }
        int i = 0;
        char chr1, chr2, chr3;
        int enc1, enc2, enc3, enc4;
        StringBuilder string = new StringBuilder();
        while (i < utftextlen) {
            chr1 = (char) utftext[i++];
            chr2 = (char) utftext[i++];
            chr3 = (char) utftext[i++];
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            string.append(_keyStr.charAt(enc1) + _keyStr.charAt(enc2) + _keyStr.charAt(enc3) + _keyStr.charAt(enc4));
        }
        string.append("");
        return string.toString();
    }

    private String UTF8Decode() {
        char[] input = new char[]{'3', '1', '7', '2', '5', '4'};
        String output = "";
        char chr1, chr2, chr3;
        int enc1, enc2, enc3, enc4;
        int i = 0;
        //input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (i < input.length) {
            enc1 = _keyStr.indexOf(input[i++]);
            enc2 = _keyStr.indexOf(input[i++]);
            enc3 = _keyStr.indexOf(input[i++]);
            enc4 = _keyStr.indexOf(input[i++]);
            chr1 = (char) ((enc1 << 2) | (enc2 >> 4));
            chr2 = (char) (((enc2 & 15) << 4) | (enc3 >> 2));
            chr3 = (char) (((enc3 & 3) << 6) | enc4);
            output = output + chr1;
            if (enc3 != 64) {
                output = output + chr2;
            }
            if (enc4 != 64) {
                output = output + chr3;
            }
        }
        String string = "";
        int j = 0;
        char c, c1, c2, c3;
        c = c1 = c2 = c3 = 0;
        while (j < output.length()) {
            c = output.charAt(j);
            if (c < 128) {
                string += c;
                i++;
            } else if ((c > 191) && (c < 224)) {
                c2 = output.charAt(j + 1);
                string += (((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = output.charAt(j + 1);
                c3 = output.charAt(j + 2);
                string += (((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }

}
