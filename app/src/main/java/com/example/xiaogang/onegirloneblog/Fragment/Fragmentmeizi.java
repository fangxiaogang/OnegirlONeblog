package com.example.xiaogang.onegirloneblog.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiaogang.onegirloneblog.Activity.ImageActivity;
import com.example.xiaogang.onegirloneblog.Adapter.StaggeredAdapter;
import com.example.xiaogang.onegirloneblog.Data.Meizi;
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
public class Fragmentmeizi extends Fragment {
    private static RecyclerView recyclerview;
    private StaggeredAdapter mAdapter;
    private List<Meizi> meizis;
    private StaggeredGridLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private int page=1;
    private ItemTouchHelper itemTouchHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    public Fragmentmeizi() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmentmeizi, container, false);
        // Inflate the layout for this fragment
//        initView();
        recyclerview = (RecyclerView) view.findViewById(R.id.recycler);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefresh) ;
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
         swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        initdata();
        new GetData().execute("http://gank.io/api/data/福利/10/1");
        return view;
    }

    private void initdata() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                new GetData().execute("http://gank.io/api/data/福利/10/1");
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
                        && lastVisibleItem +2>=mLayoutManager.getItemCount()) {
                    new GetData().execute("http://gank.io/api/data/福利/10/"+(++page));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                获取加载的最后一个可见视图在适配器的位置。
                int[] positions= mLayoutManager.findLastVisibleItemPositions(null);
                lastVisibleItem = Math.max(positions[0],positions[1]);
            }
        });
    }


        class GetData extends AsyncTask<String, Integer, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
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
                    if(meizis==null||meizis.size()==0){
                        meizis= gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {}.getType());
                        Meizi pages=new Meizi();

                    }
                    else{
                        List<Meizi> more= gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {}.getType());
                        meizis.addAll(more);


                    }

                    if(mAdapter==null){
                        recyclerview.setAdapter(mAdapter = new StaggeredAdapter(getContext(),meizis));
                        mAdapter.setOnItemClickListener(new StaggeredAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view) {
                                int position=recyclerview.getChildAdapterPosition(view);
                                Intent intent = new Intent(getContext(),ImageActivity.class);
                                intent.putExtra("url",meizis.get(position).getUrl());
                                intent.putExtra("desc",meizis.get(position).getDesc());
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view) {

                            }
                        });
                    }else{
                        mAdapter.notifyDataSetChanged();


                    }
                }
                //停止swipeRefreshLayout加载动画
                swipeRefreshLayout.setRefreshing(false);
            }
        }


   }






