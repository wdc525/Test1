package com.atguigu.androidandh5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/28 11:19
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：java和js互调
 */
public class JsCallJavaVideoActivity extends Activity {
    private WebView webView;
    private WebSettings webSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_java_video);
        webView = (WebView) findViewById(R.id.webView);

        //设置支持js
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置双击变大小
        // webSettings.setUseWideViewPort(true);

        //增加缩放按钮
        webSettings.setBuiltInZoomControls(true);
        //设置文字大小
        //webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setTextZoom(100);
        //设置不让从当前页面跳转到系统的浏览器中
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webView.addJavascriptInterface(new MyJavascriptInterface(),"Android");
        webView.loadUrl("file:///android_asset/RealNetJSCallJavaActivity.htm");

    }

    private class MyJavascriptInterface {

    }
}
