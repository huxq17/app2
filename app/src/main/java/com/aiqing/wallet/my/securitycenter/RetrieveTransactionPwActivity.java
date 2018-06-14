package com.aiqing.wallet.my.securitycenter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.aiqing.wallet.user.UserService;
import com.huxq17.xprefs.LogUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class RetrieveTransactionPwActivity extends BaseActivity {
    private EditText code, new_pw, confirm_new_pw;
    private CheckBox get_code;
    private CodeSender codeSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_transaction_pw);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.retrieve_transaction_pw));
        showBackButton();
        initView();

        codeSender = new CodeSender(get_code);
        codeSender.setFinishedListener(new CodeSender.FinishedListener() {

            @Override
            public void onFinished() {
                get_code.setChecked(true);
            }
        });
    }

    private void initView() {
        code = findViewById(R.id.code);
        new_pw = findViewById(R.id.new_pw);
        confirm_new_pw = findViewById(R.id.confirm_new_pw);
        get_code = findViewById(R.id.get_code);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.get_code:
                requestMobileCode();
                break;
            case R.id.confirm:
                findTradePwd();
                break;
        }
    }

    private void requestMobileCode() {
        codeSender.codeSent();
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("mobile", UserService.getMobile());
        params.put("type", 3);
        ApiManager.getApi(LoginApi.class).obtainMobileCode(ApiTransform.transform(params))
                .compose(RxSchedulers.<LoginApi.Bean>compose())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccess(User dataBean) {
                        toast(getString(R.string.s_mobile_code_send_success));
                        codeSender.startWait();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                        codeSender.sendFailed();
                        get_code.setChecked(true);
                    }
                });
    }

    private void findTradePwd() {
        if (isEmpty(code) && isEmpty(new_pw) && isEmpty(confirm_new_pw)) {
            toast(getString(R.string.s_blank_info));
            if (isEmpty(code)) {
                code.requestFocus();
            } else if (isEmpty(new_pw)) {
                new_pw.requestFocus();
            } else if (isEmpty(confirm_new_pw)) {
                confirm_new_pw.requestFocus();
            }
            return;
        }
        if (!getText(new_pw).equals(getText(confirm_new_pw))) {
            toast(getString(R.string.pw_atypism));
            return;
        }
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("code", getText(code));
        params.put("newTradePassword", getText(new_pw));
        params.put("newTradePassword2", getText(confirm_new_pw));
        ApiManager.getApi(UserApi.class).findTradePass(ApiTransform.transform(params))
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
