package com.example.administrator.test1.activity;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.administrator.test1.R;
import com.example.administrator.test1.fragment.ContentFragment;
import com.example.administrator.test1.fragment.LeftMenuFragment;
import com.example.administrator.test1.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class Main extends SlidingFragmentActivity {

    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFTMENU_TAG = "leftmenu_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有标题
        super.onCreate(savedInstanceState);
        //设置主页面
        setContentView(R.layout.activity_main2);

        //2、设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);

        //3、设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);

        //4、设置显示的模式：
        //slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        slidingMenu.setMode(SlidingMenu.LEFT);
        //5、设置滑动模式：滑动边缘，全屏滑动、不互动

        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //6、设置主页占据的宽度
        slidingMenu.setBehindOffset(DensityUtil.dp2px(Main.this,200));

        //初始化Fragment
        initFragment();


    }

    private void initFragment() {
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_main_content, new ContentFragment(), MAIN_CONTENT_TAG);
        ft.replace(R.id.fl_leftmenu, new LeftMenuFragment(), LEFTMENU_TAG);
        ft.commit();
    }

    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm= getSupportFragmentManager();
        return (LeftMenuFragment) fm.findFragmentByTag(LEFTMENU_TAG);
    }

    public ContentFragment getContentFragment() {
        FragmentManager fm= getSupportFragmentManager();
        return (ContentFragment) fm.findFragmentByTag(MAIN_CONTENT_TAG);
    }
}
