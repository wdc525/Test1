package com.example.administrator.test1.base;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017-11-03.
 */

public abstract class MenuDetailBasePager {
    public final Context context;


    public View rootview;

    public MenuDetailBasePager(Context context){
        this.context = context;
        rootview = initView();
    }

    public abstract View initView();

    public void initData(){

    }
}
