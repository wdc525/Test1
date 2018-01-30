package com.example.administrator.test1.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.administrator.test1.R;
import com.example.administrator.test1.activity.Main;

/**
 * Created by Administrator on 2017-11-01.
 */

public class BasePager {

    public final Context context;
    public View rootview;
    public TextView tv_title;
    public ImageButton ib_menu;
    public FrameLayout fl_content;
    public ImageButton ib_switch_list_grid;

    public BasePager(Context context){
        this.context = context;
        //构造方法一执行，视图就被初始化了
        rootview = initView();
    }
    private View initView(){
        View view = View.inflate(context, R.layout.base_pager, null);

        ib_switch_list_grid = (ImageButton)view.findViewById(R.id.ib_switch_list_grid);

        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main mainActivity = (Main) context;
                mainActivity.getSlidingMenu().toggle();
            }
        });
        return view;
    }
    public void initData(){

    }


}
