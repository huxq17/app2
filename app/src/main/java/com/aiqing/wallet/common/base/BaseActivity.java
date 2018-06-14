package com.aiqing.wallet.common.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiqing.basiclibrary.utils.Apk;
import com.aiqing.wallet.R;
import com.aiqing.wallet.utils.StatusBarUtils;
import com.netease.nim.uikit.common.fragment.TFragment;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    TextView navTitle;
    TextView tvMenu;
    private boolean isRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRunning = true;
//        this.getWindow().getDecorView().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        loadBase(this);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
    }

    public void setWindowStatusBarColor(int color) {
        StatusBarUtils.setWindowStatusBarColor(this, color);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ImageView navBack = findViewById(R.id.nav_back);
        tvMenu = findViewById(R.id.nav_menu);
        if (navBack == null) return;
        navBack.setOnClickListener(this);
        navTitle = findViewById(R.id.nav_title);
        navTitle.setText(getTitle());
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (navTitle != null) {
            navTitle.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        if (navTitle != null) {
            navTitle.setText(getResources().getString(titleId));
        }
    }


    public void showMenu(String text, int resId, int ivWidth, int ivHeight) {
        if (resId != -1) {
            Drawable drawable = getResources().getDrawable(resId);
            drawable.setBounds(0, 0, ivWidth, ivHeight);
            tvMenu.setCompoundDrawables(null, null, drawable, null);
        }
        tvMenu.setText(text);
        tvMenu.setVisibility(View.VISIBLE);
        tvMenu.setOnClickListener(this);
    }

    public void showBackButton() {
        findViewById(R.id.nav_back).setVisibility(View.VISIBLE);
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNavBack();
            }
        });
    }

    /**
     * fragment management
     */
    public TFragment addFragment(TFragment fragment, FragmentManager fragmentManager) {
        List<TFragment> fragments = new ArrayList<TFragment>(1);
        fragments.add(fragment);

        List<TFragment> fragments2 = addFragments(fragments, fragmentManager);
        return fragments2.get(0);
    }

    public List<TFragment> addFragments(List<TFragment> fragments, FragmentManager fragmentManager) {
        List<TFragment> fragments2 = new ArrayList<TFragment>(fragments.size());

        FragmentManager fm = fragmentManager;
        FragmentTransaction transaction = fm.beginTransaction();

        boolean commit = false;
        for (int i = 0; i < fragments.size(); i++) {
            // install
            TFragment fragment = fragments.get(i);
            int id = fragment.getContainerId();

            // exists
            TFragment fragment2 = (TFragment) fm.findFragmentById(id);

            if (fragment2 == null) {
                fragment2 = fragment;
                transaction.add(id, fragment);
                commit = true;
            }

            fragments2.add(i, fragment2);
        }

        if (commit) {
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }

        return fragments2;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public boolean isDestroyed() {
        return !isRunning;
    }

    public void log(String msg) {
        Log.e("tag", msg);
    }

    public void onClickNavBack() {
        finish();
    }

    public void onTitleClick() {
    }

    public void onMenuClick() {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Apk.INSTANCE.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.nav_back:
                onClickNavBack();
                break;
            case R.id.nav_title:
                onTitleClick();
                break;
            case R.id.nav_menu:
                onMenuClick();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void loadBase(Context context) {
        mBase = Base.getInstance(context);
    }

    private Base mBase;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view != null && mBase != null && mBase.isHideInput(view, ev)) {
                mBase.HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 格式化字符串
     *
     * @param format
     * @param args
     */
    public String format(String format, Object... args) {
        if (mBase != null) {
            return mBase.format(format, args);
        } else {
            return null;
        }
    }

    public void setText(Object obj, String str) {
        if (mBase != null) {
            mBase.setText(obj, str);
        }
    }

    /**
     * 获取edittext，textView,checkbox和button的文字
     *
     * @param obj
     * @return
     */
    public String getText(Object obj) {
        if (mBase != null) {
            return mBase.getText(obj);
        } else {
            return "";
        }
    }

    public boolean isEmpty(Object obj) {
        if (mBase != null) {
            return mBase.isEmpty(obj);
        } else {
            return true;
        }
    }

    public boolean isEmpty(String str) {
        return mBase != null ? mBase.isEmpty(str) : true;
    }

    public void toast(String msg) {
        if (mBase != null) {
            mBase.toast(msg);
        }
    }

    public void toastAll(String msg) {
        if (mBase != null) {
            mBase.toastAll(msg);
        }
    }

    public void toastL(String msg) {
        if (mBase != null) {
            mBase.toastL(msg);
        }
    }

    public void toastAllL(String msg) {
        if (mBase != null) {
            mBase.toastAllL(msg);
        }
    }
}
