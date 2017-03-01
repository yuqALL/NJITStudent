package com.njit.student.yuqzy.njitstudent;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.njit.student.yuqzy.njitstudent.Event.CourseEvent;
import com.njit.student.yuqzy.njitstudent.Event.InfoDoorResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.LibraryResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.LibrarySecretCode;
import com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.PersonInfoEvent;
import com.njit.student.yuqzy.njitstudent.Event.Scores;
import com.njit.student.yuqzy.njitstudent.Event.SecretCode;
import com.njit.student.yuqzy.njitstudent.Event.ThemeChangedEvent;
import com.njit.student.yuqzy.njitstudent.Event.UrlsMap;
import com.njit.student.yuqzy.njitstudent.database.CourseDatabase;
import com.njit.student.yuqzy.njitstudent.database.formKBdatabase;
import com.njit.student.yuqzy.njitstudent.database.formSJKdatabase;
import com.njit.student.yuqzy.njitstudent.database.formTTBinfoDatabase;
import com.njit.student.yuqzy.njitstudent.database.PersonInfo;
import com.njit.student.yuqzy.njitstudent.database.PersonScore;
import com.njit.student.yuqzy.njitstudent.database.PersonUrls;
import com.njit.student.yuqzy.njitstudent.database.Url;
import com.njit.student.yuqzy.njitstudent.model.FormKB;
import com.njit.student.yuqzy.njitstudent.model.FormSJK;
import com.njit.student.yuqzy.njitstudent.model.FormTTBinfo;
import com.njit.student.yuqzy.njitstudent.model.ScoreList;
import com.njit.student.yuqzy.njitstudent.database.ScoreData;
import com.njit.student.yuqzy.njitstudent.model.BaseLoginCookies;
import com.njit.student.yuqzy.njitstudent.net.NetWork;
import com.njit.student.yuqzy.njitstudent.net.ZfNetData;
import com.njit.student.yuqzy.njitstudent.ui.info.about.AboutActivity;
import com.njit.student.yuqzy.njitstudent.ui.info.course.NJITCourseFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.library.LibraryFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.news.EduNotificationFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.more.MoreInfoFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.library.ReadingFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.news.SchoolNewsFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.socre.ScoreFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.setting.SettingActivity;
import com.njit.student.yuqzy.njitstudent.utils.DoubleClickExit;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


import static com.njit.student.yuqzy.njitstudent.AppGlobal.BASE_LOGIN_HOST;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.BASE_LOGIN_HOST_KEY;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.USERAGENT;
import static com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode.REALM_COURSE_STO_OK;
import static com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode.REALM_PERSON_STO_OK;
import static com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode.REALM_SCORE_STO_OK;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager;
    private String currentFragmentTag;
    private Subscription subscription;
    private Realm realm;
    public static ZfNetData network;
    private Dialog dialog;
    private ImageView imgHead;


    //抽屉主菜单
    public static final String FRAGMENT_TAG_EDUNOTIFICATION = "education notification";//教务通知
    public static final String FRAGMENT_TAG_QUERY_SCHEDULE = "query shcedule";//课表查询
    public static final String FRAGMENT_TAG_SCORE_INQUIRY = "score inquiry";//成绩查询
    public static final String FRAGMENT_TAG_SCHOOL_NEW = "school news";//校园新闻
    public static final String FRAGMENT_TAG_MY_LIBRARY = "my library";//我的图书馆
    public static final String FRAGMENT_TAG_MORE = "more";//更多

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppGlobal.CURRENT_INDEX, currentFragmentTag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        network = new ZfNetData(this);
        fragmentManager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        imgHead = (ImageView) view.findViewById(R.id.img_head);
        imgHead.setOnClickListener(this);
        view.findViewById(R.id.tv_contact_me).setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        initFragment(savedInstanceState);

    }


    private void loginNet(final String name, final String pwd) {
        subscription = Observable.just(BASE_LOGIN_HOST).subscribeOn(Schedulers.io()).map(new Func1<String, BaseLoginCookies>() {
            @Override
            public BaseLoginCookies call(String host) {
                BaseLoginCookies cookies = new BaseLoginCookies();
                cookies.setUsername(name);
                cookies.setPassword(pwd);
                try {

                    //第一次获取key
                    Connection.Response res = Jsoup.connect(host)
                            .method(Connection.Method.POST)
                            .execute();
                    Log.e("first response", res.cookies().toString());


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
                    connection.data("Login.Token2", pwd);
                    connection.data("goto", "http://my.njit.edu.cn/loginSuccess.portal");
                    connection.data("gotoOnFail", "http://my.njit.edu.cn/loginFailure.portal");
                    connection.cookie("JSESSIONID", res.cookie("JSESSIONID"));
                    connection.method(Connection.Method.POST);
                    Connection.Response re = connection.execute();

                    Log.e("second cookies", re.cookies().toString());


                    //http://jwc.njit.edu.cn/lm_list.jsp?urltype=tree.TreeTempUrl&wbtreeid=1035
                    //http://jwc.njit.edu.cn/lm_list.jsp?totalpage=59&PAGENUM=2&urltype=tree.TreeTempUrl&wbtreeid=1035
                    //得到数据
                    Document objectDoc = Jsoup.connect(host)
                            .cookies(re.cookies())
                            .get();
                    Log.e("d2", "" + objectDoc.html());

                    cookies.setiPlanetDirectoryPro(re.cookie("iPlanetDirectoryPro"));


                } catch (IOException e) {

                }
                return cookies;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<BaseLoginCookies>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseLoginCookies cookies) {
                Toast.makeText(getApplicationContext(), cookies.getiPlanetDirectoryPro(), Toast.LENGTH_SHORT).show();
                if (cookies.getiPlanetDirectoryPro() == "" || cookies.getiPlanetDirectoryPro() == null) {
                    //LoginDialog();
                }
            }
        });
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            switchContent(SettingsUtil.getViewMain());
        } else {
            currentFragmentTag = savedInstanceState.getString(AppGlobal.CURRENT_INDEX);
            switchContent(currentFragmentTag);
        }
    }

    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            mDrawerToggle.syncState();
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!DoubleClickExit.check()) {
                Snackbar.make(MainActivity.this.getWindow().getDecorView().findViewById(android.R.id.content), "再按一次退出 App!", Snackbar.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String[] getCurrentTerm() {
        String[] term = new String[2];
        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month;
        if (month < 3) {
            term[0] = (year - 1) + "-" + year;
            term[1] = "1";
        } else if (month >= 3 && month < 9) {
            term[0] = (year - 1) + "-" + year;
            term[1] = "2";
        } else if (month >= 9) {
            term[0] = year + "-" + (year + 1);
            term[1] = "1";
        }
        Log.e("term", term[0] + ":" + term[1]);
        return term;
    }

    public static String[] getPreTerm() {
        String[] term = new String[2];
        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month;
        if (month < 3) {
            term[0] = (year - 2) + "-" + (year - 1);
            term[1] = "2";
        } else if (month >= 3 && month < 9) {
            term[0] = (year - 1) + "-" + year;
            term[1] = "1";
        } else if (month >= 9) {
            term[0] = (year - 1) + "-" + year;
            term[1] = "2";
        }
        Log.e("term", term[0] + ":" + term[1]);
        return term;
    }

    private MenuItem item;
    private String where;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        this.item = item;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edu_notification) {
            item.setChecked(true);
            switchContent(FRAGMENT_TAG_EDUNOTIFICATION);
        } else if (id == R.id.nav_school_news) {
            item.setChecked(true);
            switchContent(FRAGMENT_TAG_SCHOOL_NEW);
        } else if (id == R.id.nav_query_grade) {
            //item.setChecked(true);
            switchContent(FRAGMENT_TAG_SCORE_INQUIRY);
        } else if (id == R.id.nav_query_schedule) {
            //item.setChecked(true);
            switchContent(FRAGMENT_TAG_QUERY_SCHEDULE);
        } else if (id == R.id.nav_my_library) {
            item.setChecked(true);
            switchContent(FRAGMENT_TAG_MY_LIBRARY);
        } else if (id == R.id.nav_more) {
            item.setChecked(true);
            switchContent(FRAGMENT_TAG_MORE);
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchContent(String name) {

        if (currentFragmentTag != null && currentFragmentTag.equals(name))
            return;

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }

        Fragment foundFragment = fragmentManager.findFragmentByTag(name);

        if (foundFragment == null) {
            switch (name) {
                case FRAGMENT_TAG_EDUNOTIFICATION:
                    foundFragment = new EduNotificationFragment();
                    break;
                case FRAGMENT_TAG_SCHOOL_NEW:
                    foundFragment = new SchoolNewsFragment();
                    break;
                case FRAGMENT_TAG_SCORE_INQUIRY:
                    String[] currentTerm = getPreTerm();
                    List<PersonScore> list = null;
                    if (SettingsUtil.getXueHao() != "") {
                        if (SettingsUtil.getUserScoreTerm() != "") {
                            list = getScoreFromDatabase(SettingsUtil.getXueHao(), SettingsUtil.getUserScoreTerm());
                        } else {
                            list = getScoreFromDatabase(SettingsUtil.getXueHao(), currentTerm[0] + ":" + currentTerm[1]);
                            SettingsUtil.setUserScoreTerm(currentTerm[0] + ":" + currentTerm[1]);
                        }
                    }

                    if (list != null && list.size() > 0) {
                        Bundle bundle = new Bundle();
                        List<ScoreData> scoreDatas = new ArrayList<>();
                        for (PersonScore score : list) {
                            for (ScoreData scoreData : score.getScoreDatas()) {
                                scoreDatas.add(scoreData);
                            }
                        }
                        bundle.putSerializable("data", new ScoreList(scoreDatas, list.get(0).getPersonXH(), list.get(0).getYearAterm()));
                        foundFragment = new ScoreFragment();
                        foundFragment.setArguments(bundle);

                    } else {
                        if (network.cookieStore != null) {

                            where = FRAGMENT_TAG_SCORE_INQUIRY;
                            if (SettingsUtil.getUserScoreTerm() != "") {
                                String[] info = SettingsUtil.getUserScoreTerm().split(":");
                                network.getScore(info[0], info[1]);
                            } else {
                                network.getScore(currentTerm[0], currentTerm[1]);
                                SettingsUtil.setUserScoreTerm(currentTerm[0] + ":" + currentTerm[1]);
                            }

                            return;
                        } else {

                            where = FRAGMENT_TAG_SCORE_INQUIRY;
                            dialog = loginZfDialog("请重新登录");
                            dialog.show();
                            return;
                        }
                    }
                    break;
                case FRAGMENT_TAG_QUERY_SCHEDULE:
                    String[] currentTerm2 = getCurrentTerm();
                    CourseDatabase courseDatabase = null;
                    if (SettingsUtil.getXueHao() != "") {
                        if (SettingsUtil.getUserCourseTerm() != "") {
                            courseDatabase = getCourseFromDatabase(SettingsUtil.getXueHao(), SettingsUtil.getUserCourseTerm(), SettingsUtil.getUserCourseType());
                        } else {
                            courseDatabase = getCourseFromDatabase(SettingsUtil.getXueHao(), currentTerm2[0] + ":" + currentTerm2[1], SettingsUtil.getUserCourseType());
                            SettingsUtil.setUserCourseTerm(currentTerm2[0] + ":" + currentTerm2[1]);
                        }
                    }
                    if (courseDatabase != null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("course", courseDatabase);
                        foundFragment = new NJITCourseFragment();
                        foundFragment.setArguments(bundle);
                    } else {
                        if (network.cookieStore != null) {
                            if (personInfo != null) {
                                where = FRAGMENT_TAG_QUERY_SCHEDULE;
                                Log.e("main activity", "course query");
                                String[] xy_name = getResources().getStringArray(R.array.xy_name);
                                String[] xy_value = getResources().getStringArray(R.array.xy_name);
                                String personXY = personInfo.getPersonXY();
                                int key = 0;
                                for (int i = 0; i < xy_name.length; i++) {
                                    if (xy_name[i].equals(personXY)) {
                                        key = i;
                                        break;
                                    }
                                }
                                if (SettingsUtil.getUserCourseTerm() != "") {
                                    String[] info = SettingsUtil.getUserCourseTerm().split(":");
                                    if (SettingsUtil.getUserCourseType().equals("class")) {
                                        getBJCourse(network, personInfo, info);
                                    } else {
                                        network.getPersonCourseForm(personInfo.getPersonXH(), info[0],
                                                info[1]);
                                    }

                                } else {

                                    if (SettingsUtil.getUserCourseType().equals("class")) {
                                        getBJCourse(network, personInfo, currentTerm2);
                                    } else {
                                        network.getPersonCourseForm(personInfo.getPersonXH(), currentTerm2[0],
                                                currentTerm2[1]);
                                    }

                                }

                                return;
                            } else {
                                if (SettingsUtil.getXueHao() != "") {
                                    personInfo = getPersonInfoFromDatabase(SettingsUtil.getXueHao());
                                }
                                if (personInfo != null) {
                                    where = FRAGMENT_TAG_QUERY_SCHEDULE;
                                    String[] xy_name = getResources().getStringArray(R.array.xy_name);
                                    String[] xy_value = getResources().getStringArray(R.array.xy_name);
                                    String personXY = personInfo.getPersonXY();
                                    int key = 0;
                                    for (int i = 0; i < xy_name.length; i++) {
                                        if (xy_name[i].equals(personXY)) {
                                            key = i;
                                            break;
                                        }
                                    }
                                    if (SettingsUtil.getUserCourseTerm() != "") {
                                        String[] info = SettingsUtil.getUserCourseTerm().split(":");
                                        if (SettingsUtil.getUserCourseType().equals("class")) {
                                            getBJCourse(network, personInfo, info);
                                        } else {
                                            network.getPersonCourseForm(personInfo.getPersonXH(), info[0], info[1]);
                                        }

                                    } else {
                                        String[] info = SettingsUtil.getUserCourseTerm().split(":");
                                        if (SettingsUtil.getUserCourseType().equals("class")) {
                                            getBJCourse(network, personInfo, currentTerm2);
                                        } else {
                                            network.getPersonCourseForm(personInfo.getPersonXH(), currentTerm2[0],
                                                    currentTerm2[1]);
                                        }

                                    }
                                    return;

                                } else {
                                    where = FRAGMENT_TAG_QUERY_SCHEDULE;
                                    Toast.makeText(this, "数据库出错！", Toast.LENGTH_SHORT).show();
                                    where = FRAGMENT_TAG_QUERY_SCHEDULE;
                                    dialog = loginZfDialog("教务网登录");
                                    dialog.show();
                                    return;
                                }
                            }
                        } else {
                            where = FRAGMENT_TAG_QUERY_SCHEDULE;
                            dialog = loginZfDialog("教务网登录");
                            dialog.show();
                            return;
                        }
                    }

                    break;
                case FRAGMENT_TAG_MY_LIBRARY:

                    foundFragment = new LibraryFragment();
                    break;
                case FRAGMENT_TAG_MORE:

                    foundFragment = new MoreInfoFragment();
                    break;

            }
        }

        if (foundFragment == null) {

        } else if (foundFragment.isAdded()) {
            ft.show(foundFragment);
        } else {
            ft.add(R.id.content_main, foundFragment, name);
        }
        ft.commit();
        currentFragmentTag = name;
        invalidateOptionsMenu();
    }

    //从网络获取课表
    private void getBJCourse(ZfNetData network, PersonInfo personInfo, String[] chooseTerm) {
        String[] xy_name = getResources().getStringArray(R.array.xy_name);
        String[] xy_value = getResources().getStringArray(R.array.xy_name);
        int key = 0;
        String personXY = personInfo.getPersonXY();
        for (int i = 0; i < xy_name.length; i++) {
            if (xy_name[i].equals(personXY)) {
                key = i;
                break;
            }
        }
        network.getCourseForm(personInfo.getPersonXH(),
                personInfo.getPersonXZB(),
                chooseTerm[0], chooseTerm[1],
                personInfo.getPersonDQSZJ(),
                xy_value[key],
                personInfo.getPersonZYMC());// 0107 0501;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Scores event) {
        final List<Map<String, String>> scores = event.getScores();

        RealmQuery<PersonScore> query = realm.where(PersonScore.class);

        final RealmResults<PersonScore> results = query.equalTo("personXH", event.getXH())
                .equalTo("yearAterm", event.getYearAterm()).findAll();
        Log.e("event add score", results.size() + "");
        if (results.size() <= 0) {
            addScores2Realm(realm, scores, event.getXH(), event.getYearAterm());

        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                }
            });
            addScores2Realm(realm, scores, event.getXH(), event.getYearAterm());

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SecretCode event) {
        zf_login_yanzhengma.setImageBitmap(event.getBitmap());
    }



    public static PersonInfo personInfo;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PersonInfoEvent event) {
        personInfo = event.getInfo();

        RealmQuery<PersonInfo> query = realm.where(PersonInfo.class);

        final RealmResults<PersonInfo> results = query.equalTo("personXH", personInfo.getPersonXH()).findAll();
        if (results.size() <= 0) {
            addPersonInfo2Realm(realm, personInfo);
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                }
            });
            addPersonInfo2Realm(realm, personInfo);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UrlsMap event) {
        String personXH = event.getXh();
        RealmQuery<PersonUrls> query = realm.where(PersonUrls.class);
        final RealmResults<PersonUrls> results = query.equalTo("personXH", personXH).findAll();

        if (results.size() <= 0) {
            addPersonUrls2Realm(realm, event);
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                }
            });
            addPersonUrls2Realm(realm, event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CourseEvent event) {
        String personXH = event.getPersonXH();
        String yearAterm = event.getYearAterm();
        String type = event.getType();

        RealmQuery<CourseDatabase> query = realm.where(CourseDatabase.class);
        final RealmResults<CourseDatabase> results = query.equalTo("personXH", personXH).equalTo("yearAterm", yearAterm).equalTo("type", type).findAll();

        if (results.size() <= 0) {
            addCourse2Realm(realm, event);
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                }
            });
            addCourse2Realm(realm, event);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginResponseCode event) {

        switch (event.getCode()) {
            case LoginResponseCode.LOGIN_OK:
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (FRAGMENT_TAG_QUERY_SCHEDULE.equals(where)) {
                    String[] xy_name = getResources().getStringArray(R.array.xy_name);
                    String[] xy_value = getResources().getStringArray(R.array.xy_name);
                    String personXY = personInfo.getPersonXY();
                    int key = 0;
                    for (int i = 0; i < xy_name.length; i++) {
                        if (xy_name[i].equals(personXY)) {
                            key = i;
                            break;
                        }
                    }
                    String[] value = getCurrentTerm();
                    network.getCourseForm(personInfo.getPersonXH(), personInfo.getPersonXZB(), value[0], value[1], personInfo.getPersonDQSZJ(), xy_value[key], personInfo.getPersonZYMC());
                } else if (FRAGMENT_TAG_SCORE_INQUIRY.equals(where)) {
                    String[] value = getPreTerm();
                    network.getScore(value[0], value[1]);
                    Log.e("LOGIN_OK", "getScore");
                }
                break;
            case LoginResponseCode.LOGIN_VERFATION_ERROR:
                et_zf_login_yanzhengma.setText("");
                Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_USERNAME_ERROR:
                et_zf_login_yanzhengma.setText("");
                Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_PASSWORD_ERROR:
                et_zf_login_yanzhengma.setText("");
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_USERNAME_OR_PASSWORD_ERROR:
                et_zf_login_yanzhengma.setText("");
                Toast.makeText(this, "密码或用户名错误", Toast.LENGTH_SHORT).show();
                break;
            case REALM_SCORE_STO_OK:
                if (!where.equals(currentFragmentTag) && where.equals(FRAGMENT_TAG_SCORE_INQUIRY)) {

                    item.setChecked(true);
                    switchContent(FRAGMENT_TAG_SCORE_INQUIRY);
                }
                break;
            case REALM_PERSON_STO_OK:
                break;
            case REALM_COURSE_STO_OK:
                if (where != null && !where.equals(currentFragmentTag) && where.equals(FRAGMENT_TAG_QUERY_SCHEDULE)) {

                    item.setChecked(true);
                    switchContent(FRAGMENT_TAG_QUERY_SCHEDULE);

                }
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(InfoDoorResponseCode event) {

        switch (event.getCode()) {
            case InfoDoorResponseCode.LOGIN_OK:
                Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();

                break;
            case InfoDoorResponseCode.LOGIN_PASSWORD_OR_PASSWORD_NULL:
                NetWork.LoginDialog(this);
                Toast.makeText(this, "密码或用户名为空", Toast.LENGTH_SHORT).show();
                break;
            case InfoDoorResponseCode.LOGIN_USERNAME_OR_PASSWORD_ERROR:
                NetWork.LoginDialog(this);
                Toast.makeText(this, "密码或用户名错误", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void addScores2Realm(final Realm realm, final List<Map<String, String>> scores, final String xh, final String yearAterm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                PersonScore personScore = bgRealm.createObject(PersonScore.class);
                RealmList<ScoreData> scoreDatas = new RealmList<ScoreData>();
                for (Map<String, String> map : scores) {
                    scoreDatas.add(scoreMap2ScoreData(bgRealm, map));
                }
                personScore.setScoreDatas(scoreDatas);
                personScore.setPersonXH(xh);
                personScore.setYearAterm(yearAterm);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addScores2Realm Realm.Transaction.OnSuccess");
                EventBus.getDefault().post(new LoginResponseCode(LoginResponseCode.REALM_SCORE_STO_OK));
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", "addScores2Realm Realm.Transaction.OnError");
            }

        });
    }

    private ScoreData scoreMap2ScoreData(Realm realm, Map<String, String> result) {
        ScoreData scoreData = realm.createObject(ScoreData.class);
        scoreData.setSchoolYear(result.get("学年"));
        scoreData.setSchoolTerm(result.get("学期"));
        scoreData.setCourseCode(result.get("课程代码"));
        scoreData.setCourseName(result.get("课程名称"));
        scoreData.setCourseNature(result.get("课程性质"));
        scoreData.setCourseBelong(result.get("课程归属"));
        scoreData.setCredit(result.get("学分"));
        scoreData.setUsualScore(result.get("平时成绩"));
        scoreData.setMidtermScore(result.get("期中成绩"));
        scoreData.setFinalScore(result.get("期末成绩"));
        scoreData.setExperimentScore(result.get("实验成绩"));
        scoreData.setScore(result.get("成绩"));
        scoreData.setMake_upScore(result.get("补考成绩"));
        scoreData.setRebuild(result.get("是否重修"));
        scoreData.setCollegeBelong(result.get("开课学院"));
        scoreData.setComment(result.get("备注"));
        scoreData.setMake_upComment(result.get("补考备注"));
        return scoreData;
    }


    public void addPersonInfo2Realm(final Realm realm, final PersonInfo info) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                bgRealm.copyToRealm(info);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addPersonInfo2Realm Realm.Transaction.OnSuccess");
                EventBus.getDefault().post(new LoginResponseCode(LoginResponseCode.REALM_PERSON_STO_OK));
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", "addPersonInfo2Realm Realm.Transaction.OnError");
            }

        });

    }

    public void addPersonUrls2Realm(final Realm realm, final UrlsMap map) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                PersonUrls urls = bgRealm.createObject(PersonUrls.class);
                urls.setPersonXH(map.getXh());
                Url url;
                RealmList<Url> list = new RealmList<Url>();
                for (Map.Entry<String, String> vo : map.getQueryUrl().entrySet()) {
                    url = bgRealm.createObject(Url.class);
                    url.setName(vo.getKey());
                    url.setUrl(vo.getValue());
                    list.add(url);
                }
                urls.setUrls(list);
                Log.e("test realm", urls.getPersonXH());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addPersonUrls2Realm Realm.Transaction.OnSuccess");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", "addPersonUrls2Realm Realm.Transaction.OnError");
            }

        });

    }

    public void addCourse2Realm(final Realm realm, final CourseEvent event) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                CourseDatabase course = bgRealm.createObject(CourseDatabase.class);
                course.setPersonXH(event.getPersonXH());
                course.setYearAterm(event.getYearAterm());
                course.setType(event.getType());
                formKBdatabase formKBdatabase;
                RealmList<formKBdatabase> list = new RealmList<formKBdatabase>();
                for (int i = 0; i < event.getFormKBs().size(); i++) {
                    FormKB formKB = event.getFormKBs().get(i);
                    formKBdatabase = bgRealm.createObject(formKBdatabase.class);
                    formKBdatabase.setRowspan(formKB.getRowspan());
                    formKBdatabase.setColspan(formKB.getColspan());
                    formKBdatabase.setText(formKB.getText());
                    formKBdatabase.setLength(formKB.getLength());
                    list.add(formKBdatabase);
                }
                course.setFormKBdatabases(list);

                RealmList<formSJKdatabase> sjkList = new RealmList<formSJKdatabase>();
                formSJKdatabase formSJKdatabase;
                for (int i = 0; i < event.getFormSJKs().size(); i++) {
                    FormSJK formSJK = event.getFormSJKs().get(i);
                    formSJKdatabase = bgRealm.createObject(formSJKdatabase.class);
                    formSJKdatabase.setCourseName(formSJK.getCourseName());
                    formSJKdatabase.setTeacher(formSJK.getTeacher());
                    formSJKdatabase.setCredit(formSJK.getCredit());
                    formSJKdatabase.setStudyTime(formSJK.getStudyTime());
                    formSJKdatabase.setTime(formSJK.getTime());
                    formSJKdatabase.setStudyPlace(formSJK.getStudyPlace());
                    sjkList.add(formSJKdatabase);
                }
                course.setFormSJKdatabases(sjkList);

                RealmList<formTTBinfoDatabase> ttbList = new RealmList<formTTBinfoDatabase>();
                formTTBinfoDatabase formTTBinfoDatabase;
                for (int i = 0; i < event.getFormTTBinfos().size(); i++) {
                    FormTTBinfo formTTBinfo = event.getFormTTBinfos().get(i);
                    formTTBinfoDatabase = bgRealm.createObject(formTTBinfoDatabase.class);
                    formTTBinfoDatabase.setId(formTTBinfo.getId());
                    formTTBinfoDatabase.setCourseName(formTTBinfo.getCourseName());
                    formTTBinfoDatabase.setPreStudyState(formTTBinfo.getPreStudyState());
                    formTTBinfoDatabase.setNowStudyState(formTTBinfo.getNowStudyState());
                    formTTBinfoDatabase.setPostTime(formTTBinfo.getPostTime());
                    ttbList.add(formTTBinfoDatabase);
                }
                course.setFormTTBinfoDatabases(ttbList);
                Log.e("course realm", course.getPersonXH() + "的班级课表");
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addBJCourse2Realm Realm.Transaction.OnSuccess");
                EventBus.getDefault().post(new LoginResponseCode(REALM_COURSE_STO_OK));
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", "addBJCourse2Realm Realm.Transaction.OnError");
            }

        });

    }

    private List<PersonScore> getScoreFromDatabase(String personXH, String yearAterm) {
        realm = Realm.getDefaultInstance();
        List<PersonScore> list = new ArrayList<>();
        RealmQuery<PersonScore> query = realm.where(PersonScore.class);
        RealmResults<PersonScore> results = query
                .equalTo("personXH", personXH)
                .equalTo("yearAterm", yearAterm)
                .findAll();
        if (results.size() <= 0) return null;
        for (int i = 0; i < results.size(); i++) {
            list.add(results.get(i));
        }
        return list;
    }

    private PersonInfo getPersonInfoFromDatabase(String personXH) {
        realm = Realm.getDefaultInstance();
        RealmQuery<PersonInfo> query = realm.where(PersonInfo.class);
        PersonInfo personInfo = query
                .equalTo("personXH", personXH).findFirst();

        return personInfo;
    }

    private CourseDatabase getCourseFromDatabase(String personXH, String yearAterm, String type) {
        realm = Realm.getDefaultInstance();
        RealmQuery<CourseDatabase> query = realm.where(CourseDatabase.class);
        CourseDatabase courseDatabase = query
                .equalTo("personXH", personXH).equalTo("yearAterm", yearAterm).equalTo("type", type).findFirst();
        return courseDatabase;
    }

    private ImageView zf_login_yanzhengma, zf_login_yanzhengma_change;
    private EditText et_zf_login_mima, et_zf_login_username, et_zf_login_yanzhengma;
    private Button zf_login_btn, zf_clear_btn;

    public AlertDialog loginZfDialog(String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        LinearLayout mAlertLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_zf_login, null);
        builder.setView(mAlertLayout);
        //透明
        final AlertDialog dialog = builder.create();

        zf_login_yanzhengma = (ImageView) mAlertLayout.findViewById(R.id.zf_login_yanzhengma);

        zf_login_yanzhengma_change = (ImageView) mAlertLayout.findViewById(R.id.zf_login_yanzhengma_change);
        et_zf_login_mima = (EditText) mAlertLayout.findViewById(R.id.et_zf_login_mima);
        et_zf_login_username = (EditText) mAlertLayout.findViewById(R.id.et_zf_login_username);
        et_zf_login_yanzhengma = (EditText) mAlertLayout.findViewById(R.id.et_zf_login_yanzhengma);

        et_zf_login_username.setText(SettingsUtil.getXueHao());
        et_zf_login_mima.setText(SettingsUtil.getZFMM());

        zf_login_btn = (Button) mAlertLayout.findViewById(R.id.zf_login_btn);
        network.getSecretCode();
        zf_login_yanzhengma_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                network.getSecretCode();
            }
        });
        zf_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsUtil.setXueHao(et_zf_login_username.getText().toString());
                SettingsUtil.setZFMM(et_zf_login_mima.getText().toString());
                network.zfLogin(et_zf_login_username.getText().toString(), et_zf_login_mima.getText().toString(), et_zf_login_yanzhengma.getText().toString());
            }
        });
        zf_clear_btn = (Button) mAlertLayout.findViewById(R.id.zf_clear_btn);
        zf_clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_zf_login_username.setText("");
                et_zf_login_mima.setText("");
                et_zf_login_yanzhengma.setText("");
                network.getSecretCode();
            }
        });

        return dialog;
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head:
                Toast.makeText(this, "查看个人信息", Toast.LENGTH_SHORT).show();
                //LoginDialog();
                break;
            case R.id.tv_contact_me:
                Toast.makeText(this, "联系我", Toast.LENGTH_SHORT).show();
                feedBack();
                break;
        }
    }

    private void feedBack() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "yuq329@outlook.com", null));
        intent.putExtra(Intent.EXTRA_EMAIL, "yuq329@outlook.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "反馈");
        startActivity(Intent.createChooser(intent, "反馈"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThemeChanged(ThemeChangedEvent event) {
        this.recreate();
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
