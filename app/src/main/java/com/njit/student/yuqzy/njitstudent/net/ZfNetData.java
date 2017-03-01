package com.njit.student.yuqzy.njitstudent.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.njit.student.yuqzy.njitstudent.Event.CourseEvent;
import com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.PersonInfoEvent;
import com.njit.student.yuqzy.njitstudent.Event.Scores;
import com.njit.student.yuqzy.njitstudent.Event.SecretCode;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.PersonInfo;
import com.njit.student.yuqzy.njitstudent.Event.UrlsMap;
import com.njit.student.yuqzy.njitstudent.model.FormKB;
import com.njit.student.yuqzy.njitstudent.model.FormSJK;
import com.njit.student.yuqzy.njitstudent.model.FormTTBinfo;
import com.njit.student.yuqzy.njitstudent.ui.info.course.NJITCourseFragment;
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
import java.util.Random;

import static com.njit.student.yuqzy.njitstudent.AppGlobal.NJIT_ZF_HOST;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.NJIT_ZF_LOGIN;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.USERAGENT_DESKTOP;
import static com.thefinestartist.utils.content.ContextUtil.getApplicationContext;

public class ZfNetData {
    public static CookieStore cookieStore = null;
    private Boolean login = false;
    private String loginResult;
    public static UrlsMap theUrls = null;
    private Context context;


    public ZfNetData(Context context) {
        this.context = context;
    }


    //获取验证码
    public void getSecretCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();

                HttpPost httPost = new HttpPost("http://jwjx.njit.edu.cn/CheckCode.aspx");
                try {
                    HttpResponse httpResponse = client.execute(httPost);
                    cookieStore = ((AbstractHttpClient) client).getCookieStore();
                    Log.e("secret code cookies", cookieStore.toString());
                    HttpEntity entity = httpResponse.getEntity();
                    byte[] bytes = new byte[1024];
                    bytes = EntityUtils.toByteArray(entity);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    EventBus.getDefault().post(
                            new SecretCode(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //正方教务系统登录
    public void zfLogin(final String user, final String password, final String verifation) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultHttpClient defaultclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(NJIT_ZF_LOGIN);
                HttpResponse httpResponse;

                //设置post参数
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("__VIEWSTATE", "dDwyODE2NTM0OTg7Oz7DjHbBceJNKdGwAEGQuxU3BgFR7w=="));
                params.add(new BasicNameValuePair("Button1", ""));
                params.add(new BasicNameValuePair("hidPdrs", ""));
                params.add(new BasicNameValuePair("hidsc", ""));
                params.add(new BasicNameValuePair("lbLanguage", ""));
                params.add(new BasicNameValuePair("RadioButtonList1", "%D1%A7%C9%FA"));
                params.add(new BasicNameValuePair("TextBox2", password));
                params.add(new BasicNameValuePair("txtSecretCode", verifation));
                params.add(new BasicNameValuePair("txtUserName", user));

                if (cookieStore == null) {
                    getSecretCode();
                } else {
                    defaultclient.setCookieStore(cookieStore);

                }
                //获得个人主界面的HTML
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                    httpResponse = defaultclient.execute(httpPost);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));

                    Log.e("login2 cookies", ((AbstractHttpClient) defaultclient).getCookieStore().toString());
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        StringBuffer sb = new StringBuffer();
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        //Log.e("html",MAINBODYHTML);
                        IsLoginSuccessful(MAINBODYHTML);
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

    //判断登录成功
    public void IsLoginSuccessful(String loginresult) {
        Document doc = Jsoup.parse(loginresult);
        Elements alert = doc.select("script[language]");
        Elements success = doc.select("div#headDiv").select("ul.nav").select("a[href]");

        //先判断是否登录成功，若成功直接退出
        for (Element link : success) {
            //获取所要查询的URL,这里对应地址按钮的名字叫成绩查询
            if (link.text().equals("个人信息")) {
                Log.i("xyz", "登录成功");
                login = true;
                loginResult = loginresult;
                getPersonalInfo(loginresult);

                return;
            }
        }
        login = false;
        for (Element link : alert) {
            //刷新验证码
            getSecretCode();
            //获取错误信息
            if (link.data().contains("验证码不正确")) {
                Log.i("xyz", "验证码错误");
                EventBus.getDefault().post(
                        new LoginResponseCode(LoginResponseCode.LOGIN_VERFATION_ERROR));

            } else if (link.data().contains("用户名不能为空")) {
                Log.i("xyz", "用户名不能为空");
                EventBus.getDefault().post(
                        new LoginResponseCode(LoginResponseCode.LOGIN_USERNAME_ERROR));
            } else if (link.data().contains("密码错误")) {
                Log.i("xyz", "密码或用户名错误");
                EventBus.getDefault().post(
                        new LoginResponseCode(LoginResponseCode.LOGIN_USERNAME_OR_PASSWORD_ERROR));
            } else if (link.data().contains("密码不能为空")) {
                Log.i("xyz", "密码不能为空");
                EventBus.getDefault().post(
                        new LoginResponseCode(LoginResponseCode.LOGIN_PASSWORD_ERROR));
            }
        }
    }

    //得到个人信息
    public void getPersonalInfo(String result) {
        theUrls = new UrlsMap(null);
        Document doc = Jsoup.parse(result);
        Element menu = doc.select("div#headDiv").select("ul.nav").first();
        Elements list = menu.select("a");
        Map<String, String> Urls = new HashMap<>();
        for (Element element : list) {
            if ((!element.attr("href").contains("#a")) && (!element.attr("href").contains("#"))) {
                if (element.text() != "") {
                    Urls.put(element.text(), NJIT_ZF_HOST + element.attr("href"));
                } else if (element.select("span").size() > 0) {
                    Urls.put(element.select("span").text(), NJIT_ZF_HOST + element.attr("href"));
                }
            }
        }
        theUrls.setQueryUrl(Urls);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(theUrls.getPersonInfo());
                HttpResponse httpResponse;

                if (cookieStore == null) {

                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpGet.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpGet.addHeader("Referer", theUrls.getPersonInfo());
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpGet);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        StringBuffer sb = new StringBuffer();
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        //Log.e("test content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);
                        Element content = doc.select("div.main_box").first();

                        Element infoContent = content.select("span.formbox").select("table.formlist").first();

                        Elements infolist = infoContent.select("tr");
                        for (Element e : infolist) {
                            Log.e("info content", e.html());
                        }
                        //先找照片
                        Element ePic = infolist.select("img#xszp").first();
                        PersonInfo info = new PersonInfo();
                        info.setPersonZP(NJIT_ZF_HOST + ePic.attr("src"));
                        saveZP(info.getPersonZP());

                        String xh = infolist.select("span#xh").first().text();
                        info.setPersonXH(xh);
                        theUrls.setXh(info.getPersonXH());
                        info.setPersonXSZH(infolist.select("span#lbl_xszh").first().text());
                        info.setPersonSJLX(infolist.select("span#lbl_TELLX").first().text());
                        info.setPersonXM(infolist.select("span#xm").first().text());
                        info.setPersonPYFX(infolist.select("span#lbl_pyfx").first().text());
                        info.setPersonSJHM(infolist.select("span#lbl_TELNUMBER").first().text());
                        info.setPersonCYM(infolist.select("span#lbl_zym").first().text());
                        info.setPersonZYFX(infolist.select("span#lbl_zyfx").first().text());
                        info.setPersonJTYB(infolist.select("span#lbl_jtyb").first().text());
                        info.setPersonXB(infolist.select("span#lbl_xb").first().text());
                        info.setPersonRXRQ(infolist.select("span#lbl_rxrq").first().text());
                        info.setPersonJTDH(infolist.select("span#lbl_jtdh").first().text());
                        info.setPersonCSRQ(infolist.select("span#lbl_csrq").first().text());
                        info.setPersonBYZX(infolist.select("span#lbl_byzx").first().text());
                        info.setPersonFQXM(infolist.select("span#lbl_fqxm").first().text());
                        info.setPersonMZ(infolist.select("span#lbl_mz").first().text());
                        info.setPersonSSH(infolist.select("span#lbl_ssh").first().text());
                        info.setPersonFQDW(infolist.select("span#lbl_fqdw").first().text());
                        info.setPersonJG(infolist.select("span#lbl_jg").first().text());
                        info.setPersonDZYX(infolist.select("span#lbl_dzyxdz").first().text());
                        info.setPersonFQDWYX(infolist.select("span#lbl_fqdwyb").first().text());
                        info.setPersonZZMM(infolist.select("span#lbl_zzmm").first().text());
                        info.setPersonLXDH(infolist.select("span#lbl_lxdh").first().text());
                        info.setPersonMQXM(infolist.select("span#lbl_mqxm").first().text());
                        info.setPersonLYDQ(infolist.select("span#lbl_lydq").first().text());
                        info.setPersonYZBM(infolist.select("span#lbl_yzbm").first().text());
                        info.setPersonMQDW(infolist.select("span#lbl_mqdw").first().text());
                        info.setPersonLYS(infolist.select("span#lbl_lys").first().text());
                        info.setPersonZKZH(infolist.select("span#lbl_zkzh").first().text());
                        info.setPersonMQDWYB(infolist.select("span#lbl_mqdwyb").first().text());
                        info.setPersonCSD(infolist.select("span#lbl_csd").first().text());
                        info.setPersonSFZH(infolist.select("span#lbl_sfzh").first().text());
                        info.setPersonFQDWDHHSJ(infolist.select("span#lbl_fqdwdh").first().text());
                        info.setPersonJKZK(infolist.select("span#lbl_jkzk").first().text());
                        info.setPersonXLCC(infolist.select("span#lbl_CC").first().text());
                        info.setPersonMQDWDHHSJ(infolist.select("span#lbl_mqdwdh").first().text());
                        info.setPersonXY(infolist.select("span#lbl_xy").first().text());
                        info.setPersonGATM(infolist.select("span#lbl_gatm").first().text());
                        info.setPersonJTDZ(infolist.select("span#lbl_jtdz").first().text());
                        info.setPersonX(infolist.select("span#lbl_xi").first().text());
                        info.setPersonBDH(infolist.select("span#lbl_bdh").first().text());
                        info.setPersonJTSZD(infolist.select("span#lbl_jtszd").first().text());
                        info.setPersonZYMC(infolist.select("span#lbl_zymc").first().text());
                        info.setPersonSFGSPYDY(infolist.select("span#lbl_SFGSPYDY").first().text());
                        info.setPersonBZ(infolist.select("span#lbl_bz").first().text());
                        info.setPersonJXBMC(infolist.select("span#lbl_JXBMC").first().text());
                        info.setPersonYYDJ(infolist.select("span#lbl_yydj").first().text());
                        info.setPersonXZB(infolist.select("span#lbl_xzb").first().text());
                        info.setPersonYYCJ(infolist.select("span#lbl_YYCJ").first().text());
                        info.setPersonXZ(infolist.select("span#lbl_xz").first().text());
                        info.setPersonLJBYM(infolist.select("span#lbl_LJBYM").first().text());
                        info.setPersonXXNX(infolist.select("span#lbl_xxnx").first().text());
                        info.setPersonTC(infolist.select("span#lbl_tc").first().text());
                        info.setPersonXJZT(infolist.select("span#lbl_xjzt").first().text());
                        info.setPersonRTSJ(infolist.select("span#lbl_RDSJ").first().text());
                        info.setPersonDQSZJ(infolist.select("span#lbl_dqszj").first().text());
                        info.setPersonHCZDZ(infolist.select("span#lbl_ccqj").first().text());
                        // info.setPersonZMR(infolist.select("span#lbl_zmr").first().text());//这里可能出错
                        info.setPersonKSH(infolist.select("span#lbl_ksh").first().text());
                        info.setPersonXXXS(infolist.select("span#lbl_xxxs").first().text());
                        info.setPersonXMPY(infolist.select("span#lbl_xmpyo").first().text());

                        PersonInfoEvent event = new PersonInfoEvent();
                        event.setInfo(info);
                        EventBus.getDefault().post(event);
                        EventBus.getDefault().post(theUrls);
                        LoginResponseCode loginevent = new LoginResponseCode(LoginResponseCode.LOGIN_OK);
                        EventBus.getDefault().post(loginevent);

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

    private String btn;

    public void getScore(final String year, final String term) {
        try {
            btn = URLEncoder.encode(" 查  询 ", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = theUrls.getScoreUrl();
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(host);
                HttpResponse httpResponse;
                //设置post参数

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //params.add(new BasicNameValuePair("btnCx", "%E6%9F%A5%20%20%E8%AF%A2"));
                params.add(new BasicNameValuePair("btnCx", btn));
                params.add(new BasicNameValuePair("ddlxn", year));
                params.add(new BasicNameValuePair("ddlxq", term));
                params.add(new BasicNameValuePair("__VIEWSTATE", context.getResources().getString(R.string.score_viewstate)));
                params.add(new BasicNameValuePair("__EVENTTARGET", ""));
                params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
                if (cookieStore == null) {

                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpPost.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpPost.addHeader("Referer", host);
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpPost);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        StringBuffer sb = new StringBuffer();
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        Log.e("test content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);
                        Element content = doc.select("div.main_box").first();

                        Element scoreContent = content.select("table#DataGrid1.datelist").first();
                        Elements scorelist = scoreContent.select("tr");
                        Elements menu = scorelist.get(0).select("td");

                        List<Map<String, String>> scoreList = new ArrayList<Map<String, String>>();
                        for (int i = 1; i < scorelist.size(); i++) {
                            final Map<String, String> result = new HashMap<String, String>();
                            Element element = scorelist.get(i);
                            Elements data = element.select("td");
                            for (int j = 0; j < data.size(); j++) {
                                result.put(menu.get(j).text().trim(), data.get(j).text().trim());
                            }

                            Log.e("map", result.toString());
                            scoreList.add(result);
                        }
                        EventBus.getDefault().post(new Scores(theUrls.getXh(), year + ":" + term, scoreList));

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


    public void saveZP(final String picUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File appDir = new File(Environment.getExternalStorageDirectory(), "NJITStudent");
                    if (!appDir.exists()) {
                        appDir.mkdir();
                    }
                    String fileName = "xszp" + ".jpg";
                    File file = new File(appDir, fileName);
                    //new一个URL对象
                    URL url = new URL(picUrl);

                    //打开链接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setInstanceFollowRedirects(false);
                    //设置请求方式为"GET"
                    conn.setRequestMethod("GET");
                    //超时响应时间为5秒
                    conn.setConnectTimeout(5 * 1000);
                    //通过输入流获取图片数据
                    InputStream inStream = conn.getInputStream();

                    byte[] buffer = new byte[1024];
                    int len = -1;
                    FileOutputStream fos = new FileOutputStream(file);
                    while ((len = inStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    inStream.close();
                    fos.flush();
                    fos.close();


                } catch (MalformedURLException e) {

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private String kb;
    private String zyCode;
    private String firstViewstate = "";
    private String viewstate;


    //班级 学年 学期 年级 学院 专业
    public void getCourseForm(final String personXH, final String whichClass, final String year, final String term, final String nj, final String xy, final String zy) {
        //解析学号的ViewSTATE
        final Thread t0 = new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultHttpClient client = new DefaultHttpClient();
                //班级课表
                HttpPost httpPost = new HttpPost(theUrls.getCourseForm());
                HttpResponse httpResponse;
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                if (cookieStore == null) {
                    return;
                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpPost.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpPost.addHeader("Referer", theUrls.getCourseForm());
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpPost);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        StringBuffer sb = new StringBuffer();
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);

                        Document doc = Jsoup.parse(MAINBODYHTML);
                        Element content = doc.select("form#Form1").first();
                        Log.e("get", content.html());
                        Elements input = content.select("input");
                        for (Element e : input) {
                            if (e.attr("name").equals("__VIEWSTATE")) {
                                firstViewstate = e.attr("value").toString();

                            }
                            Log.e("get first viewstate", firstViewstate);
                        }

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

        );

        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t0.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DefaultHttpClient client = new DefaultHttpClient();
                //班级课表
                HttpPost httpPost = new HttpPost(theUrls.getCourseForm());
                HttpResponse httpResponse;
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("xn", year));
                params.add(new BasicNameValuePair("xq", term));
                params.add(new BasicNameValuePair("nj", nj));
                params.add(new BasicNameValuePair("xy", xy));
                params.add(new BasicNameValuePair("__EVENTTARGET", "zy"));
                params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
                params.add(new BasicNameValuePair("__VIEWSTATE", firstViewstate));
                if (cookieStore == null) {
                    return;
                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpPost.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpPost.addHeader("Referer", theUrls.getCourseForm());
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpPost);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        StringBuffer sb = new StringBuffer();
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);
                        Log.e("content", MAINBODYHTML);
                        Document doc = Jsoup.parse(MAINBODYHTML);
                        Element form = doc.select("form#Form1").first();
                        Elements input = form.select("input");
                        for (Element e : input) {
                            if (e.attr("name").equals("__VIEWSTATE")) {
                                viewstate = e.attr("value").toString();
                            }
                            Log.e("get viewstate", e.html());
                        }
                        Element content = doc.select("div.toolbox").first();
                        if (content != null) {
                            Elements zy_code = content.select("table#Table1").select("select#zy").select("option");
                            for (Element e : zy_code) {
                                if (e.text().toString().trim().equals(zy)) {
                                    zyCode = e.attr("value").toString();
                                }
                                Log.e("get zy code", e.html());
                            }

                            Elements tjkb = content.select("table#Table1").select("select#kb").select("option");
                            for (Element e : tjkb) {
                                if (e.text().contains(whichClass)) {
                                    kb = e.attr("value").toString();
                                }
                                Log.e("get kb", e.html());
                            }
                        }

                        Log.e("get something", "do here");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t0.join();
                    t1.join();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DefaultHttpClient client = new DefaultHttpClient();
                //班级课表
                HttpPost httpPost = new HttpPost(theUrls.getCourseForm());
                HttpResponse httpResponse;
                //设置post参数

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("xn", year));
                params.add(new BasicNameValuePair("xq", term));
                params.add(new BasicNameValuePair("nj", nj));
                params.add(new BasicNameValuePair("xy", xy));
                params.add(new BasicNameValuePair("zy", zyCode));
                params.add(new BasicNameValuePair("kb", kb));//201401072016-201712011402
                params.add(new BasicNameValuePair("__EVENTTARGET", "zy"));
                params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
                //params.add(new BasicNameValuePair("__VIEWSTATE", context.getResources().getString(R.string.course_viewstate)));
                params.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
                if (cookieStore == null) {
                    return;
                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpPost.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpPost.addHeader("Referer", theUrls.getCourseForm());
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpPost);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        StringBuffer sb = new StringBuffer();
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);

                        Document doc = Jsoup.parse(MAINBODYHTML);

                        //课表
                        Element content = doc.select("div.main_box").first();
                        //Log.e("test content", content.html());
                        if (content != null) {
                            Element allForm = content.select("span.formbox").first();
                            Element formKB = allForm.select("table#Table6.blacktab").first();//课表
                            List<FormKB> formKBs = createTable(formKB);

                            List<FormSJK> formSJKs = null;
                            //实践课信息
                            Element formSX = allForm.select("table#Table3").select("table#DataGrid1.datelist").first();//实习
                            if (formSX != null) {
                                Elements SXresults = formSX.select("tr");
                                formSJKs = new ArrayList<>();
                                for (int i = 1; i < SXresults.size(); i++) {
                                    FormSJK formSJK = new FormSJK();
                                    Elements td = SXresults.get(i).select("td");
                                    formSJK.setCourseName(td.get(0).text());
                                    formSJK.setTeacher(td.get(1).text());
                                    formSJK.setCredit(td.get(2).text());
                                    formSJK.setTime(td.get(3).text());
                                    formSJK.setStudyTime(td.get(4).text());
                                    formSJK.setStudyPlace(td.get(5).text());
                                    formSJKs.add(formSJK);
                                }
                            }
                            //调课信息
                            Element formTTB = null;
                            List<FormTTBinfo> formTTBinfos = null;
                            formTTB = allForm.select("table#DBGrid.datelist").first();//调、停、补课信息
                            if (formTTB != null) {
                                //Log.e("test content", formTTB.html() + "");
                                Elements ttbTr = formTTB.select("tr");
                                formTTBinfos = new ArrayList<>();
                                for (int i = 1; i < ttbTr.size(); i++) {
                                    FormTTBinfo formTTBinfo = new FormTTBinfo();
                                    Elements ttbTd = ttbTr.get(i).select("td");
                                    formTTBinfo.setId(ttbTd.get(0).text());
                                    formTTBinfo.setCourseName(ttbTd.get(1).text());
                                    formTTBinfo.setPreStudyState(ttbTd.get(2).text());
                                    formTTBinfo.setNowStudyState(ttbTd.get(3).text());
                                    formTTBinfo.setPostTime(ttbTd.get(4).text());
                                    formTTBinfos.add(formTTBinfo);
                                }
                            }

                            Log.e("query term", year + ":" + term);
                            if (formKBs.size() == 9 * 13) {
                                EventBus.getDefault().post(new CourseEvent(personXH, year + ":" + term, "class", formKBs, formSJKs, formTTBinfos));
                                SettingsUtil.setUserCourseType("class");
                                SettingsUtil.setUserCourseTerm(year + ":" + term);
                                SettingsUtil.setCurrentWeek("1");
                                SettingsUtil.setCourseStartWeek("1");
                                SettingsUtil.setCourseCurrentWeekBindTime(NJITCourseFragment.getCurrentTime());
                            }
//                            }else {
//                                getPersonCourseForm(SettingsUtil.getXueHao(),"","");
//                            }
                        }


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

        );
        t0.start();
        t1.start();
        t2.start();
    }

    String firstPersonViewstate = "";

    //学年 学期
    public void getPersonCourseForm(final String personXH, final String year, final String term) {
        //解析学号的ViewSTATE
        final Thread t0 = new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultHttpClient client = new DefaultHttpClient();
                //班级课表
                HttpPost httpPost = new HttpPost(theUrls.getPersonCourse());
                HttpResponse httpResponse;

                if (cookieStore == null) {
                    return;
                } else {
                    client.setCookieStore(cookieStore);
                }
                try {

                    httpPost.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpPost.addHeader("Referer", theUrls.getPersonCourse());
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpPost);


                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {

                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);

                        Document doc = Jsoup.parse(MAINBODYHTML);
                        Element content = doc.select("form#xskb_form").first();
                        Log.e("get", content.html());
                        Elements input = content.select("input");
                        for (Element e : input) {
                            if (e.attr("name").equals("__VIEWSTATE")) {
                                firstPersonViewstate = e.attr("value").toString();
                            }
                        }

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

        );


        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t0.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(theUrls.getPersonCourse());
                HttpResponse httpResponse;
                //设置post参数

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("xnd", year));
                params.add(new BasicNameValuePair("xqd", term));
                params.add(new BasicNameValuePair("__EVENTTARGET", "xqd"));
                params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
                params.add(new BasicNameValuePair("__VIEWSTATE", firstPersonViewstate));
                if (cookieStore == null) {
                    return;
                } else {
                    client.setCookieStore(cookieStore);
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpPost.addHeader("User-Agent", USERAGENT_DESKTOP);
                    httpPost.addHeader("Referer", theUrls.getPersonCourse());
                    client.getParams().setParameter("http.protocol.allow-circular-redirects", false);
                    httpResponse = client.execute(httpPost);
                    Log.i("xyz", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        String MAINBODYHTML = EntityUtils.toString(entity);

                        Document doc = Jsoup.parse(MAINBODYHTML);
                        //课表
                        Element content = doc.select("div.main_box").first();
                        //Log.e("test content", content.html());
                        if (content != null) {
                            Element allForm = content.select("span.formbox").first();
                            Log.e("info",allForm.select("table#Table2").first().html());
                            Element formKB = allForm.select("table#Table1.blacktab").first();//课表
                            Log.e("formKB", formKB.html());
                            List<FormKB> formKBs = createTable(formKB);

                            List<FormSJK> formSJKs = null;
                            //实践课信息
                            Element formSX = allForm.select("table#Table3").select("table#DataGrid1.datelist").first();//实习
                            if (formSX != null) {
                                Elements SXresults = formSX.select("tr");
                                formSJKs = new ArrayList<>();
                                for (int i = 1; i < SXresults.size(); i++) {
                                    FormSJK formSJK = new FormSJK();
                                    Elements td = SXresults.get(i).select("td");
                                    formSJK.setCourseName(td.get(0).text());
                                    formSJK.setTeacher(td.get(1).text());
                                    formSJK.setCredit(td.get(2).text());
                                    formSJK.setTime(td.get(3).text());
                                    formSJK.setStudyTime(td.get(4).text());
                                    formSJK.setStudyPlace(td.get(5).text());
                                    formSJKs.add(formSJK);
                                }
                            }

                            //调课信息
                            Element formTTB = null;
                            List<FormTTBinfo> formTTBinfos = null;
                            formTTB = allForm.select("table#DBGrid.datelist").first();//调、停、补课信息
                            if (formTTB != null) {
                                //Log.e("test content", formTTB.html() + "");
                                Elements ttbTr = formTTB.select("tr");
                                formTTBinfos = new ArrayList<>();
                                for (int i = 1; i < ttbTr.size(); i++) {
                                    FormTTBinfo formTTBinfo = new FormTTBinfo();
                                    Elements ttbTd = ttbTr.get(i).select("td");
                                    formTTBinfo.setId(ttbTd.get(0).text());
                                    formTTBinfo.setCourseName(ttbTd.get(1).text());
                                    formTTBinfo.setPreStudyState(ttbTd.get(2).text());
                                    formTTBinfo.setNowStudyState(ttbTd.get(3).text());
                                    formTTBinfo.setPostTime(ttbTd.get(4).text());
                                    formTTBinfos.add(formTTBinfo);
                                }
                            }

                            Log.e("query term", year + ":" + term);
                            if (formKBs.size() == 9 * 13) {
                                EventBus.getDefault().post(new CourseEvent(personXH, year + ":" + term, "person", formKBs, formSJKs, formTTBinfos));
                                SettingsUtil.setUserCourseType("person");
                                SettingsUtil.setUserCourseTerm(year + ":" + term);
                                SettingsUtil.setCurrentWeek("1");
                                SettingsUtil.setCourseStartWeek("1");
                                SettingsUtil.setCourseCurrentWeekBindTime(NJITCourseFragment.getCurrentTime());
                            }
                        }


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

        );
        t0.start();
        t1.start();
    }

    //提取课表表格
    private List<FormKB> createTable(Element form) {
        //表格13行 9列

        Elements formKBList = form.select("tr");

        List<FormKB> kblist = new ArrayList<>();

        for (int n = 0; n < formKBList.size(); n++) {
            Element e = formKBList.get(n);//找出一行数据
            Log.e("course ", e.html());
            Elements rowsList = e.select("td");//解析一行内的数据
            int m = 0;
            for (int i = 0; i < rowsList.size(); i++, m++) {

                Element element = rowsList.get(i);
                String colspan = "";
                if (!element.attr("colspan").toString().equals("")) {
                    colspan = element.attr("colspan");//元素跨列数
                }
                String rowspan = "";
                if (!element.attr("rowspan").toString().equals("")) {
                    rowspan = element.attr("rowspan");//元素跨行数
                }
                String text = element.text();//元素内容
                int row = 1;
                int col = 1;
                try {
                    if (rowspan != "") {
                        row = Integer.parseInt(rowspan);
                    }
                    if (colspan != "") {
                        col = Integer.parseInt(colspan);
                    }
                } catch (NumberFormatException error) {
                    //error.printStackTrace();
                }

                for (int j = 0; j < col; j++) {
                    m = m + j;
                    for (int k = 0; k < row; k++) {
                        if (!isAdded(kblist, m, n + k)) {
                            Log.e("add course", (n + k) + ":" + m + ":" + text);
                            kblist.add(new FormKB(m, n + k, row, text));
                        } else {
                            while (m < 9) {//找寻没有保存过的位置存储信息
                                ++m;
                                if (!isAdded(kblist, m, n)) {
                                    Log.e("add course", n + ":" + m + ":" + text);
                                    kblist.add(new FormKB(m, n, 1, text));
                                    break;
                                }
                            }
                        }
                    }

                }
//                if (colspan.isEmpty() || colspan == "" || colspan == null || colspan.equals("")) {//不跨列
//                    if (rowspan.isEmpty() || rowspan == "" || rowspan == null || rowspan.equals("")) {//不跨行
//                        if (!isAdded(kblist, m, n)) {//没有保存过
//                            Log.e("add course", n + ":" + m + ":" + text);
//                            kblist.add(new FormKB(m, n, 1, text));//添加元素
//                        } else {//已经保存过
//                            while (m < 9) {//找寻没有保存过的位置存储信息
//                                ++m;
//                                if (!isAdded(kblist, m, n)) {
//                                    Log.e("add course", n + ":" + m + ":" + text);
//                                    kblist.add(new FormKB(m, n, 1, text));
//                                    break;
//                                }
//                            }
//                        }
//                    } else {//存在跨行情况
//                        for (int j = 0; j < row; j++) {
//                            if (!isAdded(kblist, m, n + j)) {
//                                Log.e("add course", (n + j) + ":" + m + ":" + text);
//                                kblist.add(new FormKB(m, n + j, row, text));
//                            } else {
//                                while (m < 9) {
//                                    ++m;
//                                    if (!isAdded(kblist, m, n)) {
//                                        Log.e("add course", (n + j) + ":" + m + ":" + text);
//                                        kblist.add(new FormKB(m, n + j, row, text));
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                } else {//存在跨列
//                    for (int j = 0; j < col; j++) {
//                        m = m + j;
//                        Log.e("add ??", n + ":" + m + ":" + text);
//                        if (rowspan == null || rowspan.isEmpty() || rowspan == "" || rowspan.equals("")) {//不跨行
//                            Log.e("add ?", n + ":" + m + ":" + text);
//                            if (!isAdded(kblist, m, n)) {
//                                Log.e("add course", n + ":" + m + ":" + text);
//                                kblist.add(new FormKB(m, n, 1, text));
//                            }
//                        } else {//跨行
//                            for (int k = 0; k < row; k++) {
//                                if (!isAdded(kblist, m, n + k)) {
//                                    Log.e("add course", (n + k) + ":" + m + ":" + text);
//                                    kblist.add(new FormKB(m, n + k, row, text));
//                                }
//                            }
//                        }
//                    }
//                }


            }

        }

        return kblist;
    }

    private boolean isAdded(List<FormKB> list, int col, int row) {
        for (int i = 0; i < list.size(); i++) {
            FormKB formKB = list.get(i);
            int rowspan = formKB.getRowspan();
            int colspan = formKB.getColspan();

            if (colspan == col && rowspan == row) {
                //Log.e("is added ", rowspan + ":" + colspan + "\n" + row + ":" + col + "\n" + formKB.toString());
                return true;
            }
        }
        return false;
    }

}
