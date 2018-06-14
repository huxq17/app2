package com.aiqing.wallet.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.widget.IWebView;


public class ProtocolAct extends BaseActivity {
    IWebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        setTitle(R.string.s_user_protocol);
        showBackButton();
        webView = findViewById(R.id.wv_protocol);
        webView.loadUrl(ApiManager.getStaticBaseUrl() + "user_protocol.html");

    }

    public static void start(Activity context) {
        Intent intent = new Intent(context, ProtocolAct.class);
        context.startActivity(intent);
    }
}
