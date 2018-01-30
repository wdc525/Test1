package com.example.administrator.test1.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test1.R;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageButton ibMenu;
    private ImageButton ibBack;
    private ImageButton ibTextsize;
    private ImageButton ibShare;
    private ImageButton ibSwichListGrid;
    private Button btnCart;

    private WebView webview;
    private ProgressBar pbLoading;
    private String url;
    private WebSettings webSettings;


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-01-03 09:59:55 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ibMenu = (ImageButton) findViewById(R.id.ib_menu);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibTextsize = (ImageButton) findViewById(R.id.ib_textsize);
        ibShare = (ImageButton) findViewById(R.id.ib_share);
        ibSwichListGrid = (ImageButton) findViewById(R.id.ib_switch_list_grid);
        //btnCart = (Button)findViewById( R.id.btn_cart );

        webview = (WebView) findViewById(R.id.webview);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);


        tvTitle.setVisibility(View.GONE);
        ibMenu.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);

        ibBack.setOnClickListener(this);
        ibTextsize.setOnClickListener(this);
        ibShare.setOnClickListener(this);
        // ibSwichListGrid.setOnClickListener( this );
        // btnCart.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-01-03 09:59:55 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == ibBack) {
            // Handle clicks for ibBack
            finish();
        } else if (v == ibTextsize) {
            // Handle clicks for ibTextsize
            //Toast.makeText(NewsDetailActivity.this, "设置文字大小", Toast.LENGTH_LONG).show();
            showChangeTextSizeDialog();
        } else if (v == ibShare) {
            // Handle clicks for ibShare
            Toast.makeText(NewsDetailActivity.this, "设置分享", Toast.LENGTH_LONG).show();
        }
    }
    //设置缓存

    private int tempSize = 2;
    private int realSize =tempSize;

    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");
        String[] item = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        builder.setSingleChoiceItems(item, tempSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSize =which;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realSize = tempSize;
                changeTextSize(realSize);
            }


        });
        builder.show();


    }

    private void changeTextSize(int realSize) {
        switch (realSize) {
            case 0://超大
                webSettings.setTextZoom(200);
                break;
            case 1://大字体
                webSettings.setTextZoom(150);
                break;
            case 2://正常字体
                webSettings.setTextZoom(100);
                break;
            case 3://小字体
                webSettings.setTextZoom(75);
                break;
            case 4://超小ziti
                webSettings.setTextZoom(50);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        findViews();
        getData();
    }

    private void getData() {
        url = getIntent().getStringExtra("url");
        //设置支持js
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置双击变大小
        webSettings.setUseWideViewPort(true);

        //增加缩放按钮
        webSettings.setBuiltInZoomControls(true);
        //设置文字大小
        //webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setTextZoom(100);
        //设置不让从当前页面跳转到系统的浏览器中
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });

        webview.loadUrl(url);
        //webview.loadUrl("http://www.baidu.com");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
