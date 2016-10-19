package com.example.xiaogang.onegirloneblog.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.xiaogang.onegirloneblog.Data.OkUtil;
import com.example.xiaogang.onegirloneblog.Data.Zhihunews;
import com.example.xiaogang.onegirloneblog.R;
import com.squareup.picasso.Picasso;

import okhttp3.Call;

public class ZhihunewsActivity extends AppCompatActivity {
    private String id = "id";//不写这个下面就要getStringExtra("id"),当时一时没有反应过来要加""
    private String title = "title";
    public  Zhihunews zhihunews;
    private  String url = id;
    private WebView webView;
    private ImageView imageView;
    private String image = "image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihunews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int newsid = Integer.valueOf(getIntent().getStringExtra(id));
        title = getIntent().getStringExtra(title);
        image =getIntent().getStringExtra(image);
        getSupportActionBar().setTitle(title);
        url = "http://news-at.zhihu.com/api/4/news/"+newsid;
        System.out.println(url);
        webView = (WebView) findViewById(R.id.zhuhunews_webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        imageView = (ImageView) findViewById(R.id.zhihunews_image);

        loadLatestData();


    }


    private void loadLatestData() {
        OkUtil.getInstance().okHttpZhihuGson(url, new OkUtil.ResultCallback<Zhihunews>
                () {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Zhihunews response, String json) {
                if (response != null ) {
                    zhihunews = response;
                    System.out.println(zhihunews.getTitle());
                    loadWebView();
                }
            }
        });
    }
    private void loadWebView() {
        System.out.println(zhihunews.getBody());
        Picasso.with(this).load(zhihunews.getImage()).into(imageView);
        String html = "<HTML><HEAD><LINK href=\"news.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>" + zhihunews.getBody() + "</body></HTML>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }
    //    private void initView() {
//
//        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbartop);
//        mCollapsingToolbarLayout.setTitle();
//        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
//        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);//设置收缩后Toolbar上字体的颜色
//        想好页面布局在设置
//    }
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
