package com.atguigu.testvolley;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.atguigu.testvolley.volley.VolleyManager;

/**
 * Volley请求图片
 *
 * @author Administrator
 *         没有任何封装的方式
 */
public class ImageViewDataActivity extends Activity {

    protected static final String TAG = ImageViewDataActivity.class
            .getSimpleName();
    private ImageView iv_result;

    private String imageUrl = "http://i1.hdslb.com/bfs/archive/6274df9eed3469b8f368b415a865ebcd3ea30adf.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewdata);
        iv_result = (ImageView) findViewById(R.id.iv_result);

    }

    public void getImageViewDataFromVolley(View view) {

        ImageListener listener = VolleyManager.getImageLoader() .getImageListener(iv_result, android.R.drawable.ic_menu_rotate,
                        android.R.drawable.ic_delete);

        VolleyManager.getImageLoader().get(imageUrl, listener);
    }

}
