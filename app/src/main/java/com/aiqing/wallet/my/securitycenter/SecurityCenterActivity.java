package com.aiqing.wallet.my.securitycenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseActivity;

public class SecurityCenterActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout modifyPhone;
    LinearLayout modifyLoginPw;
    LinearLayout modifyTransactionPw;
    LinearLayout retrieveTransactionPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_center);
        initView();
        initListener();
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.security_center));
        showBackButton();
    }

    private void initView() {
        modifyPhone = (LinearLayout) findViewById(R.id.modifyPhone);
        modifyLoginPw = (LinearLayout) findViewById(R.id.modifyLoginPw);
        modifyTransactionPw = (LinearLayout) findViewById(R.id.modifyTransactionPw);
        retrieveTransactionPw = (LinearLayout) findViewById(R.id.retrieveTransactionPw);
    }

    private void initListener() {
        modifyPhone.setOnClickListener(this);
        modifyLoginPw.setOnClickListener(this);
        modifyTransactionPw.setOnClickListener(this);
        retrieveTransactionPw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modifyPhone:
                startActivity(new Intent(this, ModifyPhoneActivity.class));
                break;
            case R.id.modifyLoginPw:
                startActivity(new Intent(this, ModifyLoginPwActivity.class));
                break;
            case R.id.modifyTransactionPw:
                startActivity(new Intent(this, ModifyTransactionPwActivity.class));
                break;
            case R.id.retrieveTransactionPw:
                startActivity(new Intent(this, RetrieveTransactionPwActivity.class));
                break;
        }
    }


}
