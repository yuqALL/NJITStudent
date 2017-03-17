package com.njit.student.yuqzy.njitstudent.ui.info.more;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.PersonInfo;
import com.njit.student.yuqzy.njitstudent.ui.adapter.PersonInfoAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

public class PersonInfoActivity extends AppCompatActivity {

    private String xh;
    private ListView listView;
    private Realm realm;
    private PersonInfo info = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_person_info);
        Realm.init(this);
        Intent intent = getIntent();
        xh = SettingsUtil.getXueHao();
        toolbar = (Toolbar) findViewById(R.id.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("个人信息");
        setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.lv_person_info);
        if (xh != "") {
            info = getPersonInfoFromDatabase(xh);
        }

        if (info != null) {
            initInfo(info);
            PersonInfoAdapter adapter = new PersonInfoAdapter(this, name, value);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "个人信息为空" + xh, Toast.LENGTH_SHORT).show();
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

    private PersonInfo getPersonInfoFromDatabase(String personXH) {
        realm = Realm.getDefaultInstance();
        RealmQuery<PersonInfo> query = realm.where(PersonInfo.class);
        PersonInfo personInfo = query
                .equalTo("personXH", personXH).findFirst();
        return personInfo;
    }

    private List<String> name;
    private List<String> value;

    public void initInfo(PersonInfo info) {
        name = new ArrayList<>();
        value = new ArrayList<>();

        name.add("学号");
        value.add(info.getPersonXH());
        name.add("姓名");
        value.add(info.getPersonXM());
        name.add("曾用名");
        value.add(info.getPersonCYM());
        name.add("性别");
        value.add(info.getPersonXB());
        name.add("民族");
        value.add(info.getPersonMZ());
        name.add("政治面貌");
        value.add(info.getPersonZZMM());
        name.add("学院");
        value.add(info.getPersonXY());
        name.add("系");
        value.add(info.getPersonX());
        name.add("行政班");
        value.add(info.getPersonXZB());
        name.add("教学班名称");
        value.add(info.getPersonJXBMC());
        name.add("专业名称");
        value.add(info.getPersonZYMC());
        name.add("专业方向");
        value.add(info.getPersonZYFX());
        name.add("培养方向");
        value.add(info.getPersonPYFX());
        name.add("入学日期");
        value.add(info.getPersonRXRQ());
        name.add("出生日期");
        value.add(info.getPersonCSRQ());
        name.add("身份证号");
        value.add(info.getPersonSFZH());
        name.add("学生证号");
        value.add(info.getPersonXSZH());
        name.add("毕业中学");
        value.add(info.getPersonBYZX());
        name.add("宿舍号");
        value.add(info.getPersonSSH());
        name.add("联系电话");
        value.add(info.getPersonLXDH());
        name.add("手机号码");
        value.add(info.getPersonSJHM());
        name.add("电子邮箱");
        value.add(info.getPersonDZYX());
        name.add("手机类型");
        value.add(info.getPersonSJLX());
        name.add("籍贯");
        value.add(info.getPersonJG());
        name.add("来源地区");
        value.add(info.getPersonLYDQ());
        name.add("邮政编码");
        value.add(info.getPersonYZBM());
        name.add("准考证号");
        value.add(info.getPersonZKZH());
        name.add("学习年限");
        value.add(info.getPersonXH());
        name.add("出生地");
        value.add(info.getPersonXXNX());
        name.add("健康状况");
        value.add(info.getPersonJKZK());
        name.add("学历层次");
        value.add(info.getPersonXLCC());
        name.add("来源省");
        value.add(info.getPersonLYS());
        name.add("父亲姓名");
        value.add(info.getPersonFQXM());
        name.add("父亲单位");
        value.add(info.getPersonFQDW());
        name.add("父亲单位邮编");
        value.add(info.getPersonFQDWYX());
        name.add("父亲单位电话或手机");
        value.add(info.getPersonFQDWDHHSJ());
        name.add("母亲姓名");
        value.add(info.getPersonMQXM());
        name.add("母亲单位");
        value.add(info.getPersonMQDW());
        name.add("母亲单位邮编");
        value.add(info.getPersonMQDWYB());
        name.add("母亲单位电话或手机");
        value.add(info.getPersonMQDWDHHSJ());
        name.add("家庭电话");
        value.add(info.getPersonJTDH());
        name.add("家庭邮编");
        value.add(info.getPersonJTYB());
        name.add("港澳台码");
        value.add(info.getPersonGATM());
        name.add("家庭地址");
        value.add(info.getPersonJTDZ());
        name.add("报到号");
        value.add(info.getPersonBDH());
        name.add("家庭所在地（/省/县）");
        value.add(info.getPersonJTSZD());
        name.add("是否高水平运动员");
        value.add(info.getPersonSFGSPYDY());
        name.add("备注");
        value.add(info.getPersonBZ());
        name.add("英语等级");
        value.add(info.getPersonYYDJ());
        name.add("英语成绩");
        value.add(info.getPersonYYCJ());
        name.add("学制");
        value.add(info.getPersonXZ());
        name.add("录检表页码");
        value.add(info.getPersonLJBYM());
        name.add("特长");
        value.add(info.getPersonTC());
        name.add("学籍状态");
        value.add(info.getPersonXJZT());
        name.add("入党(团)时间");
        value.add(info.getPersonRTSJ());
        name.add("当前所在级");
        value.add(info.getPersonDQSZJ());
        name.add("火车终点站");
        value.add(info.getPersonHCZDZ());
        name.add("证明人");
        value.add(info.getPersonZMR());
        name.add("考生号");
        value.add(info.getPersonKSH());
        name.add("学习形式");
        value.add(info.getPersonXXXS());
        name.add("姓名拼音");
        value.add(info.getPersonXMPY());

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
