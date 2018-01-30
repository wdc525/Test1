package com.example.refreshlistview;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;



import java.util.Date;

/**
 * Created by Administrator on 2017-12-18.
 */

public class RefreshListview extends ListView {
    private LinearLayout headerview;
    private View ll_pull_down_refresh;
    private ImageView iv_arrow;
    private ProgressBar pb_status;
    private TextView tv_status;
    private TextView tv_time;
    private int pullDownRefreshHeight;
    private static final int PULL_DOWN_REFRESH = 0;

    private static final int RELEASE_REFRESH = 1;

    private static final int REFRESHING = 2;

    private int currentStatus = PULL_DOWN_REFRESH;

    private Animation upAnimation;
    private Animation downAnimation;
    private View footerView;
    /**
     * 加载更多控件高
     */
    private int footerViewHerght;
    /**
     * 是否加载更多
     */

    private boolean isLoadMore = false;
    private View topNesView;
    private int listViewOnScreenY = -1;


    public RefreshListview(Context context) {
        this(context, null);
    }

    public RefreshListview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);


    }

    private void initFooterView(Context context) {
        footerView = View.inflate(context, R.layout.refresh_footer, null);
        footerView.measure(0, 0);
        footerViewHerght = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHerght, 0, 0);
        //ListView添加footer
        addFooterView(footerView);

        //监听ListView的滚动监听
        setOnScrollListener(new MyOnScrollListener());

    }

    public void addTopNewsView(View topNesView) {
        if (topNesView != null) {
            this.topNesView = topNesView;
            headerview.addView(topNesView);
        }

    }

    class MyOnScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //当静止或惯性滚动的时候
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                //并且是最后一条可见
                if (getLastVisiblePosition() == getCount() - 1) {
                    //1.显示加载更多布局
                    footerView.setPadding(8, 8, 8, 8);
                    //2.状态改变
                    isLoadMore = true;
                    //3.回调接口
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onLoadMore();
                    }
                }
            }


        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }


    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);
        downAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        headerview = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);
        ll_pull_down_refresh = headerview.findViewById(R.id.ll_pull_down_refresh);
        iv_arrow = (ImageView) headerview.findViewById(R.id.iv_arrow);
        pb_status = (ProgressBar) headerview.findViewById(R.id.pb_status);
        tv_status = (TextView) headerview.findViewById(R.id.tv_status);
        tv_time = (TextView) headerview.findViewById(R.id.tv_time);

        //测量
        ll_pull_down_refresh.measure(0, 0);
        pullDownRefreshHeight = ll_pull_down_refresh.getMeasuredHeight();

        //默认隐藏下拉刷新空间

        ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
        //添加头
        RefreshListview.this.addHeaderView(headerview);
    }

    private float startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录起始坐标
                startY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = ev.getY();
                }
                //判断顶部轮播图是否完全显示，只有完全显示，才会有下拉刷新  (后加

                boolean isDisplayTopNews = isDisplayTopNews();
                if (!isDisplayTopNews) {
                    //加载更多
                    break;
                }

                //如果是正在刷新，就不让再刷新了
                if (currentStatus == REFRESHING) {
                    break;
                }
                //2.来到新的坐标
                float endY = ev.getY();
                //3.记录滑动的距离
                float distanceY = endY - startY;
                if (distanceY > 0) {//下拉

                    int paddingTop = (int) (-pullDownRefreshHeight + distanceY);

                    if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH) {
                        //下拉刷新状态
                        currentStatus = PULL_DOWN_REFRESH;
                        //更新状态
                        refreshViewState();
                    } else if (paddingTop > 0 && currentStatus != RELEASE_REFRESH) {
                        //手松刷新状态
                        currentStatus = RELEASE_REFRESH;

                        //更新状态
                        refreshViewState();

                    }

                    ll_pull_down_refresh.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (currentStatus == PULL_DOWN_REFRESH) {
                    ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
                } else if (currentStatus == RELEASE_REFRESH) {
                    //设置状态正在刷新
                    currentStatus = REFRESHING;
                    //view.setPadding(0,0,0,0);//完全显示
                    refreshViewState();
                    ll_pull_down_refresh.setPadding(0, 0, 0, 0);

                    //回调接口
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断是否完全显示顶部轮播图
     * 当listview在屏幕上的y轴坐标小于或者等于顶部轮播图在Y轴的坐标的时候，顶部轮播图完全显示
     *
     * @return
     */
    private boolean isDisplayTopNews() {
        if (topNesView !=null){
            //1.得到listview屏幕上的坐标
            int[] location = new int[2];
            if (listViewOnScreenY == -1) {
                getLocationOnScreen(location);
                listViewOnScreenY = location[1];
            }


            //2.得到顶部轮播图在屏幕上的坐标
            topNesView.getLocationOnScreen(location);
            int topNewsViewViewOnScreenY = location[1];

//        if (listViewOnScreenY <= topNewsViewViewOnScreenY) {
//            return true
//        } else {
//            return false;
//        }

            return listViewOnScreenY <= topNewsViewViewOnScreenY;
        }else{
            return true;
        }


    }

    private void refreshViewState() {
        switch (currentStatus) {
            case PULL_DOWN_REFRESH:  //下拉刷新
                iv_arrow.startAnimation(downAnimation);
                tv_status.setText("下拉刷新了");
                break;
            case RELEASE_REFRESH:  //松手刷新状态
                iv_arrow.startAnimation(upAnimation);
                tv_status.setText("手动刷新。。");

                break;
            case REFRESHING:   //正在刷新
                tv_status.setText("正在刷洗");
                pb_status.setVisibility(VISIBLE);
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(GONE);
                break;
        }
    }

    /**
     * 当联网成功或失败时回调该方法
     * 用户刷新状态的还原
     *
     * @param success
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRefreshFinish(boolean success) {
        if (isLoadMore) {
//加载更多
            isLoadMore = false;
            //隐藏加载更多布局
            footerView.setPadding(0, -footerViewHerght, 0, 0);
        } else {
            //下来刷新
            tv_status.setText("下拉刷新。。。");
            currentStatus = PULL_DOWN_REFRESH;
            iv_arrow.clearAnimation();
            pb_status.setVisibility(GONE);
            iv_arrow.setVisibility(VISIBLE);
            //隐藏下拉刷新控件
            ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
            if (success) {
                tv_time.setText("上次更新时间：" + getSystemtime());
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getSystemtime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    public interface OnRefreshListener {
        //当下拉刷新时，回调这个方法
        public void onPullDownRefresh();

        /**
         * j
         */
        public void onLoadMore();
    }

    private OnRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(OnRefreshListener l) {
        this.mOnRefreshListener = l;
    }

}
