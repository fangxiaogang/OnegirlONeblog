package com.example.xiaogang.onegirloneblog.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xiaogang.onegirloneblog.Adapter.ZhihuAdapter;
import com.example.xiaogang.onegirloneblog.Data.CircularAnim;
import com.example.xiaogang.onegirloneblog.Data.OkUtil;
import com.example.xiaogang.onegirloneblog.Data.zhihu;
import com.example.xiaogang.onegirloneblog.R;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.Call;

public class ZhihuActivity2 extends AppCompatActivity {
    private static RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;
    private List<zhihu.Stories> stories;
    private List<zhihu.Top_stories> topStories;
    private ZhihuAdapter zhihuAdapter;
    private zhihu zhi;
    String url = "http://news-at.zhihu.com/api/4/news/latest";
    private RollPagerView mRollViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("知乎日报");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);
        mRollViewPager.setAnimationDurtion(200);    //设置切换时间
        recyclerview = (RecyclerView)findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(ZhihuActivity2.this,LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(linearLayoutManager);
        new GetData().execute(url);

    }
    class GetData extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
        OkUtil.getInstance().okHttpZhihuGson(params[0], new OkUtil.ResultCallback<zhihu>
                () {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(zhihu response, String json) {
                if (response != null ) {
                    zhi = response;
                    System.out.println(zhi.getStories().get(0).getTitle());
                    stories = zhi.getStories();
                    topStories = zhi.getTop_stories();
                    System.out.println(stories.get(0).getTitle()+"5555555");
                    zhihuAdapter = new ZhihuAdapter(ZhihuActivity2.this,stories);
                    recyclerview.setAdapter(zhihuAdapter);
                    mRollViewPager.setAdapter(new ZhihuActivity2.TestLoopAdapter(mRollViewPager)); //设置适配器
                    mRollViewPager.setHintView(new ColorPointHintView(ZhihuActivity2.this, Color.WHITE, Color.GRAY));// 设置圆点指示器颜色
                    zhihuAdapter.setOnItemClickListener(new ZhihuAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            int position=recyclerview.getChildAdapterPosition(view);
                            final Intent intent = new Intent(ZhihuActivity2.this,ZhihunewsActivity.class);
                            intent.putExtra("image",stories.get(position).getString().get(0));
                            intent.putExtra("id",String.valueOf(stories.get(position).getId()));
                            intent.putExtra("title",stories.get(position).getTitle());
                            CircularAnim.fullActivity(ZhihuActivity2.this, view)
                                    .colorOrImageRes(R.color.colorPrimary)
                                    .duration(300)
                                    .go(new CircularAnim.OnAnimationEndListener() {
                                        @Override
                                        public void onAnimationEnd() {
                                            startActivity(intent);
                                        }
                                    });

                        }
                    });

                }
            }
        });
        return null;
    }
}
    private class TestLoopAdapter extends LoopPagerAdapter
    {

        public TestLoopAdapter(RollPagerView viewPager)
        {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position)
        {
            final int positio = position;
            System.out.println(topStories.get(0).getImage());
            final ImageView view = new ImageView(container.getContext());
            Picasso.with(container.getContext()).load(topStories.get(position).getImage()).into(view);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener()      // 点击事件
            {
                @Override
                public void onClick(View v)
                {
                    final Intent intent = new Intent(ZhihuActivity2.this,ZhihunewsActivity.class);

                    intent.putExtra("id",String.valueOf(topStories.get(positio).getId()));
                    intent.putExtra("title",topStories.get(positio).getTitle());
                    CircularAnim.fullActivity(ZhihuActivity2.this, view)
                            .colorOrImageRes(R.color.colorPrimary)
                            .duration(300)
                            .go(new CircularAnim.OnAnimationEndListener() {
                                @Override
                                public void onAnimationEnd() {
                                    startActivity(intent);
                                }
                            });
                }

            });

            return view;
        }

        @Override
        public int getRealCount()
        {
            return 5;
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
