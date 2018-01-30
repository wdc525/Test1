package com.example.administrator.test1.fragment;

import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.test1.R;
import com.example.administrator.test1.activity.Main;
import com.example.administrator.test1.base.BaseFragment;
import com.example.administrator.test1.base.BasePager;
import com.example.administrator.test1.pager.GovaffairPager;
import com.example.administrator.test1.pager.HomePager;
import com.example.administrator.test1.pager.NewsCenterPager;
import com.example.administrator.test1.pager.SettingPager;
import com.example.administrator.test1.pager.SmartServicePager;
import com.example.administrator.test1.utils.LogUtil;
import com.example.administrator.test1.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-12-03.
 */

public class ContentFragment extends BaseFragment{

    private TextView textView;
    private NoScrollViewPager viewpager;
    private RadioGroup rg_main;
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        LogUtil.e("正文Fragment被初始化了");
//        textView = new TextView(context);
//        textView.setTextSize(23);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.RED);

        View view = View.inflate(context, R.layout.content_fragment, null);
        viewpager = (NoScrollViewPager ) view.findViewById(R.id.viewpager);
        rg_main = (RadioGroup) view.findViewById(R.id.rg_main);

        x.view().inject(ContentFragment.this,view);


        return view;
    }

    @Override
    public void initData() {
        LogUtil.e("正文Fragment数据被初始化了");
        super.initData();
       // textView.setText("主页菜单页面Fragment");
        //初始化5个页面，放入集合
        basePagers = new ArrayList<>();

        basePagers.add(new HomePager(context));
        basePagers.add(new NewsCenterPager(context));
        basePagers.add(new SmartServicePager(context));
        basePagers.add(new GovaffairPager(context));
        basePagers.add(new SettingPager(context));  //设置页面


        //设置适配器
        viewpager.setAdapter(new ContentFragmentAdapter());
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //监听某个页面被选中，初始化对应的界面
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //默认首页
        rg_main.check(R.id.rb_home);

        basePagers.get(0).initData();
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
    }

    public NewsCenterPager getNewsConterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
           BasePager basePager= basePagers.get(position);
            basePager.initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.rb_home:
                    viewpager.setCurrentItem(0, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_newscenter:
                    viewpager.setCurrentItem(1, false);
                   isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);

                    break;
                case R.id.rb_smartservice:
                    viewpager.setCurrentItem(2, false);
                  isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_govaffair:
                    viewpager.setCurrentItem(3, false);
                   isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting:
                    viewpager.setCurrentItem(4, false);
                   isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }


    }

    private void isEnableSlidingMenu(int fullmode) {
        Main main = (Main) context;
        main.getSlidingMenu().setTouchModeAbove(fullmode);
    }

    class ContentFragmentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = basePagers.get(position); //各个页面的实例
            View rootView = basePager.rootview;   //各个子页面
            //调用各个页面的initData（）
            //basePager.initData(); //初始化数据
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
