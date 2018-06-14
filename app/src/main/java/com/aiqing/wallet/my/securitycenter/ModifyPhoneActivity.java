package com.aiqing.wallet.my.securitycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.login.CodeSender;
import com.aiqing.wallet.login.LoginApi;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ModifyPhoneActivity extends BaseActivity {
    private EditText old_phone, old_code, new_phone, new_code;
    private CheckBox get_old_code, get_new_code;
    private CodeSender oldCodeSender, newCodeSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.modify_phone));
        showBackButton();
        initView();

        oldCodeSender = new CodeSender(get_old_code);
        oldCodeSender.setFinishedListener(new CodeSender.FinishedListener() {

            @Override
            public void onFinished() {
                get_old_code.setChecked(true);
            }
        });

        newCodeSender = new CodeSender(get_new_code);
        newCodeSender.setFinishedListener(new CodeSender.FinishedListener() {

            @Override
            public void onFinished() {
                get_new_code.setChecked(true);
            }
        });
    }

    private void initView() {
        old_phone = (EditText) findViewById(R.id.old_phone);
        old_code = (EditText) findViewById(R.id.old_code);
        new_phone = (EditText) findViewById(R.id.new_phone);
        new_code = (EditText) findViewById(R.id.new_code);
        get_old_code = (CheckBox) findViewById(R.id.get_old_code);
        get_new_code = (CheckBox) findViewById(R.id.get_new_code);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.get_old_code:
                requestOldMobileCode();
                break;
            case R.id.get_new_code:
                requestNewMobileCode();
                break;
            case R.id.confirm:
                modifyMobile();
                break;
        }
    }

    private void requestOldMobileCode() {
        if (isEmpty(old_phone)) {
            toast(getString(R.string.s_fill_mobile));
            get_old_code.setChecked(true);
            return;
        }
        oldCodeSender.codeSent();
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("mobile", getText(old_phone));
        params.put("type", 2);
        ApiManager.getApi(LoginApi.class).obtainMobileCode(ApiTransform.transform(params))
                .compose(RxSchedulers.<LoginApi.Bean>compose())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccess(User dataBean) {
                        toast(getString(R.string.s_mobile_code_send_success));
                        oldCodeSender.startWait();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                        oldCodeSender.sendFailed();
                        get_old_code.setChecked(true);
                    }
                });
    }

    private void requestNewMobileCode() {
        if (isEmpty(new_phone)) {
            toast(getString(R.string.s_fill_mobile));
            get_new_code.setChecked(true);
            return;
        }
        newCodeSender.codeSent();
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("mobile", getText(new_phone));
        params.put("type", 2);
        ApiManager.getApi(LoginApi.class).obtainMobileCode(ApiTransform.transform(params))
                .compose(RxSchedulers.<LoginApi.Bean>compose())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccess(User dataBean) {
                        toast(getString(R.string.s_mobile_code_send_success));
                        newCodeSender.startWait();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                        newCodeSender.sendFailed();
                        get_new_code.setChecked(true);
                    }
                });
    }

    private void modifyMobile() {
        if (isEmpty(old_phone) && isEmpty(old_code) && isEmpty(new_phone) && isEmpty(new_code)) {
            toast(getString(R.string.s_blank_info));
            if (isEmpty(old_phone)) {
                old_phone.requestFocus();
            } else if (isEmpty(old_code)) {
                old_code.requestFocus();
            } else if (isEmpty(new_phone)) {
                new_phone.requestFocus();
            } else if (isEmpty(new_code)) {
                new_code.requestFocus();
            }
            return;
        }
        newCodeSender.codeSent();
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("mobile", getText(old_phone));
        params.put("code", getText(old_code));
        params.put("mobileNew", getText(new_phone));
        params.put("codeNew", getText(new_code));
        ApiManager.getApi(UserApi.class).resetMobile(ApiTransform.transform(params))
                .compose(RxSchedulers.<UserApi.Bean>compose())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccess(User dataBean) {
                        toast(getString(R.string.modify_success));
                        finish();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }
}
