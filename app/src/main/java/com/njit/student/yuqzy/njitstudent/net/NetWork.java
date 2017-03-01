package com.njit.student.yuqzy.njitstudent.net;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.njit.student.yuqzy.njitstudent.Event.BookShelfEvent;
import com.njit.student.yuqzy.njitstudent.Event.BreakRulesEvent;
import com.njit.student.yuqzy.njitstudent.Event.CourseEvent;
import com.njit.student.yuqzy.njitstudent.Event.CurrentReadEvent;
import com.njit.student.yuqzy.njitstudent.Event.HotRecEvent;
import com.njit.student.yuqzy.njitstudent.Event.InfoDoorResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.LibraryResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.LibrarySecretCode;
import com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.OrderBookEvent;
import com.njit.student.yuqzy.njitstudent.Event.PersonInfoEvent;
import com.njit.student.yuqzy.njitstudent.Event.PreReadEvent;
import com.njit.student.yuqzy.njitstudent.Event.Scores;
import com.njit.student.yuqzy.njitstudent.Event.SearchHistEvent;
import com.njit.student.yuqzy.njitstudent.Event.SecretCode;
import com.njit.student.yuqzy.njitstudent.Event.UrlsMap;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.PersonInfo;
import com.njit.student.yuqzy.njitstudent.model.BookShelfItem;
import com.njit.student.yuqzy.njitstudent.model.CurrentReadItem;
import com.njit.student.yuqzy.njitstudent.model.DebtInfoItem;
import com.njit.student.yuqzy.njitstudent.model.FormKB;
import com.njit.student.yuqzy.njitstudent.model.FormSJK;
import com.njit.student.yuqzy.njitstudent.model.FormTTBinfo;
import com.njit.student.yuqzy.njitstudent.model.HotRecommendItem;
import com.njit.student.yuqzy.njitstudent.model.OrderBookItem;
import com.njit.student.yuqzy.njitstudent.model.PreReadItem;
import com.njit.student.yuqzy.njitstudent.model.SearchHistItem;
import com.njit.student.yuqzy.njitstudent.ui.info.course.NJITCourseFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.library.LibraryFragment;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.njit.student.yuqzy.njitstudent.AppGlobal.BASE_LOGIN_HOST;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.BASE_LOGIN_HOST_KEY;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.NJIT_ZF_HOST;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.NJIT_ZF_LOGIN;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.USERAGENT;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.USERAGENT_DESKTOP;

public class NetWork {
    public static CookieStore cookieStore = null;
    public static String iPlanetDirectoryPro = "";
    public static String JSESSIONID = "";

    public NetWork() {

    }

    public static void LoginDialog(Context context) {
        new MaterialDialog.Builder(context)
                .title("教务网登录")
                .customView(R.layout.login, false)
                .negativeText("取消")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText etLoginName = (EditText) dialog.findViewById(R.id.et_login_name);
                        EditText etLoginPwd = (EditText) dialog.findViewById(R.id.et_login_password);
                        iPlanetDirectoryPro = "";
                        JSESSIONID = "";
                        NetWork.Login(etLoginName.getText().toString(), etLoginPwd.getText().toString());
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    public static void Login(final String name, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //第一次获取key
                    Connection.Response res = Jsoup.connect(BASE_LOGIN_HOST)
                            .method(Connection.Method.POST)
                            .execute();
                    Log.e("first response", res.cookies().toString());
                    JSESSIONID = res.cookie("JSESSIONID");
                    //第二次提交表单，获取登录cookies
                    Connection connection = Jsoup.connect(BASE_LOGIN_HOST_KEY);
                    connection.header("Host", "my.njit.edu.cn");
                    connection.userAgent(USERAGENT);
                    connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    connection.header("Accept-Language", "zh-CN,zh;q=0.8");
                    connection.header("Accept-Encoding", "gzip, deflate");
                    connection.header("Referer", "http://my.njit.edu.cn/index.portal");
                    connection.header("Upgrade-Insecure-Requests", "1");
                    connection.header("Origin", "http://my.njit.edu.cn");
                    connection.header("Content-Type", "application/x-www-form-urlencoded");
                    connection.header("Connection", "keep-alive");
                    connection.header("Cache-Control", "max-age=0");
                    connection.data("Login.Token1", name);
                    connection.data("Login.Token2", password);
                    connection.data("goto", "http://my.njit.edu.cn/loginSuccess.portal");
                    connection.data("gotoOnFail", "http://my.njit.edu.cn/loginFailure.portal");
                    connection.cookie("JSESSIONID", res.cookie("JSESSIONID"));
                    connection.method(Connection.Method.POST);
                    Connection.Response re = connection.execute();

                    Log.e("second cookies", re.cookies().toString());
                    //得到数据
                    Document objectDoc = Jsoup.connect(BASE_LOGIN_HOST)
                            .cookies(re.cookies())
                            .get();
                    if (IsLoginSuccessful(objectDoc)) {
                        iPlanetDirectoryPro = re.cookie("iPlanetDirectoryPro");
                        GetPortUrl();
                        GoLibrary();
                    }
                } catch (IOException e) {

                }
            }
        }).start();
    }


    //判断登录成功
    public static boolean IsLoginSuccessful(Document loginresult) {

        Elements success = loginresult.select("div#container").select("div#header").select("ul#board").select("a[href]");
        if (success != null) {
            //先判断是否登录成功，若成功直接退出
            for (Element link : success) {
                //获取所要查询的URL,这里对应地址按钮的名字叫成绩查询
                if (link.text().contains("退出登录")) {
                    Log.i("xyz", "登录成功");
                    EventBus.getDefault().post(
                            new InfoDoorResponseCode(InfoDoorResponseCode.LOGIN_OK));
                    return true;
                }
            }
        }
        Elements error = loginresult.select("div#loginMsg.err-msg");
        if (error != null) {
            for (Element link : error) {
                //获取错误信息
                if (link.text().equals("")) {
                    Log.i("xyz", "用户名密码不能为空");
                    EventBus.getDefault().post(
                            new InfoDoorResponseCode(InfoDoorResponseCode.LOGIN_PASSWORD_OR_PASSWORD_NULL));
                } else if (link.text().contains("用户不存在或密码错误")) {
                    Log.i("xyz", "用户不存在或密码错误");
                    EventBus.getDefault().post(
                            new InfoDoorResponseCode(InfoDoorResponseCode.LOGIN_USERNAME_OR_PASSWORD_ERROR));
                }
            }
        }
        return false;
    }

    public static void GetPortUrl() {
        //http://my.njit.edu.cn/index.portal?.pn=p14019
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect("http://my.njit.edu.cn/index.portal?.pn=p14019");
                    connection.header("Host", "my.njit.edu.cn");
                    connection.userAgent(USERAGENT_DESKTOP);
                    connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    connection.header("Accept-Language", "zh-CN,zh;q=0.8");
                    connection.header("Accept-Encoding", "gzip, deflate, sdch");
                    connection.header("DNT", "1");
                    connection.header("Connection", "keep-alive");
                    connection.header("Cache-Control", "max-age=0");
                    connection.cookie("JSESSIONID", JSESSIONID);
                    connection.cookie("iPlanetDirectoryPro", iPlanetDirectoryPro);
                    connection.method(Connection.Method.GET);
                    Connection.Response re = connection.execute();
                    Document document = re.parse();
                    Element table = document.select("table#porletsLayout").select("div.portletContent").first();
                    if (table != null) {
                        Elements list = table.select("tr");
                        for (Element e : list) {
                            Log.e("port", e.html());
                        }
                    }
                } catch (IOException e) {

                }

            }
        }).start();
    }

    //获取图书馆验证码
    public static void getLibrarySecretCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                String url = "http://opac.lib.njit.edu.cn/reader/captcha.php?" + Math.random();
                HttpPost httPost = new HttpPost(url);
                try {
                    HttpResponse httpResponse = client.execute(httPost);
                    cookieStore = ((AbstractHttpClient) client).getCookieStore();
                    Log.e("secret code cookies", cookieStore.toString());
                    HttpEntity entity = httpResponse.getEntity();
                    byte[] bytes = new byte[1024];
                    bytes = EntityUtils.toByteArray(entity);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Log.e("do library", "here");
                    EventBus.getDefault().post(new LibrarySecretCode(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //这里会打开学生图书馆个人信息页
    public static void LibraryLogin(final String user, final String password, final String verifation) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultHttpClient defaultclient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost("http://opac.lib.njit.edu.cn/reader/redr_verify.php");
                HttpResponse httpResponse;

                //设置post参数
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("number", user));
                params.add(new BasicNameValuePair("passwd", password));
                params.add(new BasicNameValuePair("captcha", verifation));
                params.add(new BasicNameValuePair("select", "cert_no"));
                params.add(new BasicNameValuePair("returnUrl", ""));

                if (cookieStore == null) {
                    getLibrarySecretCode();
                } else {
                    defaultclient.setCookieStore(cookieStore);

                }
                //获得个人主界面的HTML
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                    httpResponse = defaultclient.execute(httpPost);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));

                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                       // Log.e("html", MAINBODYHTML);
                        isLibraryLoginOK(MAINBODYHTML);
//                        GetCurrentRead();
//                        GetBookShelf();
//                        GetJGLS();
//                        GetJSLS();
//                        GetPreRead();
//                        GetWTXX();
//                        GetWZJK();
//                        GetYYXX();

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void isLibraryLoginOK(String result) {
        Document document = Jsoup.parse(result);
        Log.e("is login ok", "do here" + document.html());
        Elements success = document.select("div#header_opac").select("a[href]");
        Log.e("is login ok", "do here" + success.size() + success.html());
        if (success != null) {
            for (Element e : success) {

                Log.e("is login ok", e.html());
                if (e.text().contains("注销")) {
                    EventBus.getDefault().post(new LibraryResponseCode(LibraryResponseCode.LOGIN_OK));
                }
            }
        } else {
            EventBus.getDefault().post(new LibraryResponseCode(LibraryResponseCode.LOGIN_ERROR));
        }

        return;
    }

    //得到当前借阅页
    public static void GetCurrentRead() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "http://opac.lib.njit.edu.cn/reader/book_lst.php";
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(host);
                HttpResponse httpResponse;

                if (cookieStore == null) {

                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpGet.addHeader("Referer", "http://opac.lib.njit.edu.cn/reader/redr_info.php");
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpGet);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                       // Log.e("test content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);

                        Element table = doc.select("div#mainbox").select("div#container").select("div#mylib_content").select("table.table_line").first();
                        Elements list = table.select("tr");
                        List<CurrentReadItem> items = new ArrayList<CurrentReadItem>();
                        for (int i = 1; i < list.size(); i++) {
                            Element content = list.get(i);
                            CurrentReadItem item = new CurrentReadItem();
                            Elements value = content.select("td");
                            item.setId(value.get(0).text());
                            item.setName(value.get(1).text());
                            item.setGetTime(value.get(2).text());
                            item.setReturnTime(value.get(3).text());
                            item.setContinueTimes(value.get(4).text());
                            item.setPlace(value.get(5).text());
                            item.setOtherThing(value.get(6).text());
                            //item.setPlace(value.get(5).text());
                            String base=value.get(1).select("a").attr("href");
                            item.setBookUrl("http://opac.lib.njit.edu.cn"+base.substring(2));
                            items.add(item);
                        }
                        EventBus.getDefault().post(new CurrentReadEvent(items, SettingsUtil.getXueHao()));

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ).start();

    }

    //得到借阅历史页
    public static void GetPreRead() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "http://opac.lib.njit.edu.cn/reader/book_hist.php";
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(host);
                HttpResponse httpResponse;
                //设置post参数
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("para_string", "all"));
                params.add(new BasicNameValuePair("topage", "1"));

                if (cookieStore == null) {

                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpPost.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpPost.addHeader("Referer", "http://opac.lib.njit.edu.cn/reader/redr_info.php");
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpPost);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        //Log.e("test content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);

                        Element table = doc.select("div#mainbox").select("div#container").select("div#mylib_content").select("table.table_line").first();
                        Elements list = table.select("tr");
                        List<PreReadItem> items = new ArrayList<PreReadItem>();
                        for (int i = 1; i < list.size(); i++) {
                            Element content = list.get(i);
                            PreReadItem item = new PreReadItem();
                            Elements value = content.select("td");
                            item.setId(value.get(1).text());
                            item.setName(value.get(2).text());
                            item.setAuthor(value.get(3).text());
                            item.setReadTime(value.get(4).text());
                            item.setReturnTime(value.get(5).text());
                            item.setPlace(value.get(6).text());
                            String base=value.get(2).select("a").attr("href");
                            item.setBookUrl("http://opac.lib.njit.edu.cn"+base.substring(2));
                            items.add(item);
                        }
                        EventBus.getDefault().post(new PreReadEvent(items, SettingsUtil.getXueHao()));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ).start();

    }

    //得到荐购历史页
    public static void GetJGLS() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "http://opac.lib.njit.edu.cn/reader/asord_lst.php";
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(host);
                HttpResponse httpResponse;

                if (cookieStore == null) {

                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpGet.addHeader("Referer", "http://opac.lib.njit.edu.cn/reader/redr_info.php");
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpGet);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        //Log.e("test content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);


                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ).start();

    }

    //得到预约信息页
    public static void GetYYXX() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "http://opac.lib.njit.edu.cn/reader/preg.php";
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(host);
                HttpResponse httpResponse;

                if (cookieStore == null) {

                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpGet.addHeader("Referer", "http://opac.lib.njit.edu.cn/reader/redr_info.php");
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpGet);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        //Log.e("test content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);
                        Element table = doc.select("div#mainbox").select("div#container").select("div#mylib_content").select("table.table_line").first();
                        Elements list = table.select("tr");
                        List<OrderBookItem> items = new ArrayList<OrderBookItem>();
                        for (int i = 1; i < list.size(); i++) {
                            Element content = list.get(i);
                            OrderBookItem item = new OrderBookItem();
                            Elements value = content.select("td");
                            item.setId(value.get(0).text());
                            item.setName(value.get(1).text());
                            item.setPlace(value.get(2).text());
                            item.setOrderTime(value.get(3).text());
                            item.setAbortTime(value.get(4).text());
                            String base=value.get(1).select("a").first().attr("href");
                            item.setBookUrl("http://opac.lib.njit.edu.cn"+base.substring(2));
                            Log.e("url yyxx", item.getBookUrl());
                            item.setGetBookPlace(value.get(5).text());
                            item.setState(value.get(6).text());
                            items.add(item);
                        }
                        EventBus.getDefault().post(new OrderBookEvent(items, SettingsUtil.getXueHao()));

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ).start();

    }

    //得到委托信息页
    public static void GetWTXX() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "http://opac.lib.njit.edu.cn/reader/relegate.php";
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(host);
                HttpResponse httpResponse;

                if (cookieStore == null) {

                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpGet.addHeader("Referer", "http://opac.lib.njit.edu.cn/reader/redr_info.php");
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpGet);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        Log.e("test content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);


                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ).start();

    }

    //得到我的书架页
    public static void GetBookShelf() {

        final BookShelfEvent shelfEvent = new BookShelfEvent();
        final Thread t0 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    String host = "http://opac.lib.njit.edu.cn/reader/book_shelf.php";
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(host);
                    HttpResponse httpResponse;

                    if (cookieStore == null) {

                    } else {
                        client.setCookieStore(cookieStore);
                    }
                    try {
                        httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);
                        httpGet.addHeader("Referer", "http://opac.lib.njit.edu.cn/reader/redr_info.php");
                        client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                        httpResponse = client.execute(httpGet);
                        Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            HttpEntity entity = httpResponse.getEntity();

                            String MAINBODYHTML = EntityUtils.toString(entity);

                            Document doc = Jsoup.parse(MAINBODYHTML);
                            Elements list = doc.select("div#mainbox").select("div#container").select("div#mylib_content").select("p").select("a");
                            List<String> names = new ArrayList<>();
                            List<String> urls = new ArrayList<>();
                            for (Element e : list) {
                                Log.e("test content", e.html());
                                names.add(e.text());
                                urls.add(host+e.attr("href"));
                                Log.e("book shelf", e.attr("href"));
                            }
                            shelfEvent.setPersonXH(SettingsUtil.getXueHao());
                            shelfEvent.setShelfName(names);
                            shelfEvent.setShelfUrl(urls);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        );
        Thread t1 = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            t0.join();
                        } catch (InterruptedException e) {

                        }
                        synchronized (this) {
                            List<List<BookShelfItem>> bookShelfItems = new ArrayList<>();
                            for (String url : shelfEvent.getShelfUrl()) {

                                DefaultHttpClient client = new DefaultHttpClient();
                                HttpGet httpGet = new HttpGet(url);
                                HttpResponse httpResponse;

                                if (cookieStore == null) {

                                } else {
                                    client.setCookieStore(cookieStore);
                                }
                                try {
                                    httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);
                                    httpGet.addHeader("Referer", "http://opac.lib.njit.edu.cn/reader/redr_info.php");
                                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                                    httpResponse = client.execute(httpGet);
                                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                        HttpEntity entity = httpResponse.getEntity();

                                        String MAINBODYHTML = EntityUtils.toString(entity);
                                        //Log.e("test content", MAINBODYHTML);
                                        Document doc = Jsoup.parse(MAINBODYHTML);
                                        Element table = doc.select("div#mainbox").select("div#container").select("div#mylib_content").select("table.table_line").first();

                                        Elements list = table.select("tr");
                                        List<BookShelfItem> items = new ArrayList<BookShelfItem>();
                                        for (int i = 1; i < list.size(); i++) {
                                            Element content = list.get(i);
                                            BookShelfItem item = new BookShelfItem();
                                            Elements value = content.select("td");
                                            item.setName(value.get(1).text());
                                            item.setAuthors(value.get(2).text());
                                            item.setPublishCompany(value.get(3).text());
                                            item.setPublishTime(value.get(4).text());
                                            item.setIdSuoShu(value.get(5).text());
                                            String base=value.get(1).select("a").first().attr("href");
                                            item.setBookUrl("http://opac.lib.njit.edu.cn"+base.substring(2));
                                            Log.e("url shelf book url", item.getBookUrl());
                                            items.add(item);
                                        }
                                        bookShelfItems.add(items);
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (ClientProtocolException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            shelfEvent.setBookList(bookShelfItems);
                            EventBus.getDefault().post(shelfEvent);
                        }
                    }
                }

        );

        t0.start();
        t1.start();

    }


    //得到违章缴款页
    public static void GetWZJK() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "http://opac.lib.njit.edu.cn/reader/fine_pec.php";
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(host);
                HttpResponse httpResponse;

                if (cookieStore == null) {

                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpGet.addHeader("Referer", "http://opac.lib.njit.edu.cn/reader/redr_info.php");
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpGet);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        //Log.e("test content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);
                        Element content = doc.select("div#mainbox").select("div#container").select("div#mylib_content").first();
                        Elements tables = content.select("table.table_line");
                        List<DebtInfoItem> items = new ArrayList<DebtInfoItem>();
                        for (Element table : tables) {
                            Elements list = table.select("tr");
                            for (int i = 1; i < list.size(); i++) {
                                Element value = list.get(i);
                                DebtInfoItem item = new DebtInfoItem();
                                Elements td = value.select("td");
                                item.setId_tiaoma(td.get(0).text());
                                item.setId_suoshu(td.get(1).text());
                                item.setName(td.get(2).text());
                                item.setAuthors(td.get(3).text());
                                item.setReadTime(td.get(4).text());
                                item.setReturnTime(td.get(5).text());
                                item.setPlace(td.get(6).text());
                                item.setShould_pay(td.get(7).text());
                                item.setActual_pay(td.get(8).text());
                                item.setState(td.get(9).text());
                                String base=td.get(2).select("a").first().attr("href");
                                item.setBookurl("http://opac.lib.njit.edu.cn"+base.substring(2));
                                items.add(item);
                            }
                        }

                        EventBus.getDefault().post(new BreakRulesEvent(items, SettingsUtil.getXueHao()));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ).start();

    }

    //得到检索历史页
    public static void GetJSLS() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "http://opac.lib.njit.edu.cn/reader/search_hist.php";
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(host);
                HttpResponse httpResponse;

                if (cookieStore == null) {

                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpGet.addHeader("Referer", "http://opac.lib.njit.edu.cn/reader/redr_info.php");
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpGet);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        //Log.e("test content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);
                        Element table = doc.select("div#mainbox").select("div#container").select("div#mylib_content").select("table.table_line").first();
                        Elements list = table.select("tr");
                        List<SearchHistItem> items = new ArrayList<SearchHistItem>();
                        for (int i = 1; i < list.size(); i++) {
                            Element content = list.get(i);
                            SearchHistItem item = new SearchHistItem();
                            Elements value = content.select("td");
                            item.setContent(value.get(1).text());
                            item.setTime(value.get(2).text());
                            String base=value.get(1).select("a[href]").attr("href");
                            item.setUrl("http://opac.lib.njit.edu.cn"+base.substring(2));
                            Log.e("url search", item.getUrl());
                            items.add(item);
                        }
                        EventBus.getDefault().post(new SearchHistEvent(items, SettingsUtil.getXueHao()));

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ).start();

    }

    //得到热门推荐页
    public static void GetHotRecommend() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "http://opac.lib.njit.edu.cn/top/top_lend.php";
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(host);
                HttpResponse httpResponse;

                try {
                    httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);

                    httpResponse = client.execute(httpGet);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        //Log.e("test content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);
                        Element table = doc.select("div#mainbox").select("div#container").select("table.table_line").first();
                        Elements list = table.select("tr");
                        List<HotRecommendItem> items = new ArrayList<HotRecommendItem>();
                        for (int i = 1; i < list.size(); i++) {
                            Element content = list.get(i);
                            HotRecommendItem item = new HotRecommendItem();
                            Elements value = content.select("td");
                            item.setName(value.get(1).text());
                            item.setAuthor(value.get(2).text());
                            String base=value.get(1).select("a").first().attr("href");
                            item.setBookUrl("http://opac.lib.njit.edu.cn"+base.substring(2));
                            item.setPublishInfo(value.get(3).text());
                            item.setIdSuoShu(value.get(4).text());
                            item.setNum(value.get(5).text());
                            item.setReadTimes(value.get(6).text());
                            item.setRead(value.get(7).text());
                            Log.e("url search","   uuuu  "+item.getBookUrl());
                            items.add(item);
                        }
                        EventBus.getDefault().post(new HotRecEvent(items, SettingsUtil.getXueHao()));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ).start();

    }

    public static void GoLibrary() {
        //http://opac.lib.njit.edu.cn/reader/redr_info.php
        //http://opac.lib.njit.edu.cn/reader/book_lst.php
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect("http://opac.lib.njit.edu.cn/reader/redr_info.php");
                    connection.header("Host", "opac.lib.njit.edu.cn");
                    connection.userAgent(USERAGENT_DESKTOP);
                    connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    connection.header("Accept-Language", "zh-CN,zh;q=0.8");
                    connection.header("Accept-Encoding", "gzip, deflate, sdch");
                    connection.header("DNT", "1");
                    connection.header("Connection", "keep-alive");
                    connection.header("Cache-Control", "max-age=0");
                    //connection.header("Refer","http://opac.lib.njit.edu.cn/reader/redr_info.php");
                    connection.cookie("iPlanetDirectoryPro", iPlanetDirectoryPro);
                    connection.method(Connection.Method.GET);
                    Connection.Response re = connection.execute();
                    Document document = re.parse();
                    Element table = document.select("table#table_line").first();
                    if (table != null) {
                        Elements list = table.select("tr");
                        for (Element e : list) {
                            Log.e("port", e.html());
                        }
                    }
                } catch (IOException e) {

                }

            }
        }).start();
    }

    //http://opac.lib.njit.edu.cn/opac/openlink.php?strSearchType=title&match_flag=forward&historyCount=1&strText=java&doctype=ALL&displaypg=20&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL
    //http://opac.lib.njit.edu.cn/opac/openlink.php?location=ALL&title=java&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&count=1237&with_ebook=&page=2
    //搜索内容  内容类型 搜索匹配 搜索书目类型
    public static void LibrarySearchSimple(String content,String searchOption,String pipei,String type){
       final String host="http://opac.lib.njit.edu.cn/opac/openlink.php?strSearchType="+searchOption+"&match_flag="+pipei+"&historyCount=1&strText="+content+"&doctype="+type+"&displaypg=20&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL" ;
        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(host);
                HttpResponse httpResponse;

                try {
                    httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);

                    httpResponse = client.execute(httpGet);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        //Log.e("test content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ).start();
    }
}
