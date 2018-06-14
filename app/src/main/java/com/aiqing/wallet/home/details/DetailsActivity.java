package com.aiqing.wallet.home.details;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;
import com.aiqing.wallet.utils.Utils;

import java.util.HashMap;

public class DetailsActivity extends BaseActivity {
    private TextView type, amount, account, mobile, currency, service_charge, remarks;

    Bundle bundle;
    private int AccountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.detail));
        showBackButton();
        initView();
        getDetails();
    }

    private void initView() {
        type = findViewById(R.id.type);
        amount = findViewById(R.id.amount);
        account = findViewById(R.id.account);
        mobile = findViewById(R.id.mobile);
        service_charge = findViewById(R.id.service_charge);

        bundle = getIntent().getExtras();
        remarks = findViewById(R.id.remarks);
        if (bundle.getString("Type").equals("fee")) {
            type.setText(getString(R.string.service_charge));
        } else if (bundle.getString("Type").equals("credit")) {
            type.setText(getString(R.string.transfer_account));
        } else if (bundle.getString("Type").equals("debit")) {
            type.setText(getString(R.string.transfer));
        }
        if (bundle.getString("Amount").indexOf("-") == -1) {
            String m = getAmount(bundle.getString("Amount"), bundle.getString("Fee"));
            amount.setText("+" + Utils.keepFourBits(Double.valueOf(m)) + " " + getString(R.string.vdo));
            amount.setTextColor(Color.parseColor("#ff8106"));
        } else {
            String m = getAmount(bundle.getString("Amount"), bundle.getString("Fee"));
            amount.setText(Utils.keepFourBits(Double.valueOf(m)) + " " + getString(R.string.vdo));
            amount.setTextColor(Color.parseColor("#4D4D4D"));
        }


        service_charge.setText(bundle.getString("Fee"));
        remarks.setText(bundle.getString("Remark2"));

    }

    private String getAmount(String a, String b) {
        return String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));
    }

    private void getDetails() {
        HashMap<String, String> params = new HashMap<>();
        AccountID = bundle.getInt("AccountID");
        ApiManager.getApi(UserApi.class).getUser(AccountID, ApiTransform.addUdid(params))
                .compose(RxSchedulers.<UserApi.Bean>compose())
                .subscribe(new BaseObserver<User>(this) {
                    @Override
                    protected void onSuccess(User dataBean) {
                        account.setText(dataBean.Nickname);
                        mobile.setText(dataBean.Mobile);

                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }
}
