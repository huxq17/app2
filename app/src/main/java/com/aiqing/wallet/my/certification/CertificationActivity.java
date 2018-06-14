package com.aiqing.wallet.my.certification;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.user.UserService;

public class CertificationActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout primaryAuthentication;
    LinearLayout seniorAuthentication;
    private TextView state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        initView();
        initListener();
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.authentication));
        showBackButton();
        setState();
    }

    private void initView() {
        primaryAuthentication = findViewById(R.id.primaryAuthentication);
        seniorAuthentication = findViewById(R.id.seniorAuthentication);
        state = findViewById(R.id.state);
    }

    private void initListener() {
        primaryAuthentication.setOnClickListener(this);
        seniorAuthentication.setOnClickListener(this);
    }

    private void setState() {
        //0未认证 1已认证 2审核中 3未通过
        if (UserService.getIsRealAuth().equals("0")) {
            state.setText(getResources().getString(R.string.uncertified));
            primaryAuthentication.setEnabled(true);
        } else if (UserService.getIsRealAuth().equals("1")) {
            state.setText(getResources().getString(R.string.certified));
            primaryAuthentication.setEnabled(false);
        } else if (UserService.getIsRealAuth().equals("2")) {
            state.setText(getResources().getString(R.string.audit));
            primaryAuthentication.setEnabled(false);
        } else if (UserService.getIsRealAuth().equals("3")) {
            state.setText(getResources().getString(R.string.not_through));
            primaryAuthentication.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.primaryAuthentication:
                startActivity(new Intent(this, PrimaryCertificationActivity.class));
                break;
            case R.id.seniorAuthentication:
                startActivity(new Intent(this, SeniorCertificationActivity.class));
                break;
        }
    }
}
