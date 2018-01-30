package com.example.administrator.test1.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;


import com.example.administrator.test1.base.BasePager;

import org.xutils.common.util.LogUtil;

/**
 * Created by Administrator on 2017/10/3.
 */

public class SmartServicePager extends BasePager {
    public SmartServicePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("智慧页面数据被初始化了。。");
        //1、设置标题
        tv_title.setText("智慧服务页面");
        //2、联网请求，得到数据，创建视图
        TextView textView = new TextView(context);

        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);


        //3、把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //4、绑定数据
        textView.setText("智慧服务页面内容");
    }
}
