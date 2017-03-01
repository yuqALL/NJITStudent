package com.njit.student.yuqzy.njitstudent.ui.info.socre;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
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
import com.njit.student.yuqzy.njitstudent.model.ScoreList;
import com.njit.student.yuqzy.njitstudent.net.ZfNetData;
import com.njit.student.yuqzy.njitstudent.ui.adapter.CourseDialogAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.ScoreListAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.ScoresDialogAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.text.DecimalFormat;
import java.text.NumberFormat;
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
        ScoreList result = (ScoreList) getArguments().getSerializable("data");
        list = result.getDataList();

        String[] time = result.getYearAterm().split(":");
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("成绩查询");
        view.findViewById(R.id.img_cu).setOnClickListener(this);
        view.findViewById(R.id.img_all_score).setOnClickListener(this);
        view.findViewById(R.id.img_user).setOnClickListener(this);

        ((MainActivity) getActivity()).initDrawer(mToolbar);
        toorbarTitle = (TextView) view.findViewById(R.id.toolbar_title);

        if (MainActivity.network != null && MainActivity.network.cookieStore != null && MainActivity.personInfo != null) {
            network = MainActivity.network;
        } else {
            network = new ZfNetData(getContext());
        }
        init_view(time, list);
        return view;
    }

    private void init_view(String[] time, List<ScoreData> list) {
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
        expandableLayoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = (ImageView) view.findViewById(R.id.expand_img_score);

                if (imageView.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp).getConstantState())) {
                    imageView.setImageResource(R.drawable.ic_expand_less_black_24dp);
                } else {
                    imageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
                }
            }
        });
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
                        .items(R.array.term)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

                                chooseTerm[0] = text.toString().substring(0, 9);
                                chooseTerm[1] = text.toString().substring(11, 12);
                                SettingsUtil.setUserScoreTerm(chooseTerm[0] + ":" + chooseTerm[1]);
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
                                        if (MainActivity.network != null && MainActivity.network.cookieStore != null && MainActivity.personInfo != null&&MainActivity.network.theUrls!=null) {
                                            network.getScore(chooseTerm[0], chooseTerm[1]);
                                        } else if (network.cookieStore != null&&network.theUrls!=null) {
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
                                return false;
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
                        ScoreData data = list.get(i);
                        float credit = 0;
                        try {
                            if (!data.getCourseName().contains("体育")) {
                                credit = Float.parseFloat(data.getCredit());
                                float score = Float.parseFloat(data.getScore());
                                float finalScore = Float.parseFloat(data.getFinalScore());
                                num++;
                                allCredit += credit;
                                avrScore += score;
                                avrFinalScore += finalScore;
                                gravityScore += score * credit;
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
                                Log.e("test", allCredit + " " + avrScore + " " + avrFinalScore + " " + num + " " + gravityScore);
                            }
                        } catch (NumberFormatException e) {
                            if (data.getScore().contains("优秀")) {
                                jidian += 4.5 * credit;
                            } else if (data.getScore().contains("良好")) {
                                jidian += 3.5 * credit;
                            } else if (data.getScore().contains("中等")) {
                                jidian += 2.5 * credit;
                            } else if (data.getScore().contains("及格")) {
                                jidian += 1.5 * credit;
                            } else {

                            }
                        }
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
                    String[] info = SettingsUtil.getUserScoreTerm().split(":");
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
