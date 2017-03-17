package com.njit.student.yuqzy.njitstudent.ui.info.news;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.View;

import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.SchoolNewsCategory;
import com.njit.student.yuqzy.njitstudent.ui.base.BaseFragment;
import com.njit.student.yuqzy.njitstudent.utils.ACache;
import com.njit.student.yuqzy.njitstudent.utils.DisplayUtils;
import com.njit.student.yuqzy.njitstudent.utils.SimpleSubscriber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.njit.student.yuqzy.njitstudent.AppGlobal.SCHOOL_INDEX_HOST;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.SCHOOL_NOTIFICATION;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.SCHOOL_XINHUO_NEWS;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.SCHOOL_XUESHUHUODONG;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.USERAGENT;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.USERAGENT_DESKTOP;

public class SchoolNewsFragment extends BaseFragment implements View.OnClickListener {
    private static final String CACHE_SCHOOL_NEWS_CATE = "school_news_cache";
    private static final String CACHE_SCHOOL_IMG_CATE = "school_img_cache";
    private boolean isBannerBig; // banner 是否是大图
    private boolean isBannerAniming; // banner 放大缩小的动画是否正在执行
    private CollapsingToolbarLayout mCollapsingToolbar;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayoutState state; // CollapsingToolbarLayout 折叠状态

    private ImageView headImg;

    private ACache mCache;

    private Subscription subscription;

    public SchoolNewsFragment() {

    }

    @Override
    protected int getLayoutId() {

        return R.layout.school_news_fragment_tab_viewpager;
    }

    @Override
    protected void initViews() {
        mCache = ACache.get(getActivity());
        mCollapsingToolbar = findView(R.id.collapsing_toolbar);
        mAppBarLayout = findView(R.id.appbar);
        setFabDynamicState();

        headImg = findView(R.id.iv_home_banner);
        headImg.setOnClickListener(this);

        loadData();
    }

    private void loadImage(List<String> imgs) {

        String[] urls;
        if (imgs == null) {
            return;
        } else {
            urls = new String[imgs.size()];
            for (int i = 0; i < imgs.size(); i++) {
                urls[i] = imgs.get(i);
            }
        }
        String img = urls[new Random().nextInt(urls.length)];
        if (img.indexOf(".jpg") >= 0 || img.indexOf(".png") >= 0 || img.indexOf(".gif") >= 0) {
            Glide.with(this).load(img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.schoolnews_bd_normal)
                    .error(R.drawable.schoolnews_bd_normal)
                    .fitCenter()
                    .animate(R.anim.zoom_in)
                    .into(headImg);
        }
    }

    protected void loadData() {
        final List<String> imgs = loadNetImage();
        headImg.post(new Runnable() {
            @Override
            public void run() {
                if (imgs.size() > 0) {
                    loadImage(imgs);
                }
            }
        });
        subscription = Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {

                        if (imgs.size() > 0) {
                            loadImage(imgs);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_home_banner:

                if (isBannerAniming) {
                    return;
                }
                startBannerAnim();
                break;
        }
    }

    private List<String> loadNetImage() {

        List<String> imgLists = (List<String>) mCache.getAsObject(CACHE_SCHOOL_IMG_CATE);
        if (imgLists != null && imgLists.size() > 0) {
            return imgLists;
        }
        final List<String> imgList = new ArrayList<>();
        subscription = Observable.just(SCHOOL_INDEX_HOST).subscribeOn(Schedulers.io()).map(new Func1<String, List<String>>() {
            @Override
            public List<String> call(String host) {
                SchoolNewsCategory temp = new SchoolNewsCategory();
                try {
                    //Log.e("image host", host);
                    Document doc = Jsoup.connect(host).timeout(10000).userAgent(USERAGENT_DESKTOP).get();
                    Element box = doc.select("div.box1").first();
                    //Log.e("imgs", box.html());
                    Element bd = doc.select("div.bd").first();

                    Elements imgs = bd.select("a[href]");
                    if (imgs.size() > 0) {
                        for (Element e : imgs) {
                            imgList.add(e.select("img").attr("abs:src"));
                            //Log.e("imgs", e.select("img").attr("abs:href"));
                        }
                    }
                } catch (IOException e) {

                }

                return imgList;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> list) {
                mCache.put(CACHE_SCHOOL_IMG_CATE, (Serializable) list);
                //Log.e("imgs", list.get(1));


            }
        });
        return imgList;
    }


    @Override
    protected void lazyFetchData() {
        List<SchoolNewsCategory> schoolNewsCategories = (List<SchoolNewsCategory>) mCache.getAsObject(CACHE_SCHOOL_NEWS_CATE);
        if (schoolNewsCategories != null && schoolNewsCategories.size() > 0) {
            initTabLayout(schoolNewsCategories);
            return;
        }
        subscription = Observable.just(SCHOOL_XINHUO_NEWS).subscribeOn(Schedulers.io()).map(new Func1<String, List<SchoolNewsCategory>>() {
            @Override
            public List<SchoolNewsCategory> call(String host) {

                List<SchoolNewsCategory> list = new ArrayList<>();
                SchoolNewsCategory temp1 = new SchoolNewsCategory();
                SchoolNewsCategory temp2 = new SchoolNewsCategory();
                SchoolNewsCategory temp3 = new SchoolNewsCategory();
                temp1.setName("校园要闻");
                temp1.setUrl("http://xinghuo.njit.edu.cn/index/xwzx/xyyw.htm");
                list.add(temp1);
                temp2.setName("通知公告");
                temp2.setUrl(SCHOOL_NOTIFICATION);
                list.add(temp2);
                temp3.setName("学术活动");
                temp3.setUrl(SCHOOL_XUESHUHUODONG);
                list.add(temp3);
                try {
                    Document doc = Jsoup.connect(host).timeout(10000).userAgent(USERAGENT).get();
                    Element cate = doc.select("div.menu").first();
                    Elements links = cate.select("a[href]");
                    for (Element element : links) {
                        if (element.text().indexOf("首页") < 0 && element.text().indexOf("星火论坛") < 0) {
                            SchoolNewsCategory schoolNewsCategory = new SchoolNewsCategory();
                            schoolNewsCategory.setName(element.text());
                            schoolNewsCategory.setUrl(element.attr("abs:href"));
                            list.add(schoolNewsCategory);
                        }
                    }
                } catch (IOException e) {
                    Snackbar.make(getView(), "获取分类失败!", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            lazyFetchData();
                        }
                    }).setActionTextColor(getActivity().getResources().getColor(R.color.actionColor)).show();
                }

                return list;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<SchoolNewsCategory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(getView(), "获取分类失败!", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lazyFetchData();
                    }
                }).setActionTextColor(getActivity().getResources().getColor(R.color.actionColor)).show();
            }

            @Override
            public void onNext(List<SchoolNewsCategory> schoolNewsCategoryList) {
                mCache.put(CACHE_SCHOOL_NEWS_CATE, (Serializable) schoolNewsCategoryList);
                initTabLayout(schoolNewsCategoryList);
            }
        });
    }

    private void initTabLayout(List<SchoolNewsCategory> schoolnewsCategories) {
        TabLayout tabLayout = findView(R.id.tabs);
        ViewPager viewPager = findView(R.id.viewPager);
        setupViewPager(viewPager, schoolnewsCategories);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void setupViewPager(ViewPager viewPager, List<SchoolNewsCategory> schoolNewsCategoryList) {
        SchoolNewsFragment.ViewPagerAdapter adapter = new SchoolNewsFragment.ViewPagerAdapter(getChildFragmentManager());

        for (SchoolNewsCategory schoolNewsCategory : schoolNewsCategoryList) {
            Fragment fragment = new SchoolNewsCategoryFragment();
            Bundle data = new Bundle();
            data.putString("url", schoolNewsCategory.getUrl());
            fragment.setArguments(data);
            adapter.addFrag(fragment, schoolNewsCategory.getName());
        }

        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    private void startBannerAnim() {
        final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        ValueAnimator animator;
        if (isBannerBig) {
            animator = ValueAnimator.ofInt(DisplayUtils.getScreenHeight(getContext()), DisplayUtils.dp2px(240, getContext()));
        } else {
            animator = ValueAnimator.ofInt(DisplayUtils.dp2px(240, getContext()), DisplayUtils.getScreenHeight(getContext()));
        }
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height = (int) valueAnimator.getAnimatedValue();
                mAppBarLayout.setLayoutParams(layoutParams);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isBannerBig = !isBannerBig;
                isBannerAniming = false;
            }
        });
        animator.start();
        isBannerAniming = true;
    }


    private enum CollapsingToolbarLayoutState {
        EXPANDED, // 完全展开
        COLLAPSED, // 折叠
        INTERNEDIATE // 中间状态
    }

    /**
     * 根据 CollapsingToolbarLayout 的折叠状态，设置 FloatingActionButton 的隐藏和显示
     */
    private void setFabDynamicState() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED; // 修改状态标记为展开
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED; // 修改状态标记为折叠
                        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
                        layoutParams.height = DisplayUtils.dp2px(240, getContext());
                        mAppBarLayout.setLayoutParams(layoutParams);
                        isBannerBig = false;
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {

                        state = CollapsingToolbarLayoutState.INTERNEDIATE; // 修改状态标记为中间
                    }
                }
            }
        });
    }

}
