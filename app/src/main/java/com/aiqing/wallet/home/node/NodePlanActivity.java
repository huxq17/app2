package com.aiqing.wallet.home.node;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.aiqing.basiclibrary.utils.DensityUtil;
import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.currency.CurrencyService;
import com.aiqing.wallet.home.BonusAct;
import com.aiqing.wallet.home.details.TradeRecordAct;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.utils.Utils;
import com.aiqing.wallet.widget.AnimDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点计划
 */
public class NodePlanActivity extends BaseActivity implements View.OnClickListener {
    private TextView currency_tv, total_assets, vdo, usd, accumulative_profit;

    private String mCurrency;
    List<String> currencyNameList = new ArrayList<>();//币种名称数据
    private CurrencyService currencyService;
    private String rate;//汇率

    public static void start(Context context, String rate) {
        Intent intent = new Intent(context, NodePlanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("rate", rate);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_plan);
        if (savedInstanceState == null) {
            rate = getIntent().getStringExtra("rate");
        } else {
            rate = savedInstanceState.getString("rate");
        }
        setTitle(R.string.node_plan);
        showBackButton();
        showMenu(getString(R.string.s_account_book), -1, 0, 0);
        initView();
        currencyService = ViewModelProviders.of(this).get(CurrencyService.class);
        currencyService.getCurrency().observe(this, new Observer<String>() {

            @Override
            public void onChanged(@Nullable String currency) {
                mCurrency = currency;
                getNodePlan();
            }
        });
    }

    private void initView() {
        currency_tv = findViewById(R.id.currency_tv);
        vdo = findViewById(R.id.vdo);
        usd = findViewById(R.id.usd);
        total_assets = findViewById(R.id.total_assets);
        accumulative_profit = findViewById(R.id.accumulative_profit);

        Drawable drawable = getResources().getDrawable(R.drawable.iv_title_menu);
        int width = DensityUtil.dip2px(this, 8);
        int height = DensityUtil.dip2px(this, 4);
        drawable.setBounds(0, 0, width, height);
        currency_tv.setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    public void onMenuClick() {
        super.onMenuClick();
        TradeRecordAct.start(this, rate, 2);
    }

    Intent intent;
    Bundle bundle;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.currency_tv:
                showCurrency();
                break;
            case R.id.node_income_explain:
                BonusAct.start(this, ApiManager.getStaticBaseUrl() + "article.html", getString(R.string.node_plan_explain));
                break;
            case R.id.participation_node:
                intent = new Intent(this, ParticipationNodeActivity.class);
                bundle = new Bundle();
                bundle.putInt("active", 1);//1转入受益区，-1转出收入区
                bundle.putInt("currency", Currency);
                bundle.putString("BalanceB", BalanceB);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.turn_out_node:
                intent = new Intent(this, ParticipationNodeActivity.class);
                bundle = new Bundle();
                bundle.putInt("active", -1);//1转入受益区，-1转出收入区
                bundle.putInt("currency", Currency);
                bundle.putString("BalanceB", BalanceB);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void showCurrency() {
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
        AnimDialog bottomAnimDialog = new AnimDialog(this);
        int itemHeight = DensityUtil.dip2px(this, 50);
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
                            currency_tv.setText(text);
                        }
                    }
                })
                .build();
        bottomAnimDialog.show();
    }

    int Currency;
    String BalanceB;

    private void getNodePlan() {
        ApiManager.getApi(NodePlanApi.class).getList()
                .compose(RxSchedulers.<NodePlanApi.Bean>compose())
                .subscribe(new BaseObserver<NodePlanBean>() {
                    @Override
                    protected void onSuccess(NodePlanBean dataBean) {
                        double result = Utils.adddouble(dataBean.getEarning().getActualSelf(), dataBean.getEarning().getActualTeam(), dataBean.getEarning().getActualVip());
                        vdo.setText(String.valueOf(Utils.keepFourBits(result)) + " " + getString(R.string.vdo));

                        usd.setText(getString(R.string.approximately_dollar) + " " + String.valueOf(Utils.keepFourBits(Utils.Multiply(result, Double.valueOf(rate)))));

                        total_assets.setText(getString(R.string.total) + Utils.keepFourBits(dataBean.getAsset().getBalanceA()) + " " + getString(R.string.vdo));

                        double rt = Utils.adddouble(dataBean.getAccumulated().getEarningSelf(), dataBean.getAccumulated().getEarningTeam(), dataBean.getAccumulated().getEarningVip());
                        accumulative_profit.setText(getString(R.string.accumulative_profit) + String.valueOf(Utils.keepFourBits(rt)) + " " + getString(R.string.vdo));
                        Currency = dataBean.getAsset().Currency;
                        BalanceB = dataBean.getAsset().getBalanceB();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("rate", rate);
    }
}
