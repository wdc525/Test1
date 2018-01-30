package com.example.administrator.test1.menudetailpager;

import android.content.Context;
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
import com.example.administrator.test1.utils.CacheUitls;
import com.example.administrator.test1.utils.Constants;
import com.example.administrator.test1.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Administrator on 2017-11-03.
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {
    private final NewsCenterPagerBean.DataBean dataBean;
    private String url;


    //实例化
    @ViewInject(R.id.listview)
    private ListView listView;
    @ViewInject(R.id.gridview)
    private GridView gridview;
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news;
    private PhotosMenuDetailPagerAdapter adapter;

    //private TextView textView;


    public PhotosMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
    }

    @Override
    public View initView() {

//        textView = new TextView(context);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.RED);
//        textView.setTextSize(25);

        //return textView;

        View view = View.inflate(context, R.layout.photos_menudetail_pager, null);
        x.view().inject(PhotosMenuDetailPager.this,view);
        return view;


    }

    @Override
    public void initData() {

        LogUtil.e("图组初始化了");
        //textView.setText("图组详情页面内容");
        url = Constants.BASE_URL+dataBean.getUrl();
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
                //结束时间
                //long endtime = SystemClock.uptimeMillis();
               // long passTime = endtime-startTime;
               // LogUtil.e("voley花的时间==" + passTime);
                LogUtil.e("使用voley连接网络成功==" + result);
                //缓存数据
                CacheUitls.putString(context, url, result);

                processData(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("使用voley连接网络失败==" + volleyError.getMessage());
            }
        }){
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
        LogUtil.e("图组解析成功=="+bean.getData().getNews().get(0).getTitle());
        isShowListView = true;
        //设置适配器
        news=  bean.getData().getNews();
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
            isShowListView =  true;
            //显示ListView 隐藏GridView
            listView.setVisibility(View.VISIBLE);
            adapter = new PhotosMenuDetailPagerAdapter();
            listView.setAdapter(adapter);
            gridview.setVisibility(View.GONE);
            //按钮显示  GridView
            ib_switch_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }

    class PhotosMenuDetailPagerAdapter extends BaseAdapter{

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
            PhotosMenuDetailPagerBean.DataBean.NewsBean newsBean =  news.get(position);
            viewHolder.tv_title.setText(newsBean.getTitle());
            String imageUrl = Constants.BASE_URL+newsBean.getSmallimage();
            loaderImager(viewHolder,imageUrl);
            return convertView;
        }
    }

    /**
     *
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


    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
    }

    private PhotosMenuDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json,PhotosMenuDetailPagerBean.class);
    }
}
