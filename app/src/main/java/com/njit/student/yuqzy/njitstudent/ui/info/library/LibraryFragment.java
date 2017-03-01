package com.njit.student.yuqzy.njitstudent.ui.info.library;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.njit.student.yuqzy.njitstudent.Event.LibraryResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.LibrarySecretCode;
import com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.PersonInfoEvent;
import com.njit.student.yuqzy.njitstudent.Event.SecretCode;
import com.njit.student.yuqzy.njitstudent.MainActivity;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.CourseDatabase;
import com.njit.student.yuqzy.njitstudent.database.CurrentReadRealm;
import com.njit.student.yuqzy.njitstudent.database.PersonInfo;
import com.njit.student.yuqzy.njitstudent.database.PersonScore;
import com.njit.student.yuqzy.njitstudent.database.ScoreData;
import com.njit.student.yuqzy.njitstudent.model.ScoreList;
import com.njit.student.yuqzy.njitstudent.model.XianduCategory;
import com.njit.student.yuqzy.njitstudent.net.NetWork;
import com.njit.student.yuqzy.njitstudent.net.ZfNetData;
import com.njit.student.yuqzy.njitstudent.ui.adapter.ScoreListAdapter;
import com.njit.student.yuqzy.njitstudent.ui.adapter.ScoresDialogAdapter;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class LibraryFragment extends Fragment implements View.OnClickListener {

    private Realm realm;
    private Toolbar mToolbar;
    private TextView toorbarTitle;
    private ImageView img_login;
    private View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Realm.init(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_library, container, false);
        realm.getDefaultInstance();
        if(isShowDialog()){
            libraryDialog=loginLibraryDialog();
            libraryDialog.show();
        }
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("我的图书馆");
        img_login=(ImageView)view.findViewById(R.id.img_login);
        ((MainActivity) getActivity()).initDrawer(mToolbar);
        toorbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        initTabLayout();
        img_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libraryDialog=loginLibraryDialog();
                libraryDialog.show();
            }
        });
        return view;
    }
    private boolean isShowDialog(){
        realm = Realm.getDefaultInstance();
        RealmQuery<CurrentReadRealm> query = realm.where(CurrentReadRealm.class);
        RealmResults<CurrentReadRealm> results = query
                .equalTo("personXH", SettingsUtil.getXueHao())
                .findAll();
        if (results.size() > 0) return false;
        return true;
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
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initTabLayout() {
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }


    public static String[] list_url=new String[]{
            "http://opac.lib.njit.edu.cn/reader/book_lst.php",//当前借阅
            "http://opac.lib.njit.edu.cn/opac/search.php",// 书目检索
            "http://opac.lib.njit.edu.cn/top/top_lend.php",//热门推荐
            "http://opac.lib.njit.edu.cn/reader/book_hist.php",//借阅历史
            "http://opac.lib.njit.edu.cn/reader/book_shelf.php",//我的书架
            "http://opac.lib.njit.edu.cn/reader/preg.php",//预约信息
            "http://opac.lib.njit.edu.cn/reader/fine_pec.php",//违章缴款
            "http://opac.lib.njit.edu.cn/reader/search_hist.php"};//检索历史
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
            //当前借阅、书目检索、热门推荐、分类浏览、借阅历史、我的书架、预约信息、委托信息、违章缴款、检索历史
        String[] list=new String[]{"当前借阅","书目检索","热门推荐","借阅历史","我的书架","预约信息","违章缴款","检索历史"};

        for (int i=0;i<list.length;i++) {
            Fragment fragment = new LibraryContentFragment();
            Bundle data = new Bundle();
            data.putString("url", list_url[i]);
            fragment.setArguments(data);
            adapter.addFrag(fragment, list[i]);
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


    private Dialog libraryDialog;
    private ImageView yanzhengma, yanzhengmaChange;
    private EditText etLoginName, etLoginPwd, etYanzhengma;
    private Button login_btn, clear_btn;
    public AlertDialog loginLibraryDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("我的图书馆");
        LinearLayout mAlertLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.login_library, null);
        builder.setView(mAlertLayout);
        //透明
        final AlertDialog dialog = builder.create();
        yanzhengma = (ImageView) mAlertLayout.findViewById(R.id.login_yanzhengma);
        yanzhengmaChange = (ImageView) mAlertLayout.findViewById(R.id.login_yanzhengma_change);
        etLoginName = (EditText) mAlertLayout.findViewById(R.id.et_login_name);
        etLoginPwd = (EditText) mAlertLayout.findViewById(R.id.et_login_password);
        etYanzhengma = (EditText) mAlertLayout.findViewById(R.id.et_login_yanzhengma);


        NetWork.getLibrarySecretCode();

        yanzhengmaChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetWork.getLibrarySecretCode();
            }
        });

        etLoginName.setText(SettingsUtil.getXueHao());
        etLoginPwd.setText(SettingsUtil.getUserLibraryMm());

        login_btn = (Button) mAlertLayout.findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsUtil.setXueHao(etLoginName.getText().toString());
                SettingsUtil.setUserLibraryMm(etLoginPwd.getText().toString());
                NetWork.LibraryLogin(etLoginName.getText().toString(), etLoginPwd.getText().toString(),etYanzhengma.getText().toString());
            }
        });
        clear_btn = (Button) mAlertLayout.findViewById(R.id.clear_btn);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLoginName.setText("");
                etLoginPwd.setText("");
                etYanzhengma.setText("");
                NetWork.getLibrarySecretCode();
            }
        });

        return dialog;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LibrarySecretCode event) {
        yanzhengma.setImageBitmap(event.getBitmap());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LibraryResponseCode event) {
        if(libraryDialog.isShowing()) {
            libraryDialog.dismiss();
        }
        switch (event.getCode()) {
            case LibraryResponseCode.LOGIN_OK:

                Toast.makeText(getContext(),"登录成功",Toast.LENGTH_SHORT).show();

                break;
            case LibraryResponseCode.LOGIN_ERROR:

                Toast.makeText(getContext(), "密码或用户名为错误", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}
