package com.aiqing.wallet;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.aiqing.basiclibrary.utils.Apk;
import com.aiqing.basiclibrary.utils.DensityUtil;
import com.aiqing.im.DemoCache;
import com.aiqing.wallet.common.FragmentController;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.common.base.BaseFragment;
import com.aiqing.wallet.home.HomeFragment;
import com.aiqing.wallet.im.IMFragment;
import com.aiqing.wallet.im.UserStatusObserver;
import com.aiqing.wallet.my.MyFragment;
import com.aiqing.wallet.user.UserService;
import com.aiqing.wallet.utils.VersionUpgrade;
import com.aiqing.wallet.wallet.WalletFragment;
import com.netease.nimlib.sdk.NimIntent;


public class MainActivity extends BaseActivity {
    private LinearLayout mBottomBar;
    private static final String CUR_TAB_ID = "CURRENT_TAB_ID";
    private int curTabId;
    FragmentController<BaseFragment> fragmentController;
    private Fragment curFragment;
    UserStatusObserver userStatusObserver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DemoCache.setAccount(UserService.getAccount());
        fragmentController = new FragmentController<>(getSupportFragmentManager());
        initView();
        if (savedInstanceState != null) {
            curTabId = savedInstanceState.getInt(CUR_TAB_ID);
        } else {
            curTabId = R.id.rb_home;
        }
        receiverMsg(getIntent());
        userStatusObserver = new UserStatusObserver(this);
        userStatusObserver.register();
        new VersionUpgrade(this).check();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        receiverMsg(intent);
    }

    @Override
    public void onContentChanged() {
//        super.onContentChanged();
    }

    private void receiverMsg(Intent intent) {
        if (intent != null) {
            Object messages = intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
//            List<IMMessage> messages = (List<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            if (messages != null) {
                curTabId = R.id.rb_letter;
            }
        }
        findViewById(curTabId).performClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userStatusObserver.unregister();
    }

    private void initView() {
        mBottomBar = findViewById(R.id.tab_bottom_home);
        RadioButton rbHome = findViewById(R.id.rb_home);
        rbHome.setCompoundDrawablePadding(DensityUtil.dip2px(this, 5));
        Drawable homeTopDrawable = rbHome.getCompoundDrawables()[1];
        homeTopDrawable.setBounds(0, 0, DensityUtil.dip2px(this, 20), DensityUtil.dip2px(this, 20));
        rbHome.setCompoundDrawables(null, homeTopDrawable, null, null);

        RadioButton rbWallet = findViewById(R.id.rb_wallet);
        rbWallet.setCompoundDrawablePadding(DensityUtil.dip2px(this, 6));
        Drawable walletTopDrawable = rbWallet.getCompoundDrawables()[1];
        walletTopDrawable.setBounds(0, 0, DensityUtil.dip2px(this, 19), DensityUtil.dip2px(this, 19));
        rbWallet.setCompoundDrawables(null, walletTopDrawable, null, null);

        RadioButton rbLetter = findViewById(R.id.rb_letter);
        rbLetter.setCompoundDrawablePadding(DensityUtil.dip2px(this, 10));
        Drawable letterTopDrawable = rbLetter.getCompoundDrawables()[1];
        letterTopDrawable.setBounds(0, 0, DensityUtil.dip2px(this, 21), DensityUtil.dip2px(this, 15));
        rbLetter.setCompoundDrawables(null, letterTopDrawable, null, null);

        RadioButton rbMy = findViewById(R.id.rb_my);
        rbMy.setCompoundDrawablePadding(DensityUtil.dip2px(this, 4));
        Drawable myTopDrawable = rbMy.getCompoundDrawables()[1];
        myTopDrawable.setBounds(0, 0, DensityUtil.dip2px(this, 21), DensityUtil.dip2px(this, 21));
        rbMy.setCompoundDrawables(null, myTopDrawable, null, null);
    }

    public void onCheck(View v) {
        int checkedId = v.getId();
        if (curTabId == 0 && curTabId == checkedId) return;
        switch (checkedId) {
            case R.id.rb_home:
                replaceFragment(getTabFragmentById(HomeFragment.class, R.id.rb_home));
                break;
            case R.id.rb_letter:
                replaceFragment(getTabFragmentById(IMFragment.class, R.id.rb_letter));
                break;
            case R.id.rb_wallet:
                replaceFragment(getTabFragmentById(WalletFragment.class, R.id.rb_wallet));
                break;
            case R.id.rb_my:
                replaceFragment(getTabFragmentById(MyFragment.class, R.id.rb_my));
                break;
        }
        closeOther(checkedId);
        curTabId = checkedId;
    }

    private <T extends BaseFragment> T getTabFragmentById(final Class<T> clazz, final int id) {
        T fragment = fragmentController.getFragmentById(id, clazz, new FragmentController.Supplier<T>() {
            @Override
            public T get() {
                T fragment = null;
                try {
                    fragment = clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return fragment;
            }
        });
        return fragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CUR_TAB_ID, curTabId);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Apk.INSTANCE.onActivityResult(requestCode, resultCode, data);
        if (curFragment != null) {
            curFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (curFragment != null) {
            curFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void replaceFragment(Fragment fragment) {
        curFragment = fragment;
        fragmentController.show(fragment, R.id.main_fragment_layout);
    }

    public void closeOther(int id) {
        int count = mBottomBar.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton button = (RadioButton) mBottomBar.getChildAt(i);
            if (id != button.getId()) {
                button.setChecked(false);
            }
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
//        if (context instanceof Activity) {
//            ((Activity) context).finish();
//        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(false);
    }
}
