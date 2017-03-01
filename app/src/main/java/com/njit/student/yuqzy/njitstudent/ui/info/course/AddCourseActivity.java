package com.njit.student.yuqzy.njitstudent.ui.info.course;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.Cell;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;

public class AddCourseActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private Cell cell;
    private EditText course_name, course_place, course_teacher;
    private TextView course_time, course_week;
    private RadioButton course_bixiu, course_renxuan;
    private Button btnCancel,btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_add_course);
        toolbar = (Toolbar) findViewById(R.id.title);
        setSupportActionBar(toolbar);
        toolbar.setTitle("添加课程");
        setDisplayHomeAsUpEnabled(true);

        course_name = (EditText) findViewById(R.id.course_name);
        course_place = (EditText) findViewById(R.id.course_place);
        course_time = (TextView) findViewById(R.id.course_time);
        course_week = (TextView) findViewById(R.id.course_week);
        course_teacher = (EditText) findViewById(R.id.course_teacher);
        course_bixiu = (RadioButton) findViewById(R.id.course_bixiu);
        course_renxuan = (RadioButton) findViewById(R.id.course_renxuan);
        btnCancel=(Button)findViewById(R.id.cancel_btn);
        btnConfirm=(Button)findViewById(R.id.confirm_btn);
        course_bixiu.setChecked(true);

        course_time.setOnClickListener(this);
        course_week.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        cell = new Cell();

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
        switch (v.getId()){
            case R.id.cancel_btn:
                break;
            case R.id.confirm_btn:
                break;
            case R.id.course_time:
                break;
            case R.id.course_week:
                break;
        }
    }
}
