package com.aiqing.wallet.home.transferaccounts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aiqing.wallet.MainActivity;
import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.dialog.PasswordDialog;
import com.aiqing.wallet.login.ForgetPassAct;
import com.aiqing.wallet.login.LoginAct;
import com.aiqing.wallet.my.securitycenter.ModifyTransactionPwActivity;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;
import com.aiqing.wallet.utils.SoftInputUtils;
import com.huxq17.xprefs.LogUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 确认转账
 */
public class ConfirmTransferActivity extends BaseActivity implements View.OnClickListener {
    private TextView itransfer_accounts_tv, currency_transfer_tv, number_transfers_tv, remarks_tv, service_charge_tv, phone;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_transfer);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.transfer_accounts_title));
        showBackButton();
        initView();
    }

    private void initView() {
        itransfer_accounts_tv = findViewById(R.id.itransfer_accounts_tv);
        currency_transfer_tv = findViewById(R.id.currency_transfer_tv);
        number_transfers_tv = findViewById(R.id.number_transfers_tv);
        remarks_tv = findViewById(R.id.remarks_tv);
        service_charge_tv = findViewById(R.id.service_charge_tv);
        phone = findViewById(R.id.phone);

        bundle = getIntent().getExtras();
        itransfer_accounts_tv.setText(bundle.getString("account_number"));
        currency_transfer_tv.setText(bundle.getString("currency"));
        number_transfers_tv.setText(bundle.getString("number"));
        remarks_tv.setText(bundle.getString("remarks"));
        service_charge_tv.setText(bundle.getString("service_charge") + " " + getString(R.string.vdo));
        phone.setText(bundle.getString("phone"));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.confirm:
                ShowPwDialog();
                break;
        }
    }

    PasswordDialog passwordDialog;

    private void ShowPwDialog() {
        PasswordDialog.Builder builder = new PasswordDialog.Builder(this);
        builder.setCallback(new PasswordDialog.Callback() {
            @Override
            public void onForgetPassword() {
                passwordDialog.dismiss();
                startActivity(new Intent(ConfirmTransferActivity.this, ModifyTransactionPwActivity.class));
            }

            @Override
            public void onConfirm(String password) {
                if (isEmpty(password)) {
                    toast(getString(R.string.pw_not_null));
                } else {
                    transferAccounts(password);
                }

            }

            @Override
            public void onCancel() {
                passwordDialog.dismiss();
            }
        });
        passwordDialog = builder.create();
//        passwordDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//
//                    }
//                }, 200);
//
//            }
//        });
        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SoftInputUtils.hideSoftInput(ConfirmTransferActivity.this);
            }
        });
        passwordDialog.show();
    }

    private void transferAccounts(String password) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("password", password);
            params.put("remark", bundle.getString("remarks"));
            JSONObject from = new JSONObject();
            from.put("currency", Integer.parseInt(bundle.getString("currency_id")));
            from.put("amount", Integer.parseInt(bundle.getString("number")));
            params.put("from", from);
            JSONObject to = new JSONObject();
            to.put("id", Integer.parseInt(bundle.getString("id")));
            params.put("to", to);
            ApiManager.getApi(UserApi.class).asset(ApiTransform.transform(params))
                    .compose(RxSchedulers.<UserApi.Bean>compose())
                    .subscribe(new BaseObserver<User>() {
                        @Override
                        protected void onSuccess(User dataBean) {
                            toast(getString(R.string.transfer_success));
                            passwordDialog.dismiss();
                            SoftInputUtils.hideSoftInput(ConfirmTransferActivity.this);
                            MainActivity.start(ConfirmTransferActivity.this);
                        }

                        @Override
                        protected void onFailed(String msg) {
                            super.onFailed(msg);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
