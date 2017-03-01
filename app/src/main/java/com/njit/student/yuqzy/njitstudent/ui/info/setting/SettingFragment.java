package com.njit.student.yuqzy.njitstudent.ui.info.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.njit.student.yuqzy.njitstudent.App;
import com.njit.student.yuqzy.njitstudent.MainActivity;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.PersonScore;
import com.njit.student.yuqzy.njitstudent.database.ScoreData;
import com.njit.student.yuqzy.njitstudent.ui.info.setting.SettingActivity;
import com.njit.student.yuqzy.njitstudent.utils.FileSizeUtil;
import com.njit.student.yuqzy.njitstudent.utils.FileUtil;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.njit.student.yuqzy.njitstudent.utils.SimpleSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener {

    private Preference cleanCache;
    private Preference theme;
    private Preference personFavor;
    private Preference viewMain;
    private Preference clearInfo;
    private Preference getAllinfo;
    private Preference addUrls;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        cleanCache = findPreference(SettingsUtil.CLEAR_CACHE);
        theme = findPreference(SettingsUtil.THEME);
        viewMain = findPreference(SettingsUtil.VIEW_MAIN);
        personFavor = findPreference(SettingsUtil.PERSON_FAVOR);
        clearInfo = findPreference(SettingsUtil.CLEAR_INFO);
        getAllinfo = findPreference(SettingsUtil.FAST_GET_ALL_INFO);
        addUrls = findPreference(SettingsUtil.ADD_URLS);

        String[] colorNames = getActivity().getResources().getStringArray(R.array.color_name);
        int currentThemeIndex = SettingsUtil.getTheme();
        if (currentThemeIndex >= colorNames.length) {
            theme.setSummary("自定义色");
        } else {
            theme.setSummary(colorNames[currentThemeIndex]);
        }

        cleanCache.setOnPreferenceClickListener(this);
        theme.setOnPreferenceClickListener(this);
        viewMain.setOnPreferenceClickListener(this);
        personFavor.setOnPreferenceClickListener(this);
        clearInfo.setOnPreferenceClickListener(this);
        getAllinfo.setOnPreferenceClickListener(this);
        addUrls.setOnPreferenceClickListener(this);

        String[] cachePaths = new String[]{FileUtil.getInternalCacheDir(App.getContext()), FileUtil.getExternalCacheDir(App.getContext())};
        Observable
                .just(cachePaths)
                .map(new Func1<String[], String>() {
                    @Override
                    public String call(String[] strings) {
                        return FileSizeUtil.getAutoFileOrFilesSize(strings);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        cleanCache.setSummary(s);
                    }
                });

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {

        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == cleanCache) {
            Observable
                    .just(FileUtil.delete(FileUtil.getInternalCacheDir(App.getContext())))
                    .map(new Func1<Boolean, Boolean>() {
                        @Override
                        public Boolean call(Boolean result) {
                            return result && FileUtil.delete(FileUtil.getExternalCacheDir(App.getContext()));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SimpleSubscriber<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {
                            cleanCache.setSummary(FileSizeUtil.getAutoFileOrFilesSize(FileUtil.getInternalCacheDir(App.getContext()), FileUtil.getExternalCacheDir(App.getContext())));
                            Snackbar.make(getView(), "缓存已清除 (*^__^*)", Snackbar.LENGTH_SHORT).show();
                        }
                    });
        } else if (preference == theme) {
            new ColorChooserDialog.Builder((SettingActivity) getActivity(), R.string.theme)
                    .customColors(R.array.colors, null)
                    .doneButton(R.string.done)
                    .cancelButton(R.string.cancel)
                    .allowUserColorInput(false)
                    .allowUserColorInputAlpha(false)
                    .show();
        } else if (preference == viewMain) {
            new MaterialDialog.Builder(getActivity())
                    .title("选择主页")
                    .items(R.array.view)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            switch (which) {
                                case 0:
                                    SettingsUtil.setViewMain(MainActivity.FRAGMENT_TAG_EDUNOTIFICATION);
                                    break;
                                case 1:
                                    SettingsUtil.setViewMain(MainActivity.FRAGMENT_TAG_SCHOOL_NEW);
                                    break;
                                case 2:
                                    SettingsUtil.setViewMain(MainActivity.FRAGMENT_TAG_SCORE_INQUIRY);
                                    break;
                                case 3:
                                    SettingsUtil.setViewMain(MainActivity.FRAGMENT_TAG_QUERY_SCHEDULE);
                                    break;
                                case 4:
                                    SettingsUtil.setViewMain(MainActivity.FRAGMENT_TAG_MY_LIBRARY);
                                    break;
                                case 5:
                                    SettingsUtil.setViewMain(MainActivity.FRAGMENT_TAG_MORE);
                                    break;
                            }
                            return false;
                        }
                    })
                    .negativeText("取消")
                    .show();
        }
        return true;
    }
}
