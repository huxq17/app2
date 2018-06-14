package com.aiqing.wallet.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseActivity;


public class AboutUsAct extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTitle(R.string.about_us);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        showBackButton();
//        findViewById(R.id.settings_safe).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpToSafeSettings();
//            }
//        });
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutUsAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
