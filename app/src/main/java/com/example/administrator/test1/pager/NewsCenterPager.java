package com.example.administrator.test1.pager;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.test1.activity.Main;
import com.example.administrator.test1.base.BasePager;
import com.example.administrator.test1.base.MenuDetailBasePager;
import com.example.administrator.test1.domain.NewsCenterPagerBean;
import com.example.administrator.test1.fragment.LeftMenuFragment;
import com.example.administrator.test1.menudetailpager.InteracMenuDetailPager;
import com.example.administrator.test1.menudetailpager.NewsMenuDetailPager;
import com.example.administrator.test1.menudetailpager.PhotosMenuDetailPager;
import com.example.administrator.test1.menudetailpager.TopicMenuDetailPager;
import com.example.administrator.test1.utils.CacheUitls;
import com.example.administrator.test1.utils.Constants;
import com.example.administrator.test1.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/3.
 */

public class NewsCenterPager extends BasePager {

    //左侧菜单对应的数据
    private List<NewsCenterPagerBean.DataBean> data;
    //详情页面集合
    private ArrayList<MenuDetailBasePager> detailBasePagers;
    //起始时间
    private long startTime;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻页面数据被初始化了222。。");
        ib_menu.setVisibility(View.VISIBLE);

        //1、设置标题
        tv_title.setText("新闻中心1");
        //2、联网请求，得到数据，创建视图
//        TextView textView = new TextView(context);
//
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.RED);
//        textView.setTextSize(25);


        //3、把子视图添加到BasePager的FrameLayout中
        // fl_content.addView(rootview);
        //4、绑定数据
        //textView.setText("新闻中心内容1");
        //获取缓存数据
        String savaJson = CacheUitls.getString(context, Constants.NEWSENTER_PAGER_URL);//""
        if (!TextUtils.isEmpty(savaJson)) {
            processData(savaJson);
        }


        //测试框架性能
        startTime = SystemClock.uptimeMillis();

        //联网请求数据
        //getDataFromNet();
        getDataFromNetByVolley();


    }

    /**
     * 用volley联网请求数据
     */
    private void getDataFromNetByVolley() {
        //RequestQueue queue = Volley.newRequestQueue(context);
        //string请求
        StringRequest request = new StringRequest(Request.Method.GET, Constants.NEWSENTER_PAGER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                //结束时间
                long endtime =SystemClock.uptimeMillis();
                long passTime = endtime-startTime;
                LogUtil.e("voley花的时间==" + passTime);
                LogUtil.e("使用voley连接网络成功==" + result);
                //缓存数据
                CacheUitls.putString(context, Constants.NEWSENTER_PAGER_URL, result);

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

    /**
     * 使用xutils3联网请求
     */

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xUtils3连接网络成功==" + result);

                //缓存数据
                CacheUitls.putString(context, Constants.NEWSENTER_PAGER_URL, result);

                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xUtils3连接网络失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xUtils3onCancelled" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用onFinished");
            }
        });
    }

    //解析json数据和显示数据
    private void processData(String json) {
        NewsCenterPagerBean bean = parsedJson(json);
//        NewsCenterPagerBean2 bean2 = parsedJson2(json);
        //String title = bean.getData().get(0).getChildren().get(1).getTitle();
        // LogUtil.e("使用Gson解析数据成功==" + title);

        //String title2 = bean.getData().get(0).getChildren().get(1).getTitle();
//        LogUtil.e("使用Gson解析数据成功2==" + title2);
        //给左侧菜单提供数据

        data = bean.getData();

        Main mainActivity = (Main) context;
        //得到左侧菜单
        LeftMenuFragment leftmenuFragment = mainActivity.getLeftMenuFragment();

        //添加详情页面
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsMenuDetailPager(context, data.get(0)));
        detailBasePagers.add(new TopicMenuDetailPager(context, data.get(0)));
        detailBasePagers.add(new PhotosMenuDetailPager(context,data.get(2)));
        detailBasePagers.add(new InteracMenuDetailPager(context,data.get(2)));
        //把数据传递给左侧菜单
        leftmenuFragment.setData(data);


    }

    /**
     * 手动解析，android系统的api解析json
     *
     * @param json
     * @return
     */

//    private NewsCenterPagerBean2 parsedJson2(String json) {
//        NewsCenterPagerBean2 bean2 = new NewsCenterPagerBean2();
//        try {
//            JSONObject object = new JSONObject(json);
//
//            int retcode = object.optInt("retcode");
//            bean2.setRetcode(retcode);//redtcode字段解析成功
//
//
//            JSONArray data = object.optJSONArray("data");
//
//            if (data != null && data.length() > 0) {
//                List<NewsCenterPagerBean2.DetailPagerData> detailPagerDatas = new ArrayList<>();
//
//                //设置列表数据
//                bean2.setData(detailPagerDatas);
//                //for循环，解析每条数据；
//                for (int i = 0; i < data.length(); i++) {
//
//                    JSONObject jsonObject = (JSONObject) data.get(i);
//                    NewsCenterPagerBean2.DetailPagerData detailPagerData = new NewsCenterPagerBean2.DetailPagerData();
//                    //添加到集合中
//                    detailPagerDatas.add(detailPagerData);
//
//                    int id = jsonObject.optInt("id");
//                    detailPagerData.setId(id);
//                    int type = jsonObject.optInt("type");
//                    detailPagerData.setType(type);
//                    String title = jsonObject.optString("title");
//                    detailPagerData.setTitle(title);
//                    String url = jsonObject.optString("url");
//                    detailPagerData.setUrl(url);
//                    String url1 = jsonObject.optString("url1");
//                    detailPagerData.setUrl1(url1);
//                    String dayurl = jsonObject.optString("dayurl");
//                    detailPagerData.setDayurl(dayurl);
//                    String excurl = jsonObject.optString("excurl");
//                    detailPagerData.setExcurl(excurl);
//                    String weekurl = jsonObject.optString("weekurl");
//                    detailPagerData.setWeekurl(weekurl);
//
//                    JSONArray children = jsonObject.optJSONArray("children");
//                    if (children != null && children.length() > 0) {
//
//                        List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> childrenDatas=new ArrayList<>();
//                        //设置结合  childrenData
//                        detailPagerData.setChildren(childrenDatas);
//
//
//
//                        for (int j = 0; j < children.length(); j++) {
//                            JSONObject childrenItem = (JSONObject) data.get(j);
//
//                            NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData = new NewsCenterPagerBean2.DetailPagerData.ChildrenData();
//
//
//                            childrenDatas.add(childrenData);
//
//
//                            int childId = childrenItem.optInt("id");
//                            childrenData.setId(childId);
//                            String childTitle = childrenItem.optString("title");
//                            childrenData.setTitle(childTitle);
//                            int childType = childrenItem.optInt("type");
//                            childrenData.setType(childType);
//                            String childUrl = childrenItem.optString("url");
//                            childrenData.setUrl(childUrl);
//                        }
//                    }
//
//
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 解析Json数据
     * 方法：1、使用系统Api   2、使用第三方框架解析json数据，如 Gson  fastjson
     *
     * @param
     * @return
     */

    private NewsCenterPagerBean parsedJson(String json) {

        Gson gson = new Gson();
        NewsCenterPagerBean bean = gson.fromJson(json, NewsCenterPagerBean.class);

        return bean;
    }

    public void switchPager(int position) {
        tv_title.setText(data.get(position).getTitle());
        fl_content.removeAllViews();
        final MenuDetailBasePager detailBasePager = detailBasePagers.get(position);
        View rootview = detailBasePager.rootview;
        detailBasePager.initData();//初始化数据

        fl_content.addView(rootview);


        if (position == 2) {
            //组图详情页面
            ib_switch_list_grid.setVisibility(View.VISIBLE);
            ib_switch_list_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.得到图组详情页面对象
                    PhotosMenuDetailPager detailPager = (PhotosMenuDetailPager) detailBasePagers.get(2);
                    //2.得到图组对象的切换Listview和GridView的方法
                    detailPager.switchListAndGrid(ib_switch_list_grid);

                }
            });
        } else {
            //其它页面
           // ib_switch_list_grid.setVisibility(View.GONE);
        }


    }

    /**
     * 根据位置切换详情页面
     *
     * @param position
     */
//    public void switchPager(int position) {
//        //1.设置标题
//        tv_title.setText(data.get(position).getTitle());
//        //2.移除之前内容
//        fl_content.removeAllViews();
//        //3.添加新内容
//        MenuDetailBasePager detaibasePager = detailBasePagers.get(position);
//        View rootview = detaibasePager.rootview;
//        detaibasePager.initData();//初始化数据
//
//        fl_content.addView(rootview);
//    }


//    public void switchPager(int position) {
//        //1、设置标题
//        tv_title.setText(data.get(position).getTitle());
//        //2、移除内容
//        fl_content.removeAllViews();
//        //3、添加新内容
//        MenuDetailBasePager detailBasePager = detailBasePagers.get(position);
//        View rootView = detailBasePager.rootView;
//        detailBasePager.initData();//初始化数据
//
//        fl_content.addView(rootView);
//    }


}
