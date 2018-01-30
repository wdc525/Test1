package com.example.administrator.test1.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.test1.MainActivity;
import com.example.administrator.test1.R;
import com.example.administrator.test1.utils.CacheUitls;
import com.example.administrator.test1.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.key;

public class GuideActivity extends AppCompatActivity {
    private ViewPager viewpager;
    private Button btn_start_main;
    private List<ImageView> imageViews;
    private LinearLayout ll_point_group;
    private ImageView iv_red_point;
    private int leftmaxt;

    private int widthdpi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        btn_start_main = (Button) findViewById(R.id.btn_start_main);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);


        int[] ids = new int[]{
                R.drawable.guide_1,
                R.drawable.guide_2,
                R.drawable.guide_3
        };
        widthdpi = DensityUtil.dp2px(this, 10);

        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageview = new ImageView(this);
            imageview.setImageResource(ids[i]);
            imageViews.add(imageview);
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_nomal);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdpi, widthdpi);
            if (i != 0) {
                params.leftMargin = widthdpi;
            }
            point.setLayoutParams(params);


            ll_point_group.addView(point);

        }

        viewpager.setAdapter(new MyPagerAdapter());

        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());

        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1、保存曾经进过主页面
                CacheUitls.putBoolean(GuideActivity.this, MainActivity.START_MAIN,true);

                //2、跳转到主页面
                Intent intent = new Intent(GuideActivity.this, Main.class);
                startActivity(intent);
                //3、关闭主导页面
                finish();
            }
        });

    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //int leftmargin = (int) (positionOffset * leftmaxt);

            int leftmargin = (int) (position*leftmaxt+positionOffset*leftmaxt);
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            param.leftMargin = leftmargin;
            iv_red_point.setLayoutParams(param);
        }

        @Override
        public void onPageSelected(int position) {
            if(position==imageViews.size()-1){
                btn_start_main.setVisibility(View.VISIBLE);
            }else{
                btn_start_main.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            leftmaxt = ll_point_group.getChildAt(1).getLeft()-ll_point_group.getChildAt(0).getLeft();

        }
    }


    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            //添加到容器中
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
