package com.aiqing.wallet.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseActivity;

public class MoneyDetailsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout recharge, withdraw_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_details);
        initView();
        initListener();
    }

    private void initView() {
        recharge = findViewById(R.id.recharge);
        withdraw_money = findViewById(R.id.withdraw_money);
    }

    private void initListener() {
        recharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.recharge:
                break;
            case R.id.withdraw_money:
                break;
        }
    }
}
