package com.aiqing.wallet.my.certification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseActivity;
import com.huxq17.xprefs.LogUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrimaryCertificationActivity extends BaseActivity {
    private EditText username, id_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_certification);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.authentication));
        showBackButton();
        initView();
    }

    private void initView() {
        username = findViewById(R.id.username);
        id_number = findViewById(R.id.id_number);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.next:
                next();
                break;
        }
    }

    private void next() {
        if (isEmpty(username) || isEmpty(id_number)) {
            toast(getString(R.string.s_blank_info));
            if (isEmpty(username)) {
                username.requestFocus();
            } else if (isEmpty(id_number)) {
                id_number.requestFocus();
            }
            return;
        }
        if (!isIDCard(getText(id_number))) {
            toast(getString(R.string.input_id));
            return;
        }
        Intent intent = new Intent(PrimaryCertificationActivity.this, SeniorCertificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", getText(username));
        bundle.putString("idNumber", getText(id_number));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public boolean isIDCard(String idCard) {
        String REGEX_ID_CARD = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }
}


