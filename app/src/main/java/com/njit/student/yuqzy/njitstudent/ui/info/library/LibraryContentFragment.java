package com.njit.student.yuqzy.njitstudent.ui.info.library;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.njit.student.yuqzy.njitstudent.Event.BookShelfEvent;
import com.njit.student.yuqzy.njitstudent.Event.BreakRulesEvent;
import com.njit.student.yuqzy.njitstudent.Event.CurrentReadEvent;
import com.njit.student.yuqzy.njitstudent.Event.HotRecEvent;
import com.njit.student.yuqzy.njitstudent.Event.LibraryResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.LibrarySecretCode;
import com.njit.student.yuqzy.njitstudent.Event.OrderBookEvent;
import com.njit.student.yuqzy.njitstudent.Event.PreReadEvent;
import com.njit.student.yuqzy.njitstudent.Event.SearchHistEvent;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.BookDetailRealm;
import com.njit.student.yuqzy.njitstudent.database.BookShelfRealm;
import com.njit.student.yuqzy.njitstudent.database.BreakRulesRealm;
import com.njit.student.yuqzy.njitstudent.database.CurrentReadRealm;
import com.njit.student.yuqzy.njitstudent.database.HotRecRealm;
import com.njit.student.yuqzy.njitstudent.database.OrderBookRealm;
import com.njit.student.yuqzy.njitstudent.database.PreReadRealm;
import com.njit.student.yuqzy.njitstudent.database.SearchHistRealm;
import com.njit.student.yuqzy.njitstudent.model.BookItem;
import com.njit.student.yuqzy.njitstudent.model.BookShelfBean;
import com.njit.student.yuqzy.njitstudent.model.BookShelfItem;
import com.njit.student.yuqzy.njitstudent.model.CurrentReadItem;
import com.njit.student.yuqzy.njitstudent.model.DebtInfoItem;
import com.njit.student.yuqzy.njitstudent.model.HotRecommendItem;
import com.njit.student.yuqzy.njitstudent.model.NormalItem;
import com.njit.student.yuqzy.njitstudent.model.OrderBookItem;
import com.njit.student.yuqzy.njitstudent.model.PreReadItem;
import com.njit.student.yuqzy.njitstudent.model.SearchHistItem;
import com.njit.student.yuqzy.njitstudent.net.NetWork;
import com.njit.student.yuqzy.njitstudent.ui.adapter.BookShelfAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.BreakRulesAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.CurrentReadAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.HotBooksAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.NormalAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.OrderBookAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.PreReadAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.SearchBookAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.SearchHistAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.njit.student.yuqzy.njitstudent.utils.ThemeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;
import static com.njit.student.yuqzy.njitstudent.AppGlobal.USERAGENT;

public class LibraryContentFragment extends Fragment {


    private String baseUrl;
    private int current_page = -1;
    private ListView listView;
    private RecyclerView recyclerView;
    private Subscription subscription;
    private boolean scrollTop = false;
    private String libraryUrl = "";
    private boolean isLoading = false;
    private String usedUrl;
    private String preUrl = "";
    private SearchBookAdapter adapter;
    protected SwipeRefreshLayout refreshLayout;
    private ImageView imageView;
    private FloatingActionButton fab_search,fab_search_result;
    private Realm realm;

    public LibraryContentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Realm.init(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        baseUrl = getArguments().getString("url");
        realm = Realm.getDefaultInstance();
        View view = inflater.inflate(R.layout.fragment_zf_login, container, false);
        if (baseUrl.equals(LibraryFragment.list_url[0])) {//当前借阅
            view = inflater.inflate(R.layout.fragment_library_content, container, false);
            listView = (ListView) view.findViewById(R.id.lv_list);
            CurrentReadEvent event = getCurrRealm();
            if (event != null) {
                CurrentReadAdapter adapter = new CurrentReadAdapter(getContext(), event);
                listView.setAdapter(adapter);
            }
            current_page = 0;
        } else if (baseUrl.equals(LibraryFragment.list_url[1])) {// 书目检索
            view = inflater.inflate(R.layout.fragment_library_search, container, false);
            final EditText search_content = (EditText) view.findViewById(R.id.search_content);
            final Spinner library_search_name = (Spinner) view.findViewById(R.id.library_search_name);
            final Spinner library_search_pipei = (Spinner) view.findViewById(R.id.library_search_pipei);
            final Spinner library_search_booktype = (Spinner) view.findViewById(R.id.library_search_booktype);
            imageView=(ImageView)view.findViewById(R.id.show_library);
            Glide.with(getContext()).load(R.drawable.carousel).fitCenter().skipMemoryCache(true).into(imageView);
            final View finalView = view;
            search_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        //隐藏软键盘
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        String name = "title", pipei = "forward", type = "ALL";

                        switch (library_search_name.getSelectedItem().toString()) {
                            //title author keyword  isbn asordno coden callno publisher series tpinyin apinyin
                            case "题名":
                                name = "title";
                                break;
                            case "责任者":
                                name = "author";
                                break;
                            case "主题词":
                                name = "keyword";
                                break;
                            case "ISBN/ISSN":
                                name = "isbn";
                                break;
                            case "订购号":
                                name = "asordno";
                                break;
                            case "分类号":
                                name = "coden";
                                break;
                            case "索书号":
                                name = "callno";
                                break;
                            case "出版社":
                                name = "publisher";
                                break;
                            case "丛书名":
                                name = "series";
                                break;
                            case "题名拼音":
                                name = "tpinyin";
                                break;
                            case "责任者拼音":
                                name = "apinyin";
                                break;

                        }
                        switch (library_search_pipei.getSelectedItem().toString()) {
                            case "前方一致":
                                pipei = "forward";
                                break;
                            case "完全匹配":
                                pipei = "full";
                                break;
                            case "任意匹配":
                                pipei = "forward";
                                break;
                        }
                        switch (library_search_booktype.getSelectedItem().toString()) {
                            case "所有书刊":
                                type = "ALL";
                                break;
                            case "中文图书":
                                type = "01";
                                break;
                            case "西文图书":
                                type = "02";
                                break;
                            case "中文期刊":
                                type = "03";
                                break;
                            case "西文期刊":
                                type = "04";
                                break;
                        }
                        initSearch(finalView, search_content.getText().toString(), name, pipei, type);
                    }
                    return true;
                }
            });

            current_page = 1;
        } else if (baseUrl.equals(LibraryFragment.list_url[2])) {//热门推荐
            view = inflater.inflate(R.layout.fragment_library_content, container, false);
            listView = (ListView) view.findViewById(R.id.lv_list);
            HotRecEvent event = getHotRealm();
            if (event != null) {
                HotBooksAdapter adapter = new HotBooksAdapter(getContext(), event);
                listView.setAdapter(adapter);
            }
            current_page = 2;

        } else if (baseUrl.equals(LibraryFragment.list_url[3])) {//借阅历史
            view = inflater.inflate(R.layout.fragment_library_content, container, false);
            listView = (ListView) view.findViewById(R.id.lv_list);
            PreReadEvent event = getPreRealm();
            if (event != null) {
                PreReadAdapter adapter = new PreReadAdapter(getContext(), event);
                listView.setAdapter(adapter);
            }
            current_page = 3;
        } else if (baseUrl.equals(LibraryFragment.list_url[4])) {//我的书架
            view = inflater.inflate(R.layout.fragment_library_content, container, false);
            listView = (ListView) view.findViewById(R.id.lv_list);
            List<BookShelfBean> list = getShelfRealm();
            if (list != null&&list.size()>0) {
                BookShelfAdapter adapter = new BookShelfAdapter(getContext(),list);
                listView.setAdapter(adapter);
            }
            current_page = 4;
        } else if (baseUrl.equals(LibraryFragment.list_url[5])) {//预约信息
            view = inflater.inflate(R.layout.fragment_library_content, container, false);
            listView = (ListView) view.findViewById(R.id.lv_list);
            OrderBookEvent event = getOrderRealm();
            if (event != null) {
                OrderBookAdapter adapter = new OrderBookAdapter(getContext(), event);
                listView.setAdapter(adapter);
            }
            current_page = 5;
        } else if (baseUrl.equals(LibraryFragment.list_url[6])) {//违章缴款
            view = inflater.inflate(R.layout.fragment_library_content, container, false);
            listView = (ListView) view.findViewById(R.id.lv_list);
            BreakRulesEvent event = getBreakRealm();
            if (event != null) {
                BreakRulesAdapter adapter = new BreakRulesAdapter(getContext(), event);
                listView.setAdapter(adapter);
            }
            current_page = 6;
        } else if (baseUrl.equals(LibraryFragment.list_url[7])) {//检索历史
            view = inflater.inflate(R.layout.fragment_library_content, container, false);
            listView = (ListView) view.findViewById(R.id.lv_list);
            SearchHistEvent event = getSearchHistRealm();
            if (event != null) {
                SearchHistAdapter adapter = new SearchHistAdapter(getContext(), event);
                listView.setAdapter(adapter);
            }
            current_page = 7;
        }

        return view;
    }

    //视图  搜索内容  内容类型 搜索匹配 搜索书目类型
    private void initSearch(View view, String content, String searchOption, String pipei, String type) {
        //http://opac.lib.njit.edu.cn/opac/openlink.php?strSearchType=title&match_flag=forward&historyCount=1&strText=java&doctype=ALL&displaypg=20&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL
        libraryUrl = "http://opac.lib.njit.edu.cn/opac/openlink.php?strSearchType=" + searchOption + "&match_flag=" + pipei + "&historyCount=1&strText=" + content + "&doctype=" + type + "&displaypg=20&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL";
        usedUrl = libraryUrl;
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        fab_search=(FloatingActionButton)view.findViewById(R.id.fab_search);
        fab_search.setBackgroundColor(ThemeUtil.getCurrentColorPrimary(getActivity()));
        fab_search_result=(FloatingActionButton)view.findViewById(R.id.fab_search_result);
        fab_search_result.setBackgroundColor(ThemeUtil.getCurrentColorPrimary(getActivity()));
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Log.e("refresh ","在这里进行了刷新");
//
//            }
//        });
        adapter=new SearchBookAdapter(getContext(),null);
        lazyFetchData();
        refreshLayout.setColorSchemeResources(ThemeUtil.getCurrentColorPrimary(getActivity()));
        refreshLayout.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_search_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int preY=0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy-preY>200){
                    fab_search.hide();
                    preY=dy;
                }else if(preY>dy){
                    fab_search.show();
                    preY=dy;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    scrollTop = false;
                    isLoading = true;
                    Log.e("onScrollStateChanged x", usedUrl);
                    getSearchResultFromServer();

                } else if (!recyclerView.canScrollVertically(-1) && !isLoading) {
                    //RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                    scrollTop = true;
                    usedUrl = libraryUrl;

                    Log.e("onScrollStateChanged s", usedUrl);
                    isLoading = true;
                    getSearchResultFromServer();
                }

//                if(recyclerView.canScrollVertically(1)){
//                    fab_search.show();
//                }
            }
        });
        recyclerView.setAdapter(adapter);
        fab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout.setVisibility(View.GONE);
                fab_search_result.setVisibility(View.VISIBLE);
            }
        });
        fab_search_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_search_result.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LibraryResponseCode event) {

        switch (event.getCode()) {
            case LibraryResponseCode.LOGIN_OK:

                if (current_page == 0) {
                    NetWork.GetCurrentRead();
                } else if (current_page == 3) {
                    NetWork.GetPreRead();
                } else if (current_page == 5) {
                    NetWork.GetYYXX();
                } else if (current_page == 6) {
                    NetWork.GetWZJK();
                } else if (current_page == 7) {
                    NetWork.GetJSLS();
                } else if (current_page == 4) {
                    NetWork.GetBookShelf();
                } else if (current_page == 2) {
                    NetWork.GetHotRecommend();
                }
                break;
            case LibraryResponseCode.LOGIN_ERROR:

                break;

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CurrentReadEvent event) {
        if (current_page == 0) {
            CurrentReadAdapter adapter = new CurrentReadAdapter(getContext(), event);
            listView.setAdapter(adapter);

            //把信息添加入数据库
            RealmQuery<CurrentReadRealm> query = realm.where(CurrentReadRealm.class);
            final RealmResults<CurrentReadRealm> results = query
                    .equalTo("personXH", event.getPersonXH())
                    .findAll();
            if (results.size() > 0) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteAllFromRealm();
                    }
                });
            }
            addCurr2Realm(realm, event);
        }
    }

    //CurrentReadEvent ADD to realm
    private void addCurr2Realm(final Realm realm, final CurrentReadEvent event) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Log.e("Star!", "start realm");
                CurrentReadRealm currentReadRealm = bgRealm.createObject(CurrentReadRealm.class);
                RealmList<CurrentReadItem> currentReadItems = new RealmList<CurrentReadItem>();
                for (CurrentReadItem item : event.getCrItems()) {
                    Log.e("Star!", "start realm" + item.getName());
                    CurrentReadItem curr=bgRealm.copyToRealm(item);
                    currentReadItems.add(curr);
                    Log.e("Star!", "start realm");
                }
                Log.e("Star!", "start realm" + event.getPersonXH());
                currentReadRealm.setPersonXH(event.getPersonXH());
                currentReadRealm.setCrItems(currentReadItems);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addScores2Realm Realm.Transaction.OnSuccess");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", "addScores2Realm Realm.Transaction.OnError");
            }

        });
    }

    private CurrentReadEvent getCurrRealm() {
        RealmQuery<CurrentReadRealm> query = realm.where(CurrentReadRealm.class);
        RealmResults<CurrentReadRealm> results = query
                .equalTo("personXH", SettingsUtil.getXueHao())
                .findAll();
        if (results.size() > 0) {
            CurrentReadRealm value = results.first();
            CurrentReadEvent event = new CurrentReadEvent(value.getCrItems(), value.getPersonXH());
            return event;
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PreReadEvent event) {
        if (current_page == 3) {

            //把信息添加入数据库
            PreReadAdapter adapter = new PreReadAdapter(getContext(), event);
            listView.setAdapter(adapter);

            RealmQuery<PreReadRealm> query = realm.where(PreReadRealm.class);
            final RealmResults<PreReadRealm> results = query
                    .equalTo("personXH", event.getPersonXH())
                    .findAll();
            if (results.size() > 0) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteAllFromRealm();
                    }
                });
            }
            addPre2Realm(realm, event);
        }
    }

    //PreReadEvent ADD to realm
    private void addPre2Realm(final Realm realm, final PreReadEvent event) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                PreReadRealm preReadRealm = bgRealm.createObject(PreReadRealm.class);
                RealmList<PreReadItem> preReadItems = new RealmList<PreReadItem>();
                for (PreReadItem item : event.getPreItems()) {

                    PreReadItem pre=bgRealm.copyToRealm(item);
                    preReadItems.add(pre);

                }

                preReadRealm.setPersonXH(event.getPersonXH());
                preReadRealm.setPreItems(preReadItems);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addScores2Realm Realm.Transaction.OnSuccess");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", error.toString());
            }

        });
    }

    private PreReadEvent getPreRealm() {
        RealmQuery<PreReadRealm> query = realm.where(PreReadRealm.class);
        RealmResults<PreReadRealm> results = query
                .equalTo("personXH", SettingsUtil.getXueHao())
                .findAll();
        if (results.size() > 0) {
            PreReadRealm value = results.first();
            PreReadEvent event = new PreReadEvent(value.getPreItems(), value.getPersonXH());
            return event;
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(OrderBookEvent event) {
        if (current_page == 5) {
            //把信息添加入数据库
            OrderBookAdapter adapter = new OrderBookAdapter(getContext(), event);
            listView.setAdapter(adapter);

            RealmQuery<OrderBookRealm> query = realm.where(OrderBookRealm.class);
            final RealmResults<OrderBookRealm> results = query
                    .equalTo("personXH", event.getPersonXH())
                    .findAll();
            if (results.size() > 0) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteAllFromRealm();
                    }
                });
            }
            addOrder2Realm(realm, event);
        }
    }
    //PreReadEvent ADD to realm
    private void addOrder2Realm(final Realm realm, final OrderBookEvent event) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                OrderBookRealm orderReadRealm = bgRealm.createObject(OrderBookRealm.class);
                RealmList<OrderBookItem> orderReadItems = new RealmList<OrderBookItem>();
                for (OrderBookItem item : event.getOrderItems()) {

                    OrderBookItem order=bgRealm.copyToRealm(item);
                    orderReadItems.add(order);

                }

                orderReadRealm.setPersonXH(event.getPersonXH());
                orderReadRealm.setOrderItems(orderReadItems);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addScores2Realm Realm.Transaction.OnSuccess");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", error.toString());
            }

        });
    }

    private OrderBookEvent getOrderRealm() {
        RealmQuery<OrderBookRealm> query = realm.where(OrderBookRealm.class);
        RealmResults<OrderBookRealm> results = query
                .equalTo("personXH", SettingsUtil.getXueHao())
                .findAll();
        if (results.size() > 0) {
            OrderBookRealm value = results.first();
            OrderBookEvent event = new OrderBookEvent(value.getOrderItems(), value.getPersonXH());
            return event;
        }
        return null;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BreakRulesEvent event) {
        if (current_page == 6) {
            //把信息添加入数据库
            BreakRulesAdapter adapter = new BreakRulesAdapter(getContext(), event);
            listView.setAdapter(adapter);

            RealmQuery<BreakRulesRealm> query = realm.where(BreakRulesRealm.class);
            final RealmResults<BreakRulesRealm> results = query
                    .equalTo("personXH", event.getPersonXH())
                    .findAll();
            if (results.size() > 0) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteAllFromRealm();
                    }
                });
            }
            addBreak2Realm(realm, event);
        }
    }

    //PreReadEvent ADD to realm
    private void addBreak2Realm(final Realm realm, final BreakRulesEvent event) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                BreakRulesRealm breakRulesRealm = bgRealm.createObject(BreakRulesRealm.class);
                RealmList<DebtInfoItem> orderReadItems = new RealmList<DebtInfoItem>();
                for (DebtInfoItem item : event.getDebtItems()) {
                    DebtInfoItem debt=bgRealm.copyToRealm(item);
                    orderReadItems.add(debt);
                }
                breakRulesRealm.setPersonXH(event.getPersonXH());
                breakRulesRealm.setDebtItems(orderReadItems);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addScores2Realm Realm.Transaction.OnSuccess");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", error.toString());
            }

        });
    }

    private BreakRulesEvent getBreakRealm() {
        RealmQuery<BreakRulesRealm> query = realm.where(BreakRulesRealm.class);
        RealmResults<BreakRulesRealm> results = query
                .equalTo("personXH", SettingsUtil.getXueHao())
                .findAll();
        if (results.size() > 0) {
            BreakRulesRealm value = results.first();
            BreakRulesEvent event = new BreakRulesEvent(value.getDebtItems(), value.getPersonXH());
            return event;
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SearchHistEvent event) {
        if (current_page == 7) {

            SearchHistAdapter adapter = new SearchHistAdapter(getContext(), event);
            listView.setAdapter(adapter);

            RealmQuery<SearchHistRealm> query = realm.where(SearchHistRealm.class);
            final RealmResults<SearchHistRealm> results = query
                    .equalTo("personXH", event.getPersonXH())
                    .findAll();
            if (results.size() > 0) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteAllFromRealm();
                    }
                });
            }
            addSearchHist2Realm(realm, event);
        }
    }
    //PreReadEvent ADD to realm
    private void addSearchHist2Realm(final Realm realm, final SearchHistEvent event) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                SearchHistRealm searchHistRealm = bgRealm.createObject(SearchHistRealm.class);
                RealmList<SearchHistItem> searchHistItems = new RealmList<SearchHistItem>();
                for (SearchHistItem item : event.getSearchItems()) {
                    SearchHistItem sh=bgRealm.copyToRealm(item);
                    searchHistItems.add(sh);
                }
                searchHistRealm.setPersonXH(event.getPersonXH());
                searchHistRealm.setSearchItems(searchHistItems);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addScores2Realm Realm.Transaction.OnSuccess");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", error.toString());
            }

        });
    }

    private SearchHistEvent getSearchHistRealm() {
        RealmQuery<SearchHistRealm> query = realm.where(SearchHistRealm.class);
        RealmResults<SearchHistRealm> results = query
                .equalTo("personXH", SettingsUtil.getXueHao())
                .findAll();
        if (results.size() > 0) {
            SearchHistRealm value = results.first();
            SearchHistEvent event = new SearchHistEvent(value.getSearchItems(), value.getPersonXH());
            return event;
        }
        return null;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BookShelfEvent event) {
        if (current_page == 4) {
            List<BookShelfBean> beanList = shelf2list(event);
            //把信息添加入数据库
            BookShelfAdapter adapter = new BookShelfAdapter(getContext(), beanList);
            listView.setAdapter(adapter);

            RealmQuery<BookShelfRealm> query = realm.where(BookShelfRealm.class);
            final RealmResults<BookShelfRealm> results = query
                    .equalTo("personXH", event.getPersonXH())
                    .findAll();
            if (results.size() > 0) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteAllFromRealm();
                    }
                });
            }
            addShelf2Realm(realm, event.getPersonXH(),beanList);

        }
    }

    private List<BookShelfBean> shelf2list(BookShelfEvent event){
        List<BookShelfBean> beanList = new ArrayList<>();
        for (int i = 0; i < event.getShelfName().size(); i++) {
            BookShelfBean itemShelf = new BookShelfBean();
            itemShelf.setType(0);
            itemShelf.setTitle(event.getShelfName().get(i));
            beanList.add(itemShelf);
            for (int j = 0; j < event.getBookList().get(i).size(); j++) {
                BookShelfBean itemBook = new BookShelfBean();
                itemBook.setType(1);
                itemBook.setItem(event.getBookList().get(i).get(j));
                beanList.add(itemBook);
            }
        }

        return beanList;
    }

    //PreReadEvent ADD to realm
    private void addShelf2Realm(final Realm realm, final String personXH,final List<BookShelfBean> list) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                BookShelfRealm bookShelfRealm = bgRealm.createObject(BookShelfRealm.class);
                RealmList<BookShelfBean> bookShelfBeen = new RealmList<BookShelfBean>();
                for (BookShelfBean item : list) {
                    BookShelfBean bsb=bgRealm.copyToRealm(item);
                    bookShelfBeen.add(bsb);
                }
                bookShelfRealm.setPersonXH(personXH);
                bookShelfRealm.setContent(bookShelfBeen);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addScores2Realm Realm.Transaction.OnSuccess");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", error.toString());
            }

        });
    }

    private List<BookShelfBean> getShelfRealm() {
        RealmQuery<BookShelfRealm> query = realm.where(BookShelfRealm.class);
        RealmResults<BookShelfRealm> results = query
                .equalTo("personXH", SettingsUtil.getXueHao())
                .findAll();
        if (results.size() > 0) {
            BookShelfRealm value = results.first();
            List<BookShelfBean> list=new ArrayList<>();
            for(BookShelfBean item:value.getContent()){
                list.add(item);
            }
            return list;
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HotRecEvent event) {
        if (current_page == 2) {
            event.getPersonXH();
            for (HotRecommendItem item : event.getHotItems()) {
                Log.e("event", item.getName());
            }
            HotBooksAdapter adapter = new HotBooksAdapter(getContext(), event);
            listView.setAdapter(adapter);

            RealmQuery<HotRecRealm> query = realm.where(HotRecRealm.class);
            final RealmResults<HotRecRealm> results = query
                    .equalTo("personXH", event.getPersonXH())
                    .findAll();
            if (results.size() > 0) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteAllFromRealm();
                    }
                });
            }
            addHot2Realm(realm, event);
        }
    }

    //PreReadEvent ADD to realm
    private void addHot2Realm(final Realm realm, final HotRecEvent event) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                HotRecRealm hotRecRealm = bgRealm.createObject(HotRecRealm.class);
                RealmList<HotRecommendItem> hotRecommendItems = new RealmList<HotRecommendItem>();
                for (HotRecommendItem item : event.getHotItems()) {
                    HotRecommendItem h=bgRealm.copyToRealm(item);
                    hotRecommendItems.add(h);
                }
                hotRecRealm.setPersonXH(event.getPersonXH());
                hotRecRealm.setHotItems(hotRecommendItems);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Star!", "addScores2Realm Realm.Transaction.OnSuccess");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Star!", error.toString());
            }

        });
    }

    private HotRecEvent getHotRealm() {
        RealmQuery<HotRecRealm> query = realm.where(HotRecRealm.class);
        RealmResults<HotRecRealm> results = query
                .equalTo("personXH", SettingsUtil.getXueHao())
                .findAll();
        if (results.size() > 0) {
            HotRecRealm value = results.first();
            HotRecEvent event = new HotRecEvent(value.getHotItems(), value.getPersonXH());
            return event;
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getSearchResultFromServer() {
        showRefreshing(true);
        String url = usedUrl;
        subscription = Observable.just(url).subscribeOn(Schedulers.io())
                .map(new Func1<String, List<BookItem>>() {
                         @Override
                         public List<BookItem> call(String url) {
                             List<BookItem> bookItems = new ArrayList<>();
                             try {

                                 Document doc = Jsoup.connect(url).timeout(10000).userAgent(USERAGENT).get();
                                 //Log.e("doc",doc.html());
                                 Element list = doc.select("div#mainbox").select("div#container").select("div.book_article").select("ol#search_book_list").first();
                                 //Log.e("list", list.html());
                                 Elements items = list.select("li");
                                 for (Element element : items) {
                                     //Log.e("item", element.html());
                                     BookItem item = new BookItem();
                                     Element title = element.select("h3").first();
                                     String name = title.select("a").first().text();
                                     String booktype = title.select("span").first().text();
                                     item.setName(name);
                                     item.setBookUrl(title.select("a").attr("abs:href"));
                                     item.setBookType(booktype);
                                     item.setId_suoshu(title.text().replace(name, "").replace(booktype, "").trim());
                                     String info = br2nl(element.select("p").html());
                                     //Log.e("info", info);
                                     String value[] = info.split(":::");
                                     for (String e : value) {
                                         //Log.e("value", e);
                                     }
                                     //String numInfo=element.select("p").select("span").text();
                                     //String num[]=element.select("p").select("span").text().split("\\s+");
                                     item.setAuthor(value[2].trim());
                                     item.setNumTotal(value[0].replace("馆藏复本：", "").trim());
                                     item.setNumCanBorrow(value[1].replace("可借复本：", "").trim());
                                     item.setPublishCompany(value[3].trim());
                                     item.setPublishTime(value[4].trim());
                                     bookItems.add(item);
                                 }

                                 Elements nextUrls = doc.select("div#mainbox").select("div#container").select("div.book_article").select("div#num").select("span.pagination").select("a");
                                 if (nextUrls.size() > 0) {
                                     for (Element nextUrl : nextUrls) {
                                         Log.e("nextUrl", nextUrl.attr("abs:href"));
                                         if (nextUrl.text().contains("下一页")) {
                                             usedUrl = "http://opac.lib.njit.edu.cn/opac/openlink.php" + nextUrl.attr("href");
                                         }
                                     }
                                 }
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                             return bookItems;
                         }
                     }

                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookItem>>() {
                               @Override
                               public void onCompleted() {
                                   isLoading = false;
                               }

                               @Override
                               public void onError(Throwable e) {
                                   isLoading = false;
                                   showRefreshing(false);
                               }

                               @Override
                               public void onNext(List<BookItem> bookItems) {
                                   showRefreshing(false);

                                   if (adapter.getData() == null || adapter.getData().size() == 0 || scrollTop) {
                                       adapter.setNewData(bookItems);
                                   } else {
                                       Log.e("on next", preUrl + "\n" + usedUrl);
                                       if (preUrl != usedUrl) {
                                           adapter.addData(adapter.getData().size(), bookItems);
                                           preUrl = usedUrl;
                                       }
                                   }
                               }
                           }

                );
    }

    public static String br2nl(String html) {
        if (html == null) return html;
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        document.select("br").append(":::");
        // document.select("/span").prepend("|");
        String s = document.html().replaceAll("</span>", ":::")
                .replaceAll("&nbsp;", ":::");
        return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

    protected void showRefreshing(final boolean refresh) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refresh);
            }
        });
    }

    protected void lazyFetchData() {
        adapter.setNewData(null);
        getSearchResultFromServer();
    }


}
