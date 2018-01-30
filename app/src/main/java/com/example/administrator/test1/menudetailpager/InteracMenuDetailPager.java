package com.example.administrator.test1.menudetailpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.test1.R;
import com.example.administrator.test1.base.MenuDetailBasePager;
import com.example.administrator.test1.domain.NewsCenterPagerBean;
import com.example.administrator.test1.domain.PhotosMenuDetailPagerBean;
import com.example.administrator.test1.utils.BitmapCacheUtils;
import com.example.administrator.test1.utils.CacheUitls;
import com.example.administrator.test1.utils.Constants;
import com.example.administrator.test1.utils.NetCacheUtils;
import com.example.administrator.test1.volley.VolleyManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Administrator on 2017-11-03.
 */

public class InteracMenuDetailPager extends MenuDetailBasePager {
    private final NewsCenterPagerBean.DataBean dataBean;
    private final BitmapCacheUtils bitmampCacheUtils;
    private String url;


    //实例化
    @ViewInject(R.id.listview)
    private ListView listView;
    @ViewInject(R.id.gridview)
    private GridView gridview;
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news;
    private PhotosMenuDetailPagerAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCacheUtils.SUCESS:  //图片请求成功
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;

                    if (listView.isShown()) {
                        ImageView iv_icon = listView.findViewWithTag(position);
                        if (iv_icon != null && bitmap != null) {
                            iv_icon.setImageBitmap(bitmap);
                        }
                    }
                    if (gridview.isShown()) {
                        ImageView iv_icon = listView.findViewWithTag(position);
                        if (iv_icon != null && bitmap != null) {
                            iv_icon.setImageBitmap(bitmap);
                        }
                    }
                    LogUtil.e("请求图片成功==" + position);
                    break;
                case NetCacheUtils.FAIL:
                    //图片请求失败
                    position = msg.arg1;
                    LogUtil.e("请求图片失败==" + position);
                    break;
            }
        }
    };

    //private TextView textView;


    public InteracMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
       bitmampCacheUtils = new BitmapCacheUtils(handler);
    }

    @Override
    public View initView() {

//        textView = new TextView(context);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.RED);
//        textView.setTextSize(25);

        //return textView;

        View view = View.inflate(context, R.layout.photos_menudetail_pager, null);
        x.view().inject(InteracMenuDetailPager.this, view);
        return view;


    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("互动详情页面数据初始化了");
        url = Constants.BASE_URL + dataBean.getUrl();
        String savaJson = CacheUitls.getString(context, url);
        if (!TextUtils.isEmpty(savaJson)) {
            processData(savaJson);
        }

        getDataFromNetByVolley();
    }


    private void getDataFromNetByVolley() {
        //RequestQueue queue = Volley.newRequestQueue(context);
        //string请求
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.e("使用voley连接网络成功==" + result);
                //缓存数据
                CacheUitls.putString(context, url, result);

                processData(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("使用voley连接网络失败11==" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };
        //添加到队列中
        //queue.add(request);
        VolleyManager.getRequestQueue().add(request);
    }

    //解析和现实数据
    private void processData(String json) {
        PhotosMenuDetailPagerBean bean = parsedJson(json);
        LogUtil.e("互动解析成功==" + bean.getData().getNews().get(0).getTitle());
        isShowListView = true;
        //设置适配器
        news = bean.getData().getNews();
        adapter = new PhotosMenuDetailPagerAdapter();
        listView.setAdapter(adapter);
    }

    /**
     * true显示ListView隐藏GridView
     */
    private boolean isShowListView = true;

    public void switchListAndGrid(ImageButton ib_switch_list_grid) {
        if (isShowListView == true) {
            isShowListView = false;
            //显示Gridview隐藏ListView
            gridview.setVisibility(View.VISIBLE);
            adapter = new PhotosMenuDetailPagerAdapter();
            gridview.setAdapter(adapter);
            listView.setVisibility(View.GONE);
            //按钮显示 ListView
            ib_switch_list_grid.setImageResource(R.drawable.icon_pic_list_type);
        } else {
            isShowListView = true;
            //显示ListView 隐藏GridView
            listView.setVisibility(View.VISIBLE);
            adapter = new PhotosMenuDetailPagerAdapter();
            listView.setAdapter(adapter);
            gridview.setVisibility(View.GONE);
            //按钮显示  GridView
            ib_switch_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }

    class PhotosMenuDetailPagerAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

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
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_photos_menudetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //根据位置得到数据
            PhotosMenuDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            viewHolder.tv_title.setText(newsBean.getTitle());
            String imageUrl = Constants.BASE_URL + newsBean.getSmallimage();
            //1.使用Volley请求图片-设置图片了
            //loaderImager(viewHolder,imageUrl);
//            //2.使用自定义的三级缓存请求图片
//            viewHolder.iv_icon.setTag(position);
//            Bitmap bitmap = bitmampCacheUtils.getBitmap(imageUrl, position); //从内存或者本地
//            if (bitmap != null) {
//                viewHolder.iv_icon.setImageBitmap(bitmap);
//            }
            //使用Picasso请求网络图片
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.home_scroll_default)
                    .error(R.drawable.home_scroll_default)
                    .into(viewHolder.iv_icon);
            return convertView;
        }
    }

    /**
     * @param viewHolder
     * @param imageurl
     */
    private void loaderImager(final ViewHolder viewHolder, String imageurl) {
        //设置tag
        viewHolder.iv_icon.setTag(imageurl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.iv_icon != null) {
                        if (imageContainer.getBitmap() != null) {
                            //设置图片
                            viewHolder.iv_icon.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            //设置默认图片
                            viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
            }
        };
        VolleyManager.getImageLoader().get(imageurl, listener);
    }


    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
    }

    private PhotosMenuDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
    }

}
