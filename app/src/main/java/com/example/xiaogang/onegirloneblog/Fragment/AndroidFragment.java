package com.example.xiaogang.onegirloneblog.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiaogang.onegirloneblog.Adapter.AndroidBlogAdapter;
import com.example.xiaogang.onegirloneblog.Data.Androidblog;
import com.example.xiaogang.onegirloneblog.Data.Okhttp;
import com.example.xiaogang.onegirloneblog.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AndroidFragment extends Fragment {
    private static RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Androidblog> androidblogs;
    private AndroidBlogAdapter androidBlogAdapter;
    private List<String> blogs;
    private int lastVisibleItem;
    private int page=1;
    public AndroidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_android, container, false);
        TextView textView= (TextView) view.findViewById(R.id.androidblog_title);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefresh2) ;
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        recyclerview = (RecyclerView) view.findViewById(R.id.recycler2);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(linearLayoutManager);
        initdata();
        new GetData().execute("http://gank.io/api/data/Android/10/1");

        return view;
    }

    private void initdata() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                new GetData().execute("http://gank.io/api/data/Android/10/1");
            }
        });

        //recyclerview滚动监听
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                // 滑动状态停止并且剩余少于两个item时，自动加载下一页
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem +1>=linearLayoutManager.getItemCount()) {
                    new GetData().execute("http://gank.io/api/data/Android/10/"+(++page));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                获取加载的最后一个可见视图在适配器的位置。
                lastVisibleItem= linearLayoutManager.findLastVisibleItemPosition();
                //lastVisibleItem = Math.max(positions[0],positions[1]);
                System.out.println(lastVisibleItem);
            }
        });


    }
    private class GetData extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //设置swipeRefreshLayout为刷新状态
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {

            return Okhttp.get(params[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!TextUtils.isEmpty(result)){

                JSONObject jsonObject;
                Gson gson=new Gson();
                String jsonData=null;

                try {
                    jsonObject = new JSONObject(result);
                    jsonData = jsonObject.getString("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(androidblogs==null||androidblogs.size()==0){
                    androidblogs= gson.fromJson(jsonData, new TypeToken<List<Androidblog>>() {}.getType());

                }
                else{
                    List<Androidblog> more= gson.fromJson(jsonData, new TypeToken<List<Androidblog>>() {}.getType());
                     androidblogs.addAll(more);
                }
                if(androidBlogAdapter==null){

                    recyclerview.setAdapter(androidBlogAdapter = new AndroidBlogAdapter(getActivity(),androidblogs));

                }else{
                    androidBlogAdapter.notifyDataSetChanged();


                }
            }
            //停止swipeRefreshLayout加载动画
            swipeRefreshLayout.setRefreshing(false);

        }
    }
}
