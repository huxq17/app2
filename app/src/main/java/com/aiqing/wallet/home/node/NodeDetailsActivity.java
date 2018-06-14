package com.aiqing.wallet.home.node;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.utils.Utils;

import java.math.BigDecimal;

/**
 * 节点详情
 */
public class NodeDetailsActivity extends BaseActivity {
    private TextView type, vdo;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_details);
        setTitle(R.string.detail_details);
        showBackButton();
        initView();
    }

    private void initView() {
        type = findViewById(R.id.type);
        vdo = findViewById(R.id.vdo);

        bundle = getIntent().getExtras();
        type.setText(bundle.getString("Title"));
        BigDecimal first_amount = new BigDecimal(Double.valueOf(bundle.getString("Amount")));
        BigDecimal one_amount = new BigDecimal(0.00);
        if (first_amount.compareTo(one_amount) > 0) {
            vdo.setText("+" + Utils.keepFourBits(Double.valueOf(bundle.getString("Amount"))) + getString(R.string.vdo));
            vdo.setTextColor(Color.parseColor("#ff8106"));
        } else {
            vdo.setText(Utils.keepFourBits(Double.valueOf(bundle.getString("Amount"))) + getString(R.string.vdo));
            vdo.setTextColor(Color.parseColor("#4D4D4D"));
        }
    }
}
