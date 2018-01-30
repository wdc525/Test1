package com.example.administrator.test1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.administrator.test1.activity.GuideActivity;
import com.example.administrator.test1.activity.Main;
import com.example.administrator.test1.utils.CacheUitls;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout main;
    public static final String START_MAIN = "start_main";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (RelativeLayout) findViewById(R.id.main);

        AnimationSet set = new AnimationSet(true);

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(5000);
        sa.setFillAfter(true);
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(5000);
        ra.setFillAfter(true);
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(5000);
        aa.setFillAfter(true);

       // set.setDuration(1000);

        set.addAnimation(ra);
        set.addAnimation(aa);
        set.addAnimation(sa);

        //main.setAnimation(set);
        main.startAnimation(set);
        //对动画进行侦听
        set.setAnimationListener(new MyAnimationListener());



    }
    class MyAnimationListener implements Animation.AnimationListener{



        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            //判断是否进入主页面
            boolean isStartMain = CacheUitls.getBoolean(MainActivity.this, START_MAIN);
            //如果进入了主页面，则进入主页面
            if(isStartMain){
                intent = new Intent(MainActivity.this,Main.class);
            }else{
                intent = new Intent(MainActivity.this, GuideActivity.class);
            }
            startActivity(intent);
            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
