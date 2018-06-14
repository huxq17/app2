package com.aiqing.wallet.home.transferaccounts;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiqing.basiclibrary.utils.DensityUtil;
import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.currency.CurrencyService;
import com.aiqing.wallet.currency.RateApi;
import com.aiqing.wallet.currency.RateBean;
import com.aiqing.wallet.firend.FriendActivity;
import com.aiqing.wallet.firend.FriendBean;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;
import com.aiqing.wallet.widget.AnimDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 转账
 */
public class TransferAccountsActivity extends BaseActivity {
    private EditText account_number, number, remarks;
    private TextView currency, mobile, service_charge;
    private LinearLayout mobile_lay;
    private String formalities;
    //    List<CurrencyBean.Currency> currencyList = new ArrayList<>();//币种数据
    List<String> currencyNameList = new ArrayList<>();//币种名称数据
    private CurrencyService currencyService;
    private String mCurrency;

    public static void start(Context context, String rate) {
        Intent intent = new Intent(context, TransferAccountsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("rate", rate);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_accounts);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.transfer_accounts_title));
        showBackButton();
        initView();
        setListener();
        currencyService = ViewModelProviders.of(this).get(CurrencyService.class);
        currencyService.getCurrency().observe(this, new Observer<String>() {

            @Override
            public void onChanged(@Nullable String currency) {
                mCurrency = currency;
            }
        });
    }

    private void initView() {
        account_number = findViewById(R.id.account_number);
        number = findViewById(R.id.number);
        currency = findViewById(R.id.currency);
        mobile_lay = findViewById(R.id.mobile_lay);
        mobile = findViewById(R.id.mobile);
        service_charge = findViewById(R.id.service_charge);
        remarks = findViewById(R.id.remarks);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.friend_list:
                startActivityForResult(new Intent(this, FriendActivity.class), 0);
                break;
            case R.id.next:
                if (isEmpty(getText(account_number)) || isEmpty(getText(number)) || isEmpty(getText(currency))) {
                    toast(getString(R.string.s_blank_info));
                    return;
                }
                Intent intent = new Intent(this, ConfirmTransferActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", ID);
                bundle.putString("account_number", getText(account_number));
                bundle.putString("currency", getText(currency));
                bundle.putString("number", getText(number));
                bundle.putString("remarks", getText(remarks));
                bundle.putString("service_charge", formalities);
                bundle.putString("phone", getText(mobile));
                bundle.putString("currency_id", getCurrency(getText(currency)));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.currency:
                if (TextUtils.isEmpty(mCurrency)) {
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(mCurrency);
                    currencyNameList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        if (jsonObject.optString("type").equals("0")) {
                            currencyNameList.add(jsonObject.optString("name"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final Context context = v.getContext();
                AnimDialog bottomAnimDialog = new AnimDialog((Activity) context);
                int itemHeight = DensityUtil.dip2px(context, 50);
                for (String currency : currencyNameList) {
                    bottomAnimDialog.addItem(new AnimDialog.Item(currency).height(itemHeight));
                }
                bottomAnimDialog.addItem(new AnimDialog.Item(getString(R.string.s_cancel)).height(itemHeight))
                .gravity(Gravity.BOTTOM);
                bottomAnimDialog
                        .setOnItemClickListener(new AnimDialog.OnItemClickListener() {
                            @Override
                            public void onItemClick(String text) {
                                if (text.equals(getString(R.string.s_cancel))) {
                                } else {
                                    currency.setText(text);
                                    getRate();
                                }
                            }
                        })
                        .build();
                bottomAnimDialog.show();
                break;
        }
    }

    private String getCurrency(String name) {
        try {
            JSONArray jsonArray = new JSONArray(mCurrency);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                if (jsonObject.optString("name").equals(name)) {
                    return jsonObject.optString("iD");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (data != null) {
                    FriendBean.Friend.DataBean account = (FriendBean.Friend.DataBean) data.getSerializableExtra("friend");
                    account_number.setText(account.getUsername());
                    findUser(getText(account_number));
                }
                break;
        }
    }

    private void setListener() {
        account_number.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    findUser(getText(account_number));
                }
            }
        });
        account_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEmpty(s.toString())) {
                    mobile_lay.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    String ID;//转入用户ID

    private void findUser(String username) {
        if (isEmpty(username)) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        ApiManager.getApi(UserApi.class).findUser(ApiTransform.addUdid(params))
                .compose(RxSchedulers.<UserApi.Bean>compose())
                .subscribe(new BaseObserver<User>(this) {
                    @Override
                    protected void onSuccess(User dataBean) {
                        mobile.setText(dataBean.Mobile);
                        ID = dataBean.ID;
                        mobile_lay.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }

    private void getRate() {
        ApiManager.getApi(RateApi.class).getRate(1)
                .compose(RxSchedulers.<RateApi.Bean>compose())
                .subscribe(new BaseObserver<RateBean>() {
                    @Override
                    protected void onSuccess(RateBean rateBean) {
                        for (int i = 0; i < rateBean.getList().size(); i++) {
                            if (rateBean.getList().get(i).getType() == 0 && rateBean.getList().get(i).getFromCurrency() == 1) {
                                formalities = rateBean.getList().get(i).getFeeAmount();
                                break;
                            }
                        }
                        service_charge.setText(getString(R.string.service_charge_colon) + formalities + " " + getString(R.string.vdo));
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }

}
