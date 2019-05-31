package com.spark.newbitrade.activity.message;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.utils.StringUtils;


import butterknife.BindView;

/**
 * 平台消息详情/首页图片点击进入/首页滚动点击进入/帮助中心点击进入
 */
public class WebViewActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webView;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        initwebview();
    }

    private void initwebview() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        /**
         *  Webview在安卓5.0之前默认允许其加载混合网络协议内容
         *  在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
         *   解决图片不显示问题
         */
        webSettings.setBlockNetworkImage(false); // 解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.LOAD_NORMAL);
        }

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        //与H5交互
        webView.addJavascriptInterface(this, "android");
        webView.setBackgroundColor(getResources().getColor(R.color.white));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!StringUtils.isEmpty(view.getTitle())) {
                    if (view.getTitle().length() > 10) {
//                        tvTitle.setText(view.getTitle().substring(0, 10) + "...");
                    } else {
//                        tvTitle.setText(view.getTitle());
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean isImage = bundle.getBoolean("isImage");
            String title = bundle.getString("title");
            setTitle(title);
            if (isImage) { // 首页图片跳转链接
                String url = bundle.getString("url");
                setWebData(url);
            } else {
                String content = bundle.getString("content");
                webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
            }
        }
    }

    /**
     * 设置webview链接
     *
     * @param url
     */
    private void setWebData(String url) {
        if (StringUtils.isNotEmpty(url)) {
            webView.loadUrl(url);
        }
    }

}
