package com.atguigu.testvolley;

import android.app.Application;

import com.atguigu.testvolley.volley.VolleyManager;

/**
 * Created by Administrator on 2016/1/6.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {//所有的组件被实例化之前被调用
        super.onCreate();
        VolleyManager.init(this);
    }
}
