package com.njit.student.yuqzy.njitstudent.ui.info.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.ui.adapter.HelpImageAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;

public class HelpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView helpList;
    private HelpImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_help);
        toolbar=(Toolbar)findViewById(R.id.title);
        setSupportActionBar(toolbar);
        toolbar.setTitle("使用帮助");
        setDisplayHomeAsUpEnabled(true);

        helpList=(ListView)findViewById(R.id.help_list);
        adapter=new HelpImageAdapter(this);
        helpList.setAdapter(adapter);
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
}
