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
import com.aiqing.wallet.user.UserService;
import com.aiqing.wallet.widget.AnimDialog;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RegisterAct extends BaseActivity implements View.OnClickListener {
    private EditText etAccount, etMobile, etEnsurePass, etPass, etMobleCode, etInviteCode;
    private CheckBox cbPassVisibility, cbPassVisibility2, cbGetMobileCode;
    private CodeSender mCodeSender;
    private AnimDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(R.string.s_forget_register_title);
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
        etAccount = findViewById(R.id.et_account);
        etEnsurePass = findViewById(R.id.et_ensure_pass);
        etMobile = findViewById(R.id.et_mobile);
        etPass = findViewById(R.id.et_pass);
        etMobleCode = findViewById(R.id.et_mobile_code);
        etInviteCode = findViewById(R.id.et_invite_code);
        cbPassVisibility = findViewById(R.id.cb_pass_visiblity);
        cbPassVisibility2 = findViewById(R.id.cb_ensure_pass_visiblity);
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
        cbPassVisibility2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etEnsurePass.setTransformationMethod(isChecked ? HideReturnsTransformationMethod.getInstance()
                        : PasswordTransformationMethod.getInstance());
                etEnsurePass.requestFocus();
                etEnsurePass.setSelection(getText(etEnsurePass).length());
            }
        });
        cbGetMobileCode.setOnClickListener(this);
        findViewById(R.id.tv_protocol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtocolAct.start(RegisterAct.this);
            }
        });
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterAct.class);
        context.startActivity(intent);
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

    public void register(View v) {
        if (isEmpty(etMobile) || isEmpty(etPass) || isEmpty(etMobleCode) || isEmpty(etAccount) || isEmpty(etEnsurePass)) {
            toast(getString(R.string.s_blank_info));
            if (isEmpty(etMobile)) {
                etMobile.requestFocus();
            } else if (isEmpty(etPass)) {
                etPass.requestFocus();
            } else if (isEmpty(etMobleCode)) {
                etMobleCode.requestFocus();
            }
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("username", getText(etAccount));
        params.put("mobile", getText(etMobile));
        params.put("password", getText(etPass));
        params.put("password2", getText(etEnsurePass));
        params.put("code", getText(etMobleCode));
        if (!isEmpty(etInviteCode)) {
            params.put("inviteCode", getText(etInviteCode));
        }
        ApiManager.getApi(LoginApi.class).register(ApiTransform.transform(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<User>(this, getString(R.string.s_operator_ing)) {
                    @Override
                    protected void onSuccess(User user) {
                        user.Mobile = getText(etMobile);
                        UserService.save(user);
                        LoginAct.start(RegisterAct.this);
//                        if (user.isActive()) {
//                            MainActivity.start(RegisterAct.this);
//                        } else {
//                            mDialog = new AnimDialog(RegisterAct.this);
//                            mDialog.contentView(R.layout.layout_user_active)
//                                    .gravity(Gravity.CENTER)
//                                    .cancelable(false)
////                    .animator(R.style.ActionSheetDialogAnimation)
//                                    .build().show();
//                        }
//                        UpdateProfileAct.start(RegisterAct.this);
                    }
                });
//                .compose(RxSchedulers.<LoginApi.Bean>compose())
//                .subscribe(new BaseObserver<User>() {
//                    @Override
//                    protected void onSuccess(User o) {
//                        toast("注册成功");
//                        finish();
//                    }
//                });
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
        params.put("type", 1);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

//    public void onActive(View v) {
//        MainActivity.start(this);
//    }
//
//    public void onActiveCancel(View v) {
//        MainActivity.start(this);
//    }
}
