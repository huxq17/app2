package com.aiqing.wallet.my.securitycenter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.login.LoginApi;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;
import com.aiqing.wallet.user.UserService;
import com.huxq17.xprefs.LogUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ModifyTransactionPwActivity extends BaseActivity {
    private EditText old_pw, new_pw, confirm_new_pw;
    private LinearLayout old_pw_lay;
    private View line;
    private Button confirm;
    private int tps; // 是否有交易密码 0没有 1有

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_transaction_pw);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        showBackButton();
        initView();
        tps = UserService.getTradePasswordStatus();
        if (tps == 0) {
            setTitle(getResources().getString(R.string.set_transaction_pw));
            confirm.setText(getResources().getString(R.string.determine));
            old_pw_lay.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            new_pw.setHint(R.string.transaction_pw_rule);
            confirm_new_pw.setHint(R.string.transaction_pw_rule);
        } else if (tps == 1) {
            setTitle(getResources().getString(R.string.modify_transaction_pw));
            confirm.setText(getResources().getString(R.string.confirm_modify));
            old_pw_lay.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        old_pw = findViewById(R.id.old_pw);
        new_pw = findViewById(R.id.new_pw);
        confirm_new_pw = findViewById(R.id.confirm_new_pw);
        old_pw_lay = findViewById(R.id.old_pw_lay);
        line = findViewById(R.id.line);
        confirm = findViewById(R.id.confirm);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.confirm:
                if (tps == 0) {
                    setTradePwd(); // 没有交易密码,调用设置交易密码
                } else if (tps == 1) {
                    modifyTradePwd();//有交易密码，调用修改交易密码
                }
                break;
        }
    }

    private void setTradePwd() {
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
        params.put("newTradePassword", getText(new_pw));
        params.put("newTradePassword2", getText(confirm_new_pw));
        ApiManager.getApi(UserApi.class).setTradePass(ApiTransform.transform(params))
                .compose(RxSchedulers.<UserApi.Bean>compose())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccess(User dataBean) {
                        toast(getString(R.string.modify_success));
                        UserService.setTradePasswordStatus(1);
                        finish();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }

    private void modifyTradePwd() {
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
        params.put("oldTradePassword", getText(old_pw));
        params.put("newTradePassword", getText(new_pw));
        params.put("newTradePassword2", getText(confirm_new_pw));
        ApiManager.getApi(UserApi.class).resetTradePass(ApiTransform.transform(params))
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
