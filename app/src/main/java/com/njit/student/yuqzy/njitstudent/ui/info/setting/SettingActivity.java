package com.njit.student.yuqzy.njitstudent.ui.info.setting;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.njit.student.yuqzy.njitstudent.Event.ThemeChangedEvent;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.njit.student.yuqzy.njitstudent.utils.ThemeUtil;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;

public class SettingActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback{

    protected Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();

        setContentView(R.layout.activity_setting);
        Realm.init(this);
        initToolBar();
        setDisplayHomeAsUpEnabled(true);
        loadData();
    }
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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


    private void initTheme(){
        int themeIndex = SettingsUtil.getTheme();
        switch (themeIndex){
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
    protected void loadData() {
        getFragmentManager().beginTransaction().replace(R.id.contentLayout, new SettingFragment()).commit();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        if (selectedColor == ThemeUtil.getThemeColor(this, R.attr.colorPrimary))
            return;
        toolbar.setBackgroundColor(selectedColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(selectedColor);
        }
        if (selectedColor == getResources().getColor(R.color.lapis_blue)) {
            setTheme(R.style.LapisBlueTheme);
            SettingsUtil.setTheme(0);
        } else if (selectedColor == getResources().getColor(R.color.pale_dogwood)) {
            setTheme(R.style.PaleDogwoodTheme);
            SettingsUtil.setTheme(1);
        } else if (selectedColor == getResources().getColor(R.color.greenery)) {
            setTheme(R.style.GreeneryTheme);
            SettingsUtil.setTheme(2);
        } else if (selectedColor == getResources().getColor(R.color.primrose_yellow)) {
            setTheme(R.style.PrimroseYellowTheme);
            SettingsUtil.setTheme(3);
        } else if (selectedColor == getResources().getColor(R.color.flame)) {
            setTheme(R.style.FlameTheme);
            SettingsUtil.setTheme(4);
        } else if (selectedColor == getResources().getColor(R.color.island_paradise)) {
            setTheme(R.style.IslandParadiseTheme);
            SettingsUtil.setTheme(5);
        } else if (selectedColor == getResources().getColor(R.color.kale)) {
            setTheme(R.style.KaleTheme);
            SettingsUtil.setTheme(6);
        } else if (selectedColor == getResources().getColor(R.color.pink_yarrow)) {
            setTheme(R.style.PinkYarrowTheme);
            SettingsUtil.setTheme(7);
        } else if (selectedColor == getResources().getColor(R.color.niagara)) {
            setTheme(R.style.NiagaraTheme);
            SettingsUtil.setTheme(8);
        }
        getFragmentManager().beginTransaction().replace(R.id.contentLayout, new SettingFragment()).commit();
        EventBus.getDefault().post(new ThemeChangedEvent(selectedColor));
    }
}
