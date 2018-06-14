package com.aiqing.wallet.home.transferaccounts;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 账本
 */
public class AccountBookActivity extends BaseActivity implements View.OnClickListener {
    private TextView all;
    private TextView change_into;
    private TextView turn_out;

    private ViewPager viewPager;
    private List<Fragment> list = new ArrayList<>();
    private TabFragmentPagerAdapter adapter;

    private View line;
    private int currIndex;
    private int bmpW;//横线宽度
    private int offset;//横线移动的偏移量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_book);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.account_book));
        showBackButton();
        initView();
        initListener();
        initLine();
        initViewPager();
    }

    private void initView() {
        all = (TextView) findViewById(R.id.all);
        change_into = (TextView) findViewById(R.id.change_into);
        turn_out = (TextView) findViewById(R.id.turn_out);
        line = (View) findViewById(R.id.line);
    }

    private void initListener() {
        all.setOnClickListener(this);
        change_into.setOnClickListener(this);
        turn_out.setOnClickListener(this);
    }

    /*
     * 初始化图片的位移像素
     */
    public void initLine() {
        bmpW = line.getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW / 3 - bmpW) / 2;
        Log.i("screenW", String.valueOf(screenW));
        Log.i("bmpW", String.valueOf(bmpW));
        Log.i("offset", String.valueOf(offset));
        line.setPadding(offset, 0, 0, 0);
    }

    private void initViewPager() {
        list.add(new AllFragment());
        list.add(new ChangeIntoFragment());
        list.add(new TurnOutFragment());
        viewPager.setOnPageChangeListener(new MyPagerChangeListener());
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.all:
                break;
            case R.id.change_into:
                break;
            case R.id.turn_out:
                break;
        }
    }

    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {
        private int one = offset * 2 + bmpW;//两个相邻页面的偏移量

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    viewPager.setCurrentItem(0);
                    addAn(one, 0);
                    break;
                case 1:
                    viewPager.setCurrentItem(1);
                    addAn(one, 1);
                    break;
                case 2:
                    viewPager.setCurrentItem(2);
                    addAn(one, 2);
                    break;
            }
        }
    }

    private void addAn(int one, int arg0) {
        Animation animation = new TranslateAnimation(currIndex * one, arg0 * one, 0, 0);//平移动画
        currIndex = arg0;
        animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
        animation.setDuration(200);//动画持续时间0.2秒
        line.startAnimation(animation);//是用ImageView来显示动画的
    }
}
