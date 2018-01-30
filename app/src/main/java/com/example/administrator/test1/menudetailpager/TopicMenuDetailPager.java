package com.example.administrator.test1.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.test1.R;
import com.example.administrator.test1.activity.Main;
import com.example.administrator.test1.base.MenuDetailBasePager;
import com.example.administrator.test1.domain.NewsCenterPagerBean;
import com.example.administrator.test1.menudetailpager.tabdetailpager.TopicDetailPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-03.
 */

public class TopicMenuDetailPager extends MenuDetailBasePager {


    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;
    //
    //private TabPageIndicator tabPageIndicator;
    @ViewInject(R.id.tabLayout)
    private TabLayout tabLayout;
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    //页签页面的数据
    private List<NewsCenterPagerBean.DataBean.ChildrenBean> children;

    //页签页面的集合

    private ArrayList<TopicDetailPager> tabDetailPagers;

    /**
     * 页签页面的集合
     *
     * @param context
     * @param dataBean
     */

    private TextView textView;

    public TopicMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        children = dataBean.getChildren();
    }

    @Override
    public View initView() {

        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
//
        // return textView;
        //View view = View.inflate(context, R.layout.base_pager, null);
        View view = View.inflate(context, R.layout.topicmenu_detail_pager, null);
        x.view().inject(this, view);

        //设置点击事件

        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        return view;

    }

    @Override
    public void initData() {

        LogUtil.e("专题初始化了");
        textView.setText("专题详情页面内容");

        //准备专题详情页面的数据
        tabDetailPagers = new ArrayList<>();

        for (int i = 0; i < children.size(); i++) {
            tabDetailPagers.add(new TopicDetailPager(context, children.get(i)));
        }
        //设置viewpager的适配器
        viewPager.setAdapter(new TopicMenuDetailPager.MyNewsMenuDtailPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        //主页以后监听页面的变化，tabPageIndicator监听页面的变化
        //tabLayout.setOnPageChangeListener(new TopicMenuDetailPager.MyONPageChangeListener());
        viewPager.addOnPageChangeListener(new TopicMenuDetailPager.MyONPageChangeListener());
        //设置滑动或者固定
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            tab.setCustomView(getTabView(i));
//        }


    }
    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(children.get(position).getTitle());
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(R.drawable.dot_focus);
        return view;
    }


    class MyONPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                //SlidingMenu滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            } else {
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private void isEnableSlidingMenu(int fullmode) {
        Main main = (Main) context;
        main.getSlidingMenu().setTouchModeAbove(fullmode);
    }

    class MyNewsMenuDtailPagerAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TopicDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootview;
            tabDetailPager.initData();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
