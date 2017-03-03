package com.njit.student.yuqzy.njitstudent.ui.info.more;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.njit.student.yuqzy.njitstudent.Event.BreakRulesEvent;
import com.njit.student.yuqzy.njitstudent.MainActivity;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.BreakRulesRealm;
import com.njit.student.yuqzy.njitstudent.database.NewsDetail;
import com.njit.student.yuqzy.njitstudent.model.DebtInfoItem;
import com.njit.student.yuqzy.njitstudent.model.LikeNews;
import com.njit.student.yuqzy.njitstudent.model.NormalItem;
import com.njit.student.yuqzy.njitstudent.model.UrlAll;
import com.njit.student.yuqzy.njitstudent.model.UrlAllEvent;
import com.njit.student.yuqzy.njitstudent.model.UrlItem;
import com.njit.student.yuqzy.njitstudent.net.NetWork;
import com.njit.student.yuqzy.njitstudent.ui.info.more.PersonInfoActivity;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.njit.student.yuqzy.njitstudent.utils.ShowLoadDialog;
import com.njit.student.yuqzy.njitstudent.utils.WebUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.njit.student.yuqzy.njitstudent.AppGlobal.NJIT_ZF_HOST;

public class MoreInfoFragment extends Fragment implements View.OnClickListener {

    private Toolbar mToolbar;
    private Realm realm;
    private RealmQuery<NewsDetail> query;
    private RealmResults<NewsDetail> results;
    public static boolean parseUrl = false;

    //private CardView cardPerInfo,cardJWW,cardService,cardLinks;
    public MoreInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(getContext());
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_info, container, false);
        realm = Realm.getDefaultInstance();
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("更多");
        ((MainActivity) getActivity()).initDrawer(mToolbar);
        view.findViewById(R.id.show_personal_info).setOnClickListener(this);
        view.findViewById(R.id.jww_more_info).setOnClickListener(this);
        view.findViewById(R.id.school_help_page).setOnClickListener(this);
        view.findViewById(R.id.fast_links).setOnClickListener(this);
        view.findViewById(R.id.show_like_news).setOnClickListener(this);
        view.findViewById(R.id.show_like_books).setOnClickListener(this);
        return view;
    }


    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_personal_info:
                Intent intent = new Intent(getContext(), PersonInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.jww_more_info:
                WebUtils.openExternal(getContext(), NJIT_ZF_HOST);
                break;
            case R.id.school_help_page:
                Intent intent1 = new Intent(getContext(), UrlActivity.class);

                //intent1.putExtra("data",all);
                intent1.putExtra("title", "校园服务");
                startActivity(intent1);
                break;
            case R.id.fast_links:
                RealmQuery<UrlAll> query2 = realm.where(UrlAll.class);
                RealmResults<UrlAll> results2 = query2
                        .findAll();
                if (results2.size() <= 0) {
                    parseUrl=true;
                    NetWork.SchoolLink();
                }
                Intent intent4 = new Intent(getContext(), UrlActivity.class);
                intent4.putExtra("title", "快速链接");
                startActivity(intent4);
                break;

            case R.id.show_like_news:

                query = realm.where(NewsDetail.class);
                results = query.findAll();
                LikeNews likeNews = new LikeNews();
                List<NormalItem> list = new ArrayList<>();
                for (NewsDetail newsDetail : results) {
                    NormalItem normalItem = new NormalItem();
                    normalItem.setName(newsDetail.getTitle());
                    normalItem.setUrl(newsDetail.getHost());
                    normalItem.setUpdateTime(newsDetail.getUpdatetime());
                    list.add(normalItem);
                }
                likeNews.setList(list);
                Intent intent2 = new Intent(getContext(), LikeNewsActivity.class);
                intent2.putExtra("data", likeNews);
                getContext().startActivity(intent2);
                break;
            case R.id.show_like_books:
                Intent intent3 = new Intent(getContext(), LikeBooksActivity.class);
                getContext().startActivity(intent3);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UrlAllEvent event) {

//        ShowLoadDialog.dismiss();
//        Intent intent=new Intent(getContext(),UrlActivity.class);
//        intent.putExtra("data",event);
//        intent.putExtra("title","快速链接");
//        startActivity(intent);
        //存储到数据库

        RealmQuery<UrlAll> query = realm.where(UrlAll.class);
        final RealmResults<UrlAll> results = query
                .findAll();
        if (results.size() > 0) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                }
            });
        }
        addUrl2Realm(realm, event);
    }

    //PreReadEvent ADD to realm
    private void addUrl2Realm(final Realm realm, final UrlAllEvent event) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                UrlAll all = bgRealm.createObject(UrlAll.class);
                RealmList<UrlItem> urlItems = new RealmList<UrlItem>();
                for (UrlItem item : event.getUrlItems()) {
                    UrlItem ut = bgRealm.copyToRealm(item);
                    urlItems.add(ut);
                }
                all.setUrlItems(urlItems);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addScores2Realm Realm.Transaction.OnSuccess");
                parseUrl = false;
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", error.toString());
            }

        });
    }


}
