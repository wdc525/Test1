package com.atguigu.testvolley.imagerlist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.atguigu.testvolley.R;
import com.atguigu.testvolley.volley.VolleyManager;

public class ImagerListAdapter2 extends BaseAdapter {

    private static final String TAG = ImagerListAdapter2.class.getSimpleName();
    private final String[] imageThumbUrls;
    private final Context context;
    private  ImageLoader imageLoader = VolleyManager.getImageLoader();



    public ImagerListAdapter2(Context context, String[] imageThumbUrls) {
        this.context = context;
        this.imageThumbUrls = imageThumbUrls;
    }

    @Override
    public int getCount() {
        return imageThumbUrls.length;
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.image_items, null);
            viewHolder = new ViewHolder();
            viewHolder.imageview = (ImageView) convertView.findViewById(R.id.imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageview.setImageResource(R.drawable.empty_photo);
        //得到数据
        String imageurl = imageThumbUrls[position];
        System.out.println("imageurl==="+imageurl);
        loaderImager(viewHolder, imageurl);

        return convertView;

    }

    /**
     *
     * @param viewHolder
     * @param imageurl
     */
    private void loaderImager(final ViewHolder viewHolder, String imageurl) {

        viewHolder.imageview.setTag(imageurl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.imageview != null) {
                        if (imageContainer.getBitmap() != null) {
                            viewHolder.imageview.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            viewHolder.imageview.setImageResource(R.drawable.empty_photo);
                        }
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.imageview.setImageResource(R.drawable.empty_photo);
            }
        };
        imageLoader.get(imageurl, listener);
    }


    static class ViewHolder {
        ImageView imageview;
    }

}
