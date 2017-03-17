package com.njit.student.yuqzy.njitstudent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.njit.student.yuqzy.njitstudent.Event.CourseEvent;
import com.njit.student.yuqzy.njitstudent.Event.FavorChangedEvent;
import com.njit.student.yuqzy.njitstudent.Event.InfoDoorResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.NavBacChangedEvent;
import com.njit.student.yuqzy.njitstudent.Event.PersonInfoEvent;
import com.njit.student.yuqzy.njitstudent.Event.Scores;
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

import com.njit.student.yuqzy.njitstudent.database.ScoreData;

import com.njit.student.yuqzy.njitstudent.net.NetWork;
import com.njit.student.yuqzy.njitstudent.net.ZfNetData;
import com.njit.student.yuqzy.njitstudent.ui.info.about.AboutActivity;
import com.njit.student.yuqzy.njitstudent.ui.info.course.NJITCourseFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.library.LibraryFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.more.PersonInfoActivity;
import com.njit.student.yuqzy.njitstudent.ui.info.news.EduNotificationFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.more.MoreInfoFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.news.SchoolNewsFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.socre.ScoreFragment;
import com.njit.student.yuqzy.njitstudent.ui.info.setting.SettingActivity;
import com.njit.student.yuqzy.njitstudent.utils.DoubleClickExit;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;



import static com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode.REALM_COURSE_STO_OK;
import static com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode.REALM_PERSON_STO_OK;
import static com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode.REALM_SCORE_STO_OK;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager;
    private String currentFragmentTag;
    private Realm realm;
    public static ZfNetData network;
    private CircleImageView imgHead;
    private LinearLayout navHeader;


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
        view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        navHeader = (LinearLayout) view.findViewById(R.id.nav_header);
        imgHead = (CircleImageView) view.findViewById(R.id.img_head);
        if (SettingsUtil.getNavBac() != "") {
            Glide.with(this).load(SettingsUtil.getNavBac()).skipMemoryCache(true).fitCenter().into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    navHeader.setBackground(resource);
                }
            });
        }
        if (SettingsUtil.getPersonFavor() != "") {
            Glide.with(this).load(SettingsUtil.getPersonFavor()).skipMemoryCache(true).fitCenter().into(imgHead);
        }
        imgHead.setOnClickListener(this);
        view.findViewById(R.id.tv_contact_me).setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        initFragment(savedInstanceState);

    }

    View view;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FavorChangedEvent event) {
        Log.e("change favor", "change" + event.getPath());
        if (event.getPath() != null && event.getPath() != "")
            Glide.with(this).load(event.getPath()).skipMemoryCache(true).fitCenter().into(imgHead);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NavBacChangedEvent event) {
        if (event.getPath() != null && event.getPath() != "")
            Glide.with(this).load(event.getPath()).skipMemoryCache(true).fitCenter().into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    navHeader.setBackground(resource);
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
                    foundFragment = new ScoreFragment();
                    break;
                case FRAGMENT_TAG_QUERY_SCHEDULE:
                    foundFragment = new NJITCourseFragment();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Scores event) {
        final List<Map<String, String>> scores = event.getScores();

        RealmQuery<PersonScore> query = realm.where(PersonScore.class);

        final RealmResults<PersonScore> results = query
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


    public static PersonInfo personInfo;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PersonInfoEvent event) {
        personInfo = event.getInfo();

        RealmQuery<PersonInfo> query = realm.where(PersonInfo.class);

        final RealmResults<PersonInfo> results = query.findAll();
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
        final RealmResults<PersonUrls> results = query.findAll();

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
        final RealmResults<CourseDatabase> results = query.equalTo("yearAterm", yearAterm).equalTo("type", type).findAll();

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
                break;
            case LoginResponseCode.LOGIN_VERFATION_ERROR:
                Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_USERNAME_ERROR:
                Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_PASSWORD_ERROR:
                Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_USERNAME_OR_PASSWORD_ERROR:
                Toast.makeText(getApplicationContext(), "密码或用户名错误", Toast.LENGTH_SHORT).show();
                break;
            case REALM_SCORE_STO_OK:
                break;
            case REALM_PERSON_STO_OK:
                break;
            case REALM_COURSE_STO_OK:
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(InfoDoorResponseCode event) {

        switch (event.getCode()) {
            case InfoDoorResponseCode.LOGIN_OK:
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head:
                //Toast.makeText(this, "查看个人信息", Toast.LENGTH_SHORT).show();
                //LoginDialog();
                Intent intent = new Intent(this, PersonInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_contact_me:
                //Toast.makeText(this, "联系我", Toast.LENGTH_SHORT).show();
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
