package com.njit.student.yuqzy.njitstudent.ui.info.about;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.njit.student.yuqzy.njitstudent.BuildConfig;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.njit.student.yuqzy.njitstudent.utils.ShareUtils;
import com.njit.student.yuqzy.njitstudent.utils.SimpleSubscriber;
import com.njit.student.yuqzy.njitstudent.utils.UpdateUtil;
import com.njit.student.yuqzy.njitstudent.utils.WebUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class AboutActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    private TextView tvVersion;
    private ImageSwitcher imageSwitcher;
    private int[] imgs = {
            R.drawable.about_1, R.drawable.about_2, R.drawable.about_3, R.drawable.about_5
    };

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_about);
        initToolBar();
        initViews();
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

    protected void initViews() {
        setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("关于");
        tvVersion = (TextView) findViewById(R.id.tv_app_version);
        tvVersion.setText("v" + BuildConfig.VERSION_NAME);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(AboutActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.zoom_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.zoom_out));
    }

    protected void loadData() {
        imageSwitcher.post(new Runnable() {
            @Override
            public void run() {
                loadImage();
            }
        });
        subscription = Observable.interval(4, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        loadImage();
                    }
                });
    }

    private void loadImage() {
        Glide.with(this).load(imgs[new Random().nextInt(4)]).into(new SimpleTarget<GlideDrawable>(imageSwitcher.getWidth(), imageSwitcher.getHeight()) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageSwitcher.setImageDrawable(resource);
            }
        });
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


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_web_home:
                WebUtils.openExternal(this, "https://github.com/yuqZY/NJITStudent");
                break;
            case R.id.btn_feedback:
                feedBack();
                break;
            case R.id.btn_check_update:
                UpdateUtil.check(AboutActivity.this, false);
                break;
            case R.id.btn_share_app:
                ShareUtils.shareText(this, "https://github.com/yuqZY/NJITStudent");
                break;
            case R.id.btn_mark_app:
                openAppMarket();
                break;
            case R.id.btn_blog_home:
                WebUtils.openExternal(this, "https://yuqzy.github.io/");
                break;
            case R.id.btn_pay_app:
                startActivity(new Intent(AboutActivity.this, PayActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    private void feedBack() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "yuq329@outlook.com", null));
        intent.putExtra(Intent.EXTRA_EMAIL, "yuq329@outlook.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "反馈");
        startActivity(Intent.createChooser(intent, "反馈"));
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

    private void openAppMarket() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException anf) {
            Toast.makeText(this, "未找到相关应用", Toast.LENGTH_SHORT).show();
        }

    }
}
