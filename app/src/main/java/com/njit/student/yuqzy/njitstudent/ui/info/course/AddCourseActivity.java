package com.njit.student.yuqzy.njitstudent.ui.info.course;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.CardBean;
import com.njit.student.yuqzy.njitstudent.model.Cell;
import com.njit.student.yuqzy.njitstudent.model.CellAdd;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;

import java.util.ArrayList;
import java.util.List;

public class AddCourseActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private CellAdd cell;
    private EditText course_name, course_place, course_teacher;
    private TextView course_time, course_week;
    private RadioButton course_bixiu, course_renxuan;
    private Button btnCancel, btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_add_course);
        toolbar = (Toolbar) findViewById(R.id.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("添加课程");
        setDisplayHomeAsUpEnabled(true);

        course_name = (EditText) findViewById(R.id.course_name);
        course_place = (EditText) findViewById(R.id.course_place);
        course_time = (TextView) findViewById(R.id.course_time);
        course_week = (TextView) findViewById(R.id.course_week);
        course_teacher = (EditText) findViewById(R.id.course_teacher);
        course_bixiu = (RadioButton) findViewById(R.id.course_bixiu);
        course_renxuan = (RadioButton) findViewById(R.id.course_renxuan);
        btnCancel = (Button) findViewById(R.id.cancel_btn);
        btnConfirm = (Button) findViewById(R.id.confirm_btn);
        course_bixiu.setChecked(true);

        course_time.setOnClickListener(this);
        course_week.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        cell = new CellAdd();

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                this.finish();
                break;
            case R.id.confirm_btn:
                addCourse();
                break;
            case R.id.course_time:
                SetCourseTime();
                break;
            case R.id.course_week:
                SetCourseWeek();
                break;
        }
    }

    private void SetCourseTime() {
        cell.setCourseName(course_name.getText().toString());
        cell.setPlaceName(course_place.getText().toString());
        cell.setTeacher(course_teacher.getText().toString());
        if (course_bixiu.isChecked()) {
            cell.setCourseType(course_bixiu.getText().toString());
        } else {
            cell.setCourseType(course_renxuan.getText().toString());
        }

    }

    private void SetCourseWeek() {
        initCustomOptionPicker();
        pvCustomOptions.show();
    }

    private boolean addCourse() {

        return false;
    }

    private OptionsPickerView pvCustomOptions;
    private ArrayList<CardBean> cardItem = new ArrayList<>();
    private Button btn_Time, btn_Options, btn_CustomOptions, btn_CustomTime;

    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局

        // 注意：自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针
        // 具体可参考demo 里面的两个自定义布局
        pvCustomOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = cardItem.get(options1).getPickerViewText()+
                        cardItem.get(options1).getPickerViewText();
                course_time.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData(tvSubmit);
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

                        tvAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData();
                                pvCustomOptions.setPicker(cardItem,cardItem);
                            }
                        });

                    }
                })
                .setContentTextSize(20)
                .setDividerType(WheelView.DividerType.WRAP)
                .setDividerColor(R.color.add_course)
                .setTextColorCenter(R.color.add_course) //设置选中项文字颜色
                .isDialog(true)
                .setOutSideCancelable(false)// default is true
                .build();
        pvCustomOptions.setPicker(cardItem,cardItem);//添加数据

    }

    public void getData() {
        for (int i = 0; i < 5; i++) {
            cardItem.add(new CardBean(i, "No.ABC12345 " + i));
        }
    }
}
