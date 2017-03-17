package com.njit.student.yuqzy.njitstudent.ui.info.socre;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.PersonInfoEvent;
import com.njit.student.yuqzy.njitstudent.Event.SecretCode;
import com.njit.student.yuqzy.njitstudent.MainActivity;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.PersonInfo;
import com.njit.student.yuqzy.njitstudent.database.PersonScore;
import com.njit.student.yuqzy.njitstudent.database.ScoreData;
import com.njit.student.yuqzy.njitstudent.net.ZfNetData;
import com.njit.student.yuqzy.njitstudent.ui.adapter.ScoreListAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.ScoresDialogAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.njit.student.yuqzy.njitstudent.utils.ShowLoadDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ScoreFragment extends Fragment implements View.OnClickListener {

    private Realm realm;
    private Toolbar mToolbar;
    private TextView toorbarTitle;
    private ExpandableLayoutListView expandableLayoutListView;
    private View view;
    private ZfNetData network;
    private List<ScoreData> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Realm.init(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_score, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("成绩查询");
        view.findViewById(R.id.img_cu).setOnClickListener(this);
        view.findViewById(R.id.img_all_score).setOnClickListener(this);
        view.findViewById(R.id.img_user).setOnClickListener(this);

        ((MainActivity) getActivity()).initDrawer(mToolbar);
        toorbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        toorbarTitle.setText("查询");
        network = MainActivity.network;
        List<PersonScore> result = null;
        if (SettingsUtil.getXueHao() != "") {
            if (SettingsUtil.getUserScoreTerm() != "") {
                //String[] info = SettingsUtil.getUserScoreTerm().split(":");
                result = getScoreFromDatabase(SettingsUtil.getXueHao(), SettingsUtil.getUserScoreTerm());
            }
        }
        if (result != null && result.size() > 0) {

            list = null;
            list = new ArrayList<>();

            for (PersonScore score : result) {
                for (ScoreData scoreData : score.getScoreDatas()) {
                    list.add(scoreData);
                }
            }
            String[] info = SettingsUtil.getUserScoreTerm().split(":");
            init_view(info, list);
        } else {
            dialogLogin = loginZfDialog("教务网登录");
            dialogLogin.show();
        }
        return view;
    }

    private void init_view(String[] time, List<ScoreData> list) {
        ShowLoadDialog.dismiss();
        //toorbarTitle.setText("2016-2017 第1学期");
        toorbarTitle.setText(time[0] + " 第" + time[1] + "学期");
        toorbarTitle.setOnClickListener(this);
        expandableLayoutListView = (ExpandableLayoutListView) view.findViewById(R.id.lv_score_list);
        loadData(list);
    }

    private void init_view(List<ScoreData> list) {
        //toorbarTitle.setText("2016-2017 第1学期");
        toorbarTitle.setText("所有数据（数据库）");
        toorbarTitle.setOnClickListener(this);
        expandableLayoutListView = (ExpandableLayoutListView) view.findViewById(R.id.lv_score_list);
        loadData(list);
    }

    private void loadData(List<ScoreData> list) {

        ScoreListAdapter adapter = new ScoreListAdapter(getContext(), list);
        expandableLayoutListView.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private String[] chooseTerm = new String[2];

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_title:
                new MaterialDialog.Builder(getContext())
                        .title("选择学期")
                        .items(genTerm())
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

                                chooseTerm = termValue.get(which).split(":");
                                List<PersonScore> scoreList = null;
                                if (SettingsUtil.getXueHao() != "") {
                                    scoreList = getScoreFromDatabase(SettingsUtil.getXueHao(), chooseTerm[0] + ":" + chooseTerm[1]);
                                    if (scoreList != null && scoreList.size() > 0) {

                                        list = null;
                                        list = new ArrayList<>();

                                        for (PersonScore score : scoreList) {
                                            for (ScoreData scoreData : score.getScoreDatas()) {
                                                list.add(scoreData);
                                            }
                                        }
                                        init_view(chooseTerm, list);
                                    } else {
                                        if (network.cookieStore != null && network.theUrls != null) {
                                            ShowLoadDialog.show(getContext());
                                            network.getScore(chooseTerm[0], chooseTerm[1]);
                                        } else {
                                            dialogLogin = loginZfDialog("重新登录");
                                            dialogLogin.show();
                                        }
                                    }
                                } else {
                                    dialogLogin = loginZfDialog("教务网登录");
                                    dialogLogin.show();
                                }
                                return;
                            }
                        })
                        .negativeText("取消")
                        .show();
                break;

            case R.id.img_cu:
                if (list != null && !list.isEmpty()) {
                    float avrScore = 0;
                    float avrFinalScore = 0;
                    float gravityScore = 0;
                    float jidian = 0;
                    int num = 0;
                    float allCredit = 0;
                    for (int i = 0; i < list.size(); i++) {
                        float score = 0;
                        float finalScore = 0;
                        ScoreData data = list.get(i);
                        float credit = 0;
                        try {
                            if (!data.getCourseName().contains("体育")) {
                                credit = Float.parseFloat(data.getCredit());
                                score = Float.parseFloat(data.getScore());
                                finalScore = Float.parseFloat(data.getFinalScore());

                                if (score >= 95) {
                                    jidian += 5 * credit;
                                } else if (score >= 90) {
                                    jidian += 4.5 * credit;
                                } else if (score >= 85) {
                                    jidian += 4 * credit;
                                } else if (score >= 80) {
                                    jidian += 3.5 * credit;
                                } else if (score >= 75) {
                                    jidian += 3 * credit;
                                } else if (score >= 70) {
                                    jidian += 2.5 * credit;
                                } else if (score >= 65) {
                                    jidian += 2 * credit;
                                } else if (score >= 60) {
                                    jidian += 1 * credit;
                                } else {

                                }

                            }
                        } catch (NumberFormatException e) {
                            if (data.getScore().contains("优秀")) {
                                jidian += 4.5 * credit;
                                score = 95;
                            } else if (data.getScore().contains("良好")) {
                                jidian += 3.5 * credit;
                                score = 85;
                            } else if (data.getScore().contains("中等")) {
                                jidian += 2.5 * credit;
                                score = 75;
                            } else if (data.getScore().contains("及格")) {
                                jidian += 1.5 * credit;
                                score = 60;
                            } else {
                                score = 0;
                            }

                            if (data.getFinalScore().contains("优秀")) {

                                finalScore = 95;
                            } else if (data.getFinalScore().contains("良好")) {

                                finalScore = 85;
                            } else if (data.getFinalScore().contains("中等")) {

                                finalScore = 75;
                            } else if (data.getFinalScore().contains("及格")) {

                                finalScore = 60;
                            } else {
                                finalScore = 0;
                            }
                        }


                        num++;
                        allCredit += credit;
                        avrScore += score;
                        avrFinalScore += finalScore;
                        gravityScore += score * credit;

                        Log.e("test", allCredit + " " + avrScore + " " + avrFinalScore + " " + num + " " + gravityScore);
                    }
                    DecimalFormat fnum = new DecimalFormat("##0.00");
                    avrScore = avrScore / num;
                    avrFinalScore = avrFinalScore / num;
                    gravityScore = gravityScore / allCredit;
                    jidian = jidian / allCredit;
                    //Toast.makeText(getContext(), "" + avrScore, Toast.LENGTH_SHORT).show();
                    displayDialog(new String[]{fnum.format(gravityScore), fnum.format(jidian), fnum.format(avrScore), fnum.format(avrFinalScore)});
                }

                break;
            case R.id.img_all_score:
                List<PersonScore> scoreList = null;
                if (SettingsUtil.getXueHao() != "") {
                    scoreList = getScoreFromDatabase(SettingsUtil.getXueHao());
                }
                if (scoreList != null && scoreList.size() > 0) {

                    if (list == null) {
                        list = new ArrayList<>();
                    } else {
                        list.clear();
                    }
                    for (PersonScore score : scoreList) {
                        for (ScoreData scoreData : score.getScoreDatas()) {
                            list.add(scoreData);
                        }
                    }
                }
                init_view(list);
                break;
            case R.id.img_user:
                dialogLogin = loginZfDialog("重新登录");
                dialogLogin.show();
                break;
        }
    }

    //生成学期选择
    private List<String> termValue;

    private List<String> genTerm() {
        Time time = new Time("GMT+8");
        time.setToNow();
        String nj = SettingsUtil.getNj();
        List<String> name = new ArrayList<>();
        termValue = new ArrayList<>();
        if (nj != "") {
            int startYear = Integer.parseInt(nj);
            for (int i = startYear, t = 0; i < time.year; i++, t++) {
                for (int j = 0; j < 2; j++) {
                    String title = "";
                    String content = i + "-" + (i + 1) + ":" + (j + 1);
                    if (t == 0) {
                        title = "大一";
                    } else if (t == 1) {
                        title = "大二";
                    } else if (t == 2) {
                        title = "大三";
                    } else if (t == 3) {
                        title = "大四";
                    }
                    if (j == 0) {
                        title += "上学期";
                    } else if (j == 1) {
                        title += "下学期";
                    }

                    name.add(title);
                    termValue.add(content);
                }
            }

            return name;
        } else {

        }

        return null;
    }

    private void displayDialog(String[] list) {
        ScoresDialogAdapter adapter = new ScoresDialogAdapter(getContext(), list);
        DialogPlus dialog = DialogPlus.newDialog(getContext())
                .setBackgroundColorResId(R.color.transparent)
                .setContentHolder(new ListHolder())
                .setFooter(R.layout.course_dialog_footer)
                .setCancelable(true)
                .setAdapter(adapter)
                .setGravity(Gravity.CENTER)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.footer_close_button:
                                dialog.dismiss();
                                break;
                            case R.id.footer_confirm_button:
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .setExpanded(false)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialog.show();
    }

    private ImageView zf_login_yanzhengma, zf_login_yanzhengma_change;
    private EditText et_zf_login_mima, et_zf_login_username, et_zf_login_yanzhengma;
    private Button zf_login_btn, zf_clear_btn;

    public AlertDialog loginZfDialog(String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        LinearLayout mAlertLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_zf_login, null);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SecretCode event) {
        zf_login_yanzhengma.setImageBitmap(event.getBitmap());
    }

    private PersonInfo personInfo;
    private Dialog dialogLogin;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PersonInfoEvent event) {
        personInfo = event.getInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginResponseCode event) {
        switch (event.getCode()) {
            case LoginResponseCode.LOGIN_OK:
                if (dialogLogin.isShowing()) {
                    dialogLogin.dismiss();
                }
                if (SettingsUtil.getUserScoreTerm() != "") {
                    ShowLoadDialog.show(getContext());
                    String[] info = SettingsUtil.getUserScoreTerm().split(":");
                    network.getScore(info[0], info[1]);
                } else {
                    String info[] = MainActivity.getCurrentTerm();
                    network.getScore(info[0], info[1]);
                }
                break;
            case LoginResponseCode.LOGIN_VERFATION_ERROR:
                Toast.makeText(getContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_USERNAME_ERROR:
                Toast.makeText(getContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_PASSWORD_ERROR:
                Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.LOGIN_USERNAME_OR_PASSWORD_ERROR:
                Toast.makeText(getContext(), "密码或用户名错误", Toast.LENGTH_SHORT).show();
                break;
            case LoginResponseCode.REALM_SCORE_STO_OK:
                List<PersonScore> scoreList = null;
                if (SettingsUtil.getXueHao() != "") {
                    if (SettingsUtil.getUserScoreTerm() != "") {
                        String[] info = SettingsUtil.getUserScoreTerm().split(":");
                        scoreList = getScoreFromDatabase(SettingsUtil.getXueHao(), info[0] + ":" + info[1]);
                    } else {
                        SettingsUtil.setUserCourseTerm(chooseTerm[0] + ":" + chooseTerm[1]);
                        scoreList = getScoreFromDatabase(SettingsUtil.getXueHao(), chooseTerm[0] + ":" + chooseTerm[1]);
                    }
                }

                if (scoreList != null && scoreList.size() > 0) {

                    if (list == null) {
                        list = new ArrayList<>();
                    } else {
                        list.clear();
                    }
                    for (PersonScore score : scoreList) {
                        for (ScoreData scoreData : score.getScoreDatas()) {
                            list.add(scoreData);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "获取出错", Toast.LENGTH_SHORT).show();

                }
                init_view(SettingsUtil.getUserScoreTerm().split(":"), list);
                break;
        }
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

    private List<PersonScore> getScoreFromDatabase(String personXH) {
        realm = Realm.getDefaultInstance();
        List<PersonScore> list = new ArrayList<>();
        RealmQuery<PersonScore> query = realm.where(PersonScore.class);
        RealmResults<PersonScore> results = query
                .equalTo("personXH", personXH)
                .findAll();
        if (results.size() <= 0) return null;
        for (int i = 0; i < results.size(); i++) {
            list.add(results.get(i));
        }
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
