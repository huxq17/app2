package com.aiqing.wallet.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.user.User;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ForgetPassAct extends BaseActivity {
    private EditText etMobile, etCode, etPass;
    private CheckBox cbPassVisibility, cbGetMobileCode;
    private CodeSender mCodeSender;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(R.string.s_forget_pass_title);
        showBackButton();
        initView();
        mCodeSender = new CodeSender(cbGetMobileCode);
        mCodeSender.setFinishedListener(new CodeSender.FinishedListener() {

            @Override
            public void onFinished() {
                cbGetMobileCode.setChecked(true);
            }
        });
    }

    private void initView() {
        etMobile = findViewById(R.id.et_mobile);
        etPass = findViewById(R.id.et_pass);
        etCode = findViewById(R.id.et_mobile_code);
        cbPassVisibility = findViewById(R.id.cb_pass_visiblity);
        cbGetMobileCode = findViewById(R.id.cb_get_mobile_code);
        cbPassVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etPass.setTransformationMethod(isChecked ? HideReturnsTransformationMethod.getInstance()
                        : PasswordTransformationMethod.getInstance());
                etPass.requestFocus();
                etPass.setSelection(getText(etPass).length());
            }
        });
        cbGetMobileCode.setOnClickListener(this);
    }

    public void retrievePassBack(View v) {
        if (isEmpty(etMobile) || isEmpty(etPass) || isEmpty(etMobile)) {
            toast(getString(R.string.s_blank_info));
            if (isEmpty(etMobile)) {
                etMobile.requestFocus();
            } else if (isEmpty(etPass)) {
                etPass.requestFocus();
            } else if (isEmpty(etMobile)) {
                etMobile.requestFocus();
            }
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", getText(etMobile));
        params.put("password", getText(etPass));
        params.put("code", getText(etCode));
        ApiManager.getApi(LoginApi.class).resetPass(ApiTransform.transform(params))
                .compose(RxSchedulers.<LoginApi.Bean>compose())
                .subscribe(new BaseObserver<User>(this) {
                    @Override
                    protected void onSuccess(User o) {
                        toast(getString(R.string.s_operator_ok));
                        finish();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.cb_get_mobile_code:
                requestMobileCode();
                break;
        }
    }

    private void requestMobileCode() {
        if (isEmpty(etMobile)) {
            toast(getString(R.string.s_fill_mobile));
            cbGetMobileCode.setChecked(true);
            return;
        }
        mCodeSender.codeSent();
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("mobile", getText(etMobile));
        params.put("type", 0);
        ApiManager.getApi(LoginApi.class).obtainMobileCode(ApiTransform.transform(params))
                .compose(RxSchedulers.<LoginApi.Bean>compose())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccess(User dataBean) {
                        toast(getString(R.string.s_mobile_code_send_success));
                        mCodeSender.startWait();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                        mCodeSender.sendFailed();
                        cbGetMobileCode.setChecked(true);
                    }
                });
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ForgetPassAct.class);
        context.startActivity(intent);
    }
}
