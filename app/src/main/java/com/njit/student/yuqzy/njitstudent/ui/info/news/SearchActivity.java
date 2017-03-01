package com.njit.student.yuqzy.njitstudent.ui.info.news;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.njit.student.yuqzy.njitstudent.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText et_search;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private SearchCategoryFragment fragment;
    private String host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("搜索");
        Intent intent=getIntent();
        String search=intent.getStringExtra("search");
        host=intent.getStringExtra("search_host");
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        et_search=(EditText) findViewById(R.id.et_search);
        if(search!="") et_search.setText(search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    onSearch(et_search.getText().toString());
                }
                return true;
            }
        });


    }

    //搜索
    private void onSearch(String text) {
        Toast.makeText(this, "" + text, Toast.LENGTH_SHORT).show();
        fragment.setSearch(text);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragment = new SearchCategoryFragment();
        Bundle bundle=new Bundle();
        bundle.putString("search_host",host);
        fragment.setArguments(bundle);
        adapter.addFrag(fragment);
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

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
