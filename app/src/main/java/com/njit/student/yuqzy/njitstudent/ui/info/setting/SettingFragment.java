package com.njit.student.yuqzy.njitstudent.ui.info.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.luck.picture.lib.model.PictureConfig;
import com.njit.student.yuqzy.njitstudent.App;
import com.njit.student.yuqzy.njitstudent.Event.FavorChangedEvent;
import com.njit.student.yuqzy.njitstudent.MainActivity;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.CurrentReadRealm;
import com.njit.student.yuqzy.njitstudent.database.PersonScore;
import com.njit.student.yuqzy.njitstudent.database.PreReadRealm;
import com.njit.student.yuqzy.njitstudent.database.ScoreData;
import com.njit.student.yuqzy.njitstudent.ui.info.setting.SettingActivity;
import com.njit.student.yuqzy.njitstudent.utils.FileSizeUtil;
import com.njit.student.yuqzy.njitstudent.utils.FileUtil;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.njit.student.yuqzy.njitstudent.utils.SimpleSubscriber;
import com.njit.student.yuqzy.njitstudent.utils.ThemeUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.yalantis.ucrop.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
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
//    private Preference getAllinfo;
//    private Preference addUrls;

    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        Realm.init(getActivity());
        realm.getDefaultInstance();
        cleanCache = findPreference(SettingsUtil.CLEAR_CACHE);
        theme = findPreference(SettingsUtil.THEME);
        viewMain = findPreference(SettingsUtil.VIEW_MAIN);
        personFavor = findPreference(SettingsUtil.PERSON_FAVOR);
        clearInfo = findPreference(SettingsUtil.CLEAR_INFO);
//        getAllinfo = findPreference(SettingsUtil.FAST_GET_ALL_INFO);
//        addUrls = findPreference(SettingsUtil.ADD_URLS);

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
//        getAllinfo.setOnPreferenceClickListener(this);
//        addUrls.setOnPreferenceClickListener(this);

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
        } else if (preference == clearInfo) {
            new MaterialDialog.Builder(getActivity())
                    .title("警告")
                    .content("是否要清除所有个人信息？(个人收藏信息无法恢复)")
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            clearPersonInfo();
                            dialog.dismiss();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }).show();
        }else if(preference==personFavor){
            FunctionConfig config = new FunctionConfig();
            config.setType(LocalMediaLoader.TYPE_IMAGE);
            config.setCopyMode(FunctionConfig.COPY_MODEL_1_1);
            config.setCompress(false);
            config.setEnablePixelCompress(true);
            config.setEnableQualityCompress(true);
            config.setMaxSelectNum(1);
            config.setSelectMode(FunctionConfig.MODE_SINGLE);
            config.setShowCamera(true);
            config.setEnablePreview(true);
            config.setEnableCrop(true);
            config.setPreviewVideo(false);
            config.setRecordVideoDefinition(FunctionConfig.HIGH);// 视频清晰度
            config.setRecordVideoSecond(60);// 视频秒数
//        config.setCropW(1000);
//        config.setCropH(1000);
            config.setCheckNumMode(false);
            config.setCompressQuality(100);
            config.setImageSpanCount(4);
            config.setSelectMedia(selectMedia);

            config.setThemeStyle(ThemeUtil.getThemeColor(getActivity(), R.attr.colorPrimary));
            // 先初始化参数配置，在启动相册
            PictureConfig.init(config);
            PictureConfig.getPictureConfig().openPhoto(getActivity(), resultCallback);
        }
        return true;
    }
    private List<LocalMedia> selectMedia = new ArrayList<>();
    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            selectMedia = resultList;
            Log.i("callBack_result", selectMedia.size() + "");
            if (selectMedia != null) {
                if(selectMedia.get(0).getCutPath()!=null&&selectMedia.get(0).getCutPath()!="") {
                    //setBackground(selectMedia.get(0).getCutPath());
                    SettingsUtil.setPersonFavor(selectMedia.get(0).getCutPath());
                }else {
                    //setBackground(selectMedia.get(0).getPath());
                    SettingsUtil.setPersonFavor(selectMedia.get(0).getPath());
                }
                // setBackground(selectMedia.get(0).getPath());
                Log.e("path",SettingsUtil.getPersonFavor()+" vs "+selectMedia.get(0).getPath());
                EventBus.getDefault().post(new FavorChangedEvent(SettingsUtil.getPersonFavor()));
            }
        }
    };

    private void clearPersonInfo() {
        realm=Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
        SettingsUtil.setZFMM("");
        SettingsUtil.setXueHao("");
        SettingsUtil.setUserLibraryMm("");
        SettingsUtil.setCourseBacGround("");
        SettingsUtil.setTheme(0);
        SettingsUtil.setPersonFavor("");

    }
}
