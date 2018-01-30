package com.example.administrator.test1;

import android.app.Application;

import com.example.administrator.test1.volley.VolleyManager;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017-12-03.
 */

public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.setDebug(true);
        x.Ext.init(this);
        //初始化volley

        VolleyManager.init(this);

        //初始化激光推送
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
}
