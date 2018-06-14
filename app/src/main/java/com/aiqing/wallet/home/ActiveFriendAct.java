package com.aiqing.wallet.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseActivity;

public class ActiveFriendAct extends BaseActivity {
    private EditText etAccount;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_friend);
        etAccount = findViewById(R.id.et_active_account);
        setTitle(getString(R.string.s_home_active_friend));
        showBackButton();
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.activeResult.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    toast(getString(R.string.s_active_success));
                    finish();
                }
            }
        });
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ActiveFriendAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public void active(View v) {
        String account = getText(etAccount);
        if (isEmpty(account)) {
            toast(getString(R.string.s_login_account_input));
            return;
        }
        homeViewModel.activeFriend(account);
    }
}
