package com.aiqing.wallet.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.TextureView;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aiqing.basiclibrary.utils.DensityUtil;
import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.user.UserService;
import com.aiqing.wallet.utils.ShareUtils;
import com.aiqing.wallet.widget.IWebView;

public class BonusAct extends BaseActivity {

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, BonusAct.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void start(Context context, String url, String title) {
        Intent intent = new Intent(context, BonusAct.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    private String url, title;
    private IWebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
        } else {
            url = savedInstanceState.getString("url");
            title = savedInstanceState.getString("title");
        }
        if (TextUtils.isEmpty(url)) {
            finish();
        }
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }else{
            setTitle(R.string.s_request_ing);
        }

        setContentView(R.layout.activity_notice_details);
        showBackButton();
        int size = DensityUtil.dip2px(this, 15);
//        showMenu("", R.drawable.iv_share, size, size);
//        setTitle(R.string.s_home_bonous);
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

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(BonusAct.this.title))
                    setTitle(title);
            }

        });
        webView.addJavascriptInterface(this, "android");
    }

    @Override
    public void onMenuClick() {
        ShareUtils.share(this, "", getTitle() + " " + url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @android.webkit.JavascriptInterface
    public void toDetails(final String url) {
        BonusAct.start(this, url + "?token=" + UserService.getUserToken());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("url", url);
        outState.putString("title", title);
    }
}
