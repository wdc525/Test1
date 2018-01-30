package com.atguigu.androidandh5;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/28 11:19
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：java和js互调
 */
public class JsCallJavaCallPhoneActivity extends Activity {


    private WebSettings webSettings;
    private WebView webView;

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
        webView.loadUrl("file:///android_asset/JsCallJavaCallPhone.html");

    }

    class MyJavascriptInterface {
        //拨打电话了
        @JavascriptInterface
        public void call(String phone) {
            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + phone));
            if (ActivityCompat.checkSelfPermission(JsCallJavaCallPhoneActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);

        }

        //加载联系人
        @JavascriptInterface
        public void showcontacts() {
            String json = "[{\"name\":\"sha尚硅谷,\"phone\":\"18888888888\"}]";
            //调用js中的方法
            //webView.loadUrl("javascript:show("+"'"+json+"'"+")");
            webView.loadUrl("javascript:show('"+json+"')");
        }
    }


}
