package com.example.administrator.test1.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.test1.R;
import com.example.administrator.test1.activity.Main;
import com.example.administrator.test1.base.BaseFragment;
import com.example.administrator.test1.domain.NewsCenterPagerBean;
import com.example.administrator.test1.pager.NewsCenterPager;
import com.example.administrator.test1.utils.DensityUtil;
import com.example.administrator.test1.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2017-12-03.
 */

public class LeftMenuFragment extends BaseFragment{

    //private TextView textView;
    private List<NewsCenterPagerBean.DataBean> data;
    private LeftmenuFragmentAdapter adapter;
    private ListView listView;
    private int prePosition;

    @Override
    public View initView() {
        LogUtil.e("左侧菜单被初始化了");
//        textView = new TextView(context);
//        textView.setTextSize(23);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.RED);
        //View view = View.inflate()
        listView = new ListView(context);
        listView.setPadding(0, DensityUtil.dp2px(context,40),0,0);
        listView.setDividerHeight(0);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setSelector(android.R.color.transparent);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prePosition = position;
                adapter.notifyDataSetChanged();

                Main main = (Main) context;
                main.getSlidingMenu().toggle();

                //3、切换到对应的详情页面：新闻，专题 图组  互动
                switchPager(prePosition);

            }
            private void switchPager(int position) {
                Main mainActivity = (Main) context;
                ContentFragment contentFragment = mainActivity.getContentFragment();
                NewsCenterPager newsCenterPager= contentFragment.getNewsConterPager();
                newsCenterPager.switchPager(position);
            }
        });

        return listView;
    }

    @Override
    public void initData() {
        LogUtil.e("左侧菜单数据被初始化了");
        super.initData();
        //textView.setText("左侧菜单页面Fragment");
    }


    /**
     * 接受数据
     * @param data
     */
    public void setData(List<NewsCenterPagerBean.DataBean> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            LogUtil.e("title =="+data.get(i).getTitle());
        }
        //设置适配器
        adapter = new LeftmenuFragmentAdapter();
        listView.setAdapter(adapter);



    }
    class  LeftmenuFragmentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
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
            TextView textView = (TextView) View.inflate(context,R.layout.item_leftmenu,null);
            textView.setText(data.get(position).getTitle());
//            if (position==prePosition){
//                textView.setEnabled(true);
//            }else {
//                textView.setEnabled(false);
//            }
            textView.setEnabled(position==prePosition);
            return textView;
        }
    }
}
