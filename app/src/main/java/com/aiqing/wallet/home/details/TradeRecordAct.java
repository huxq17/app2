package com.aiqing.wallet.home.details;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.aiqing.basiclibrary.utils.DensityUtil;
import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.home.HomeApi;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.widget.tablayout.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TradeRecordAct extends BaseActivity implements NumberPicker.OnValueChangeListener, NumberPicker.Formatter {
    private List<RecordFragment> mTabContents = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<String> mDatas;
    private TabLayout mTabLayout;
    private NumberPicker mDatePicker;
    private View flDatePick;
    private int selectMonth;

    public static void start(Context context, String rate,int type) {
        Intent intent = new Intent(context, TradeRecordAct.class);
        intent.putExtra("rate", rate);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    private String rate;
    private  int type;//1:首页，2节点

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_record);
        if (savedInstanceState == null) {
            rate = getIntent().getStringExtra("rate");
            type = getIntent().getIntExtra("type",1);
        } else {
            type = getIntent().getIntExtra("type",1);
        }
        setTitle(getString(R.string.s_home_title_detail));
        showBackButton();
        mDatas = Arrays.asList(getString(R.string.s_details_all), getString(R.string.s_details_in), getString(R.string.s_details_out));
        showMenu("", R.drawable.iv_record_select_time, DensityUtil.dip2px(this, 19), DensityUtil.dip2px(this, 20));
        initView();
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onMenuClick() {
        showDatePicker();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("rate", rate);
    }

    public String getRate() {
      return   rate;
    }

    private void initDatas() {
        for (String data : mDatas) {
            RecordFragment fragment = RecordFragment.newInstance(data,type);
            mTabContents.add(fragment);
        }
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabContents.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mDatas.get(position);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mViewPager = findViewById(R.id.record_viewpager);
        mTabLayout = findViewById(R.id.record_tab_layout);
        flDatePick = LayoutInflater.from(this).inflate(R.layout.layout_datepicker, null);
        mDatePicker = flDatePick.findViewById(R.id.date_picker);
        initDatas();
        mViewPager.setAdapter(mAdapter);
        initDatePicker();
    }

    private void initDatePicker() {
        selectMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        mDatePicker.setFormatter(this);
        mDatePicker.setOnValueChangedListener(this);
        mDatePicker.setMinValue(1);
        mDatePicker.setMaxValue(12);
        obtainData(selectMonth);
    }

    public void showDatePicker() {
        mDatePicker.setValue(selectMonth);
        ViewGroup parent = (ViewGroup) flDatePick.getParent();
        if (parent != null) parent.removeView(flDatePick);
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.s_select_month))
                .setView(flDatePick)
                .setPositiveButton(getString(R.string.s_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obtainData(mDatePicker.getValue());
                    }
                })
                .show();
    }

    private void obtainData(int date) {
        selectMonth = date;
        if(type==1){
            ApiManager.getApi(HomeApi.class).queryAccountBook(getMonth(), "1")
                    .compose(RxSchedulers.<RecordBean>compose())
                    .subscribe(new BaseObserver<RecordBean.DataBean>(this) {
                        @Override
                        protected void onSuccess(RecordBean.DataBean dataBean) {
                            for (RecordFragment fragment : mTabContents) {
                                fragment.refreshData(dataBean.getList());
                            }
                        }
                    });
        }else {
            ApiManager.getApi(HomeApi.class).queryAccountBook(getMonth(), "1","2")
                    .compose(RxSchedulers.<RecordBean>compose())
                    .subscribe(new BaseObserver<RecordBean.DataBean>(this) {
                        @Override
                        protected void onSuccess(RecordBean.DataBean dataBean) {
                            for (RecordFragment fragment : mTabContents) {
                                fragment.refreshData(dataBean.getList());
                            }
                        }
                    });
        }

    }

    @Override
    public String format(int value) {
        return getDate(value);
    }

    public String getDate(int value) {
        if (value < 10) {
            return "0" + value;
        } else {
            return "" + value;
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
    }

    private String getMonth() {
        return "2018" + getDate(selectMonth);
    }
}
