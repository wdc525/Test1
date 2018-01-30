package com.example.administrator.test1.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.administrator.test1.base.BasePager;


/**
 * Created by Administrator on 2017/10/3.
 */

public class GovaffairPager extends BasePager {
    public GovaffairPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //1、设置标题
        tv_title.setText("政要页面");
        //2、联网请求，得到数据，创建视图
        TextView textView = new TextView(context);

        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);


        //3、把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //4、绑定数据
        textView.setText("政要页面内容");
    }
}
