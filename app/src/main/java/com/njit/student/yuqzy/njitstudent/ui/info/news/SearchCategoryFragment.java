package com.njit.student.yuqzy.njitstudent.ui.info.news;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.NormalItem;
import com.njit.student.yuqzy.njitstudent.ui.adapter.NormalAdapter;
import com.njit.student.yuqzy.njitstudent.ui.base.BaseContentFragment;

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
        try {
            this.search = URLEncoder.encode(search, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (adapter != null) {
            if (adapter.getItemCount() > 0) {
                adapter.setNewData(null);
            }
        }

        getCookies();
        //getSearchResultsFromServer();
    }

    private void getCookies() {
        subscription = Observable.just(baseUrl).subscribeOn(Schedulers.io()).map(new Func1<String, String>() {
            @Override
            public String call(String host) {
                try {

                    //第一次请求
                    Connection con=Jsoup.connect(SCHOOL_INDEX_HOST);//获取连接
                    con.header("User-Agent", USERAGENT_DESKTOP);//配置模拟浏览器
                    Connection.Response rs= con.execute();//获取响应
                    Document d1=Jsoup.parse(rs.body());//转换为Dom树
                    Element et= d1.select("form#au4a").first();//获取form表单，可以通过查看页面源码代码得知
                    Elements list=et.select("input");

                    //获取，cooking和表单属性，下面map存放post时的数据
                    Map<String, String> datas=new HashMap<>();
                    for(Element e:list){
                        if(e.attr("name").equals("lucenenewssearchkeyword")){
                            e.attr("value", "5Zu95a62");
                        }

                        if(e.attr("name").equals("showkeycode")){
                            e.attr("value","国家");
                        }

                        if(e.attr("name").length()>0){//排除空值表单属性
                            datas.put(e.attr("name"), e.attr("value"));
                        }
                    }
                    Log.e("form",et.html());




                    /**
                     * 第二次请求，post表单数据，以及cookie信息
                     *
                     * **/
                    Connection con2=Jsoup.connect(host);
                    con2.header("User-Agent", USERAGENT_DESKTOP);
                    //设置cookie和post上面的map数据
                    Connection.Response login=con2.ignoreContentType(true).method(Connection.Method.POST).data(datas).cookies(rs.cookies()).execute();
                    //打印，登陆成功后的信息
                    Log.e("post ",login.body());

                    //登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可
                    Map<String, String> map=login.cookies();
                    for(String s:map.keySet()){
                        System.out.println(s+"      "+map.get(s));
                    }



                    //第一次获取key
//                    Connection.Response res = Jsoup.connect(SCHOOL_INDEX_HOST)
//                            .method(Connection.Method.POST)
//                            .execute();
//                    Log.e("first response",res.cookies().toString());


                    //第二次提交表单，获取登录cookies
                    Connection connection = Jsoup.connect(host);
                    connection.header("Host", "my.njit.edu.cn");
                    connection.userAgent(USERAGENT_DESKTOP);
                    connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    connection.header("Accept-Language", "zh-CN,zh;q=0.8");
                    connection.header("Accept-Encoding", "gzip, deflate");
                    //connection.header("Referer", "http://www.njit.edu.cn/index.htm");
                    connection.header("Upgrade-Insecure-Requests", "1");
                    //connection.header("Origin", "http://my.njit.edu.cn");
                    connection.header("Content-Type", "application/x-www-form-urlencoded");
                    connection.header("Connection", "keep-alive");
                    connection.header("Cache-Control", "max-age=0");
                    connection.data("lucenenewssearchkeyword", "5Zu95a62");
                    //connection.data("_lucenesearchtype", "1");
                    connection.data("showkeycode", "国家");
                    //connection.data("x", "27");
                    //connection.data("y", "13");
                    //connection.cookie("JSESSIONID",res.cookie("JSESSIONID"));
                    connection.method(Connection.Method.POST);
                    Connection.Response re = connection.execute();

                    Log.e("second cookies", re.cookies().toString());


                    //http://jwc.njit.edu.cn/lm_list.jsp?urltype=tree.TreeTempUrl&wbtreeid=1035
                    //http://jwc.njit.edu.cn/lm_list.jsp?totalpage=59&PAGENUM=2&urltype=tree.TreeTempUrl&wbtreeid=1035
                    //得到数据
//                    Document objectDoc = Jsoup.connect(host)
//                            .userAgent(USERAGENT_DESKTOP)
//                            .get();
                    Document objectDoc = re.parse();
                    Log.e("d2", "" + objectDoc.html());


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
//                Toast.makeText(getContext(),cookies.getiPlanetDirectoryPro(),Toast.LENGTH_SHORT).show();
//                if(cookies.getiPlanetDirectoryPro()==""||cookies.getiPlanetDirectoryPro()==null)
//                {
//                    LoginDialog();
//                }
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
        char[] input=new char[]{'3','1','7','2','5','4'};
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
            chr1 = (char)((enc1 << 2) | (enc2 >> 4));
            chr2 = (char)(((enc2 & 15) << 4) | (enc3 >> 2));
            chr3 = (char)(((enc3 & 3) << 6) | enc4);
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
        char c, c1, c2,c3;
        c=c1=c2=c3=0;
        while (j < output.length()) {
            c = output.charAt(j);
            if (c < 128) {
                string += c;
                i++;
            } else if ((c > 191) && (c < 224)) {
                c2 = output.charAt(j+1);
                string += (((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = output.charAt(j+1);
                c3 = output.charAt(j+2);
                string += (((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }

}
