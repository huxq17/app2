package com.aiqing.wallet.home.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aiqing.basiclibrary.utils.DensityUtil;
import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.utils.ShareUtils;
import com.aiqing.wallet.widget.IWebView;

public class DetailsAct extends BaseActivity {

    public static void start(Context context, String url, int type) {
        Intent intent = new Intent(context, DetailsAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("url", url);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    //type: 1:banner 2:资讯 3:公告
    private int type;
    private String url;
    private IWebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            url = getIntent().getStringExtra("url");
            type = getIntent().getIntExtra("type", -1);
        } else {
            url = savedInstanceState.getString("url");
            type = savedInstanceState.getInt("type");
        }
        if (type == -1 || TextUtils.isEmpty(url)) {
            finish();
        }
        setContentView(R.layout.activity_notice_details);
        showBackButton();
        int size = DensityUtil.dip2px(this, 15);
        showMenu("", R.drawable.iv_share, size, size);
        if (type == 3) {
            setTitle(R.string.s_notice_details);
        } else if (type == 2) {
            setTitle(R.string.s_news_details);
        } else {
            setTitle(getString(R.string.s_request_ing));
        }
        webView = findViewById(R.id.wv_details);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return shouldOverrideUrlLoading(view, request.getUrl().toString());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    return true;
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (type == 1) {
                    setTitle(title);
                }
            }
        });
    }

    @Override
    public void onMenuClick() {
        ShareUtils.share(this,"", getTitle() +" " +url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("url", url);
        outState.putInt("type", type);
    }
}
