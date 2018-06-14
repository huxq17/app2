package com.aiqing.wallet.my.securitycenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.login.LoginApi;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ModifyLoginPwActivity extends BaseActivity {
    private EditText old_pw, new_pw, confirm_new_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_login_pw);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.modify_login_pw));
        showBackButton();
        initView();
    }

    private void initView() {
        old_pw = findViewById(R.id.old_pw);
        new_pw = findViewById(R.id.new_pw);
        confirm_new_pw = findViewById(R.id.confirm_new_pw);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.confirm:
                modifyLoginPwd();
                break;
        }
    }

    private void modifyLoginPwd() {
        if (isEmpty(old_pw) && isEmpty(new_pw) && isEmpty(confirm_new_pw)) {
            toast(getString(R.string.s_blank_info));
            if (isEmpty(old_pw)) {
                old_pw.requestFocus();
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
        params.put("oldPassword", getText(old_pw));
        params.put("newPassword", getText(new_pw));
        params.put("newPassword2", getText(confirm_new_pw));
        ApiManager.getApi(UserApi.class).resetLoginPass(ApiTransform.transform(params))
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
