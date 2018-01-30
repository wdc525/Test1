package com.example.administrator.test1.menudetailpager.tabdetailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test1.R;
import com.example.administrator.test1.activity.NewsDetailActivity;
import com.example.administrator.test1.base.MenuDetailBasePager;
import com.example.administrator.test1.domain.NewsCenterPagerBean;
import com.example.administrator.test1.domain.TabDetailPagerBean;
import com.example.administrator.test1.utils.CacheUitls;
import com.example.administrator.test1.utils.Constants;
import com.example.administrator.test1.utils.DensityUtil;
import com.example.administrator.test1.utils.LogUtil;
import com.example.administrator.test1.view.HorizontalScrollViewPager;
import com.example.refreshlistview.RefreshListview;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2017-12-10.
 */

public class TabDetailPager extends MenuDetailBasePager {
    public static final String READ_ARRAY_ID = "read_array_id";
    private HorizontalScrollViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private RefreshListview listview;
    private ImageOptions imageOptions;


    private List<TabDetailPagerBean.DataBean.NewsBean> news;
    private TabDetailPagerListAdapter adapter;

    private final NewsCenterPagerBean.DataBean.ChildrenBean childrenBean;
    //private TextView textView;
    private String url;

    List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    private String moreUrl;
    //是否加载更多
    private boolean isLoadMore;
    private List<TabDetailPagerBean.DataBean.NewsBean> morenews;
    private TabDetailPagerBean.DataBean.NewsBean newsBean;
    private InternalHandler internalHandler;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dp2px(context, 100), DensityUtil.dp2px(context, 100))
                .setRadius(DensityUtil.dp2px(context, 5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    public View initView() {
//        textView = new TextView(context);
//        textView = new TextView(context);
//        textView.setTextSize(23);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.RED);
//        return textView;

        View view = View.inflate(context, R.layout.tabdetail_pager, null);
        listview = (RefreshListview) view.findViewById(R.id.listview);

        View topNesView = View.inflate(context, R.layout.topnews, null);
        viewpager = (HorizontalScrollViewPager) topNesView.findViewById(R.id.viewpager);
        tv_title = (TextView) topNesView.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) topNesView.findViewById(R.id.ll_point_group);
//把顶部轮播图部分视图，以头的方式添加到ListView中；
        // listview.addHeaderView(topNesView);

        listview.addTopNewsView(topNesView);


        //设置监听下拉刷新
        listview.setOnRefreshListener(new MyOnRefreshListener());

        //设置listview的item的点击监听
        listview.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int realPositon = position - 1;
            newsBean = news.get(realPositon);
            Toast.makeText(context, "newsBean==id==" + newsBean.getId() + "newsBean_title==" + newsBean.getTitle(), Toast.LENGTH_LONG).show();
            LogUtil.e("newsBean==id==" + newsBean.getId() + "newsBean_title==" + newsBean.getTitle() + "url==" + newsBean.getUrl());
            //1.取出保存的ID集合
            String idArray = CacheUitls.getString(context, READ_ARRAY_ID);
            //2.判断是否存在，如果不存在，才保存，并刷新适配器
            if (!idArray.contains(newsBean.getId() + "")) {
                CacheUitls.putString(context, READ_ARRAY_ID, idArray + newsBean.getId() + ",");
                //刷新适配器
                adapter.notifyDataSetChanged(); //getcount->getView
            }


            //
            //跳转到新闻浏览页面   71
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("url", Constants.BASE_URL + newsBean.getUrl());
            context.startActivity(intent);


        }
    }

    class MyOnRefreshListener implements RefreshListview.OnRefreshListener {

        @Override
        public void onPullDownRefresh() {
            Toast.makeText(context, "下拉刷新。。。", Toast.LENGTH_SHORT).show();
            getDataFromNet();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onLoadMore() {

//            Toast.makeText(context, "加载更多。。。", Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(moreUrl)) {
                //没有更多数据
                Toast.makeText(context, "没有跟多。。。", Toast.LENGTH_SHORT).show();
                listview.onRefreshFinish(false);

            } else {
                getMoreDataFromNet();
            }

        }


    }

    /**
     * 获取更多的数据
     */
    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(String result) {
                LogUtil.e("加载更多联网成功==" + result);
                listview.onRefreshFinish(false);
                //一定放到前面
                isLoadMore = true;
                //解析数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean b) {
                LogUtil.e("加载更多联网失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException e) {
                LogUtil.e("加载更多联网取消==" + e.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("加载更多联网完成");
            }
        });


    }

    @Override
    public void initData() {
        super.initData();
        // textView.setText(childrenBean.getTitle());
        url = Constants.BASE_URL + childrenBean.getUrl();
        //把之前缓存的数据取出
        String savaJson = CacheUitls.getString(context, url);

        if (!TextUtils.isEmpty(savaJson)) {
            //解析数据
            processData(savaJson);
        }
        LogUtil.e(childrenBean.getTitle() + "的联网地址=" + url);
//        联网请求数据
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(String result) {
                //LogUtil.e(childrenBean.getTitle() + "页面数据请求成功==" + result);
                processData(result);
                //隐藏下拉刷新控件 ，重新显示数据，更新时间（后加）
                listview.onRefreshFinish(true);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onError(Throwable throwable, boolean b) {
                // LogUtil.e(childrenBean.getTitle() + "页面数据请求失败==" + throwable.getMessage());
                //隐藏下拉刷新控件 ，不更新时间 ，只隐藏
                listview.onRefreshFinish(false);

            }

            @Override
            public void onCancelled(CancelledException e) {
                LogUtil.e(childrenBean.getTitle() + "页面数据请onCancelled==" + e.getMessage());
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 之前点高亮的位置
     */
    private int prePosition;

    private void processData(String json) {
        TabDetailPagerBean bean = parsedJson(json);
        LogUtil.e(childrenBean.getTitle() + "解析成功" + bean.getData().getNews().get(0).getTitle());

        moreUrl = "";

        if (TextUtils.isEmpty(bean.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + moreUrl + bean.getData().getMore();
        }

        //默认和加载更多
        if (!isLoadMore) {
            //顶部轮播图的数据
            topnews = bean.getData().getTopnews();
            int a = topnews.size();
            LogUtil.e("顶部轮播图的数量：" + a);
            TabDetailPagerTopNewsAdapter tabAdapter = new TabDetailPagerTopNewsAdapter();
            viewpager.setAdapter(tabAdapter);
            ll_point_group.removeAllViews(); //移除所有的红点

            //监听页面的改变，设置红点变化和文本变化
            viewpager.addOnPageChangeListener(new MyOnPageChangerListener());
            tv_title.setText(topnews.get(prePosition).getTitle());

            addPoint();

            news = bean.getData().getNews();
            adapter = new TabDetailPagerListAdapter();
            listview.setAdapter(adapter);
        } else {
            //加载更多
            isLoadMore = false;
            morenews = bean.getData().getNews();
            //添加原来的集合中
            news.addAll(morenews);
            //刷先适配器
            adapter.notifyDataSetChanged();
        }
        //发送消息每隔4000切换一次viewPager页面
        if (internalHandler == null) {
            internalHandler = new InternalHandler();
        }

        internalHandler.removeCallbacksAndMessages(null);
        internalHandler.postDelayed(new MyRunnable(), 4000);


    }

    class InternalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //切换viewpager的下一个页面
            int item = (viewpager.getCurrentItem() + 1) % topnews.size();
            viewpager.setCurrentItem(item);
        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            internalHandler.sendEmptyMessage(0);
        }
    }


    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;

    }

    private void addPoint() {
        ll_point_group.removeAllViews(); //移除所有的红点
        for (int i = 0; i < topnews.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dp2px(context, 8), DensityUtil.dp2px(context, 8));
            if (i == 0) {
                imageView.setEnabled(true);

            } else {
                imageView.setEnabled(false);
                params.leftMargin = DensityUtil.dp2px(context, 8);
            }

            imageView.setLayoutParams(params);
            ll_point_group.addView(imageView);
        }
    }

    class MyOnPageChangerListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //1、设置文本
            tv_title.setText(topnews.get(position).getTitle());
            //2、对应页面的点高亮-红色
            //把之前的变成灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //把当前设置红色
            ll_point_group.getChildAt(position).setEnabled(true);

            prePosition = position;


        }

        private boolean isDragging = false;

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {//拖拽
                isDragging = true;
                //拖拽要移除消息
                internalHandler.removeCallbacksAndMessages(null);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {//惯性
                isDragging = false;
                internalHandler.removeCallbacksAndMessages(null);
                internalHandler.postDelayed(new MyRunnable(), 4000);
                //发消息
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {//静止
                isDragging = false;
                internalHandler.removeCallbacksAndMessages(null);
                internalHandler.postDelayed(new MyRunnable(), 4000);
            }
        }
    }


    class TabDetailPagerTopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            //设置图片默认  北京
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);


            container.addView(imageView);
            //联网请求图片
            TabDetailPagerBean.DataBean.TopnewsBean topnewsBean = topnews.get(position);
            String imageUrl = Constants.BASE_URL + topnewsBean.getTopimage();
            LogUtil.e(imageUrl);
            x.image().bind(imageView, imageUrl);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://按下
                            LogUtil.e("按下");
                            internalHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP://离开
                            LogUtil.e("离开");
                            internalHandler.removeCallbacksAndMessages(null);
                            internalHandler.postDelayed(new MyRunnable(), 4000);
                            break;
//                        case MotionEvent.ACTION_CANCEL://取消
//                            LogUtil.e("取消");
//                            internalHandler.removeCallbacksAndMessages(null);
//                            internalHandler.postDelayed(new MyRunnable(), 4000);
//                            break;


                    }
                    return true;
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    class TabDetailPagerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_tabdetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }
            TabDetailPagerBean.DataBean.NewsBean newsData = news.get(position);
            String imageUrl = Constants.BASE_URL + newsData.getListimage();

            x.image().bind(viewHolder.iv_icon, imageUrl, imageOptions);
            viewHolder.tv_title.setText(newsData.getTitle());
            viewHolder.tv_time.setText(newsData.getPubdate());


            //-----------
            String idArray = CacheUitls.getString(context, READ_ARRAY_ID);
            if (idArray.contains(newsData.getId() + "")) {
                //设置灰色
                viewHolder.tv_title.setTextColor(Color.GRAY);
            } else {
                viewHolder.tv_title.setTextColor(Color.BLACK);
                //设置黑色
            }
            return convertView;
        }
    }

    private TabDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, TabDetailPagerBean.class);
    }
}
