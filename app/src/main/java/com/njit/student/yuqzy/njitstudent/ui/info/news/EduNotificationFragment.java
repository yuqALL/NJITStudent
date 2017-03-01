package com.njit.student.yuqzy.njitstudent.ui.info.news;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.njit.student.yuqzy.njitstudent.MainActivity;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.EduNotificationCategory;
import com.njit.student.yuqzy.njitstudent.ui.base.BaseFragment;
import com.njit.student.yuqzy.njitstudent.utils.ACache;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.njit.student.yuqzy.njitstudent.AppGlobal.EDU_NOTIFICATION_HOST;

public class EduNotificationFragment extends BaseFragment {
    private static final String CACHE_EDU_NOTIFICATION_CATE = "edu_notification_cache";

    private Toolbar mToolbar;

    private ACache mCache;

    private Subscription subscription;
    public EduNotificationFragment() {

    }




    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_viewpager;
    }

    @Override
    protected void initViews() {
        mCache = ACache.get(getActivity());
        mToolbar = findView(R.id.toolbar);
        mToolbar.setTitle("教务通知");
        ((MainActivity) getActivity()).initDrawer(mToolbar);

    }

    @Override
    protected void lazyFetchData() {
        List<EduNotificationCategory> eduNotificationCategories = (List<EduNotificationCategory>) mCache.getAsObject(CACHE_EDU_NOTIFICATION_CATE);
        if (eduNotificationCategories != null && eduNotificationCategories.size() > 0) {
            initTabLayout(eduNotificationCategories);
            return;
        }
        subscription = Observable.just(EDU_NOTIFICATION_HOST).subscribeOn(Schedulers.io()).map(new Func1<String, List<EduNotificationCategory>>() {
            @Override
            public List<EduNotificationCategory> call(String host) {
                List<EduNotificationCategory> list = new ArrayList<>();
                try {
                    Document doc = Jsoup.connect(host).timeout(10000).get();
                    Element cate = doc.select("div.nav-left-in").first();
                    Elements links = cate.select("a[href]");
                    for (Element element : links) {
                        EduNotificationCategory notification = new EduNotificationCategory();
                        notification.setName(element.text());
                        notification.setUrl(element.attr("abs:href"));
                        list.add(notification);
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
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<EduNotificationCategory>>() {
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
            public void onNext(List<EduNotificationCategory> eduNotificationCategoryList) {
                mCache.put(CACHE_EDU_NOTIFICATION_CATE, (Serializable) eduNotificationCategoryList);
                initTabLayout(eduNotificationCategoryList);
            }
        });
    }

    private void initTabLayout(List<EduNotificationCategory> minxueCategories) {
        TabLayout tabLayout = findView(R.id.tabs);
        ViewPager viewPager = findView(R.id.viewPager);
        setupViewPager(viewPager, minxueCategories);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void setupViewPager(ViewPager viewPager, List<EduNotificationCategory> notificationCategories) {
        EduNotificationFragment.ViewPagerAdapter adapter = new EduNotificationFragment.ViewPagerAdapter(getChildFragmentManager());

        for (EduNotificationCategory notificationCategory : notificationCategories) {
            Fragment fragment = new EduNotificationCategoryFragment();
            Bundle data = new Bundle();
            data.putString("url", notificationCategory.getUrl());
            fragment.setArguments(data);
            adapter.addFrag(fragment, notificationCategory.getName());
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

}
