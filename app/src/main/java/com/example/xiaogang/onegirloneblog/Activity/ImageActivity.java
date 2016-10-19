package com.example.xiaogang.onegirloneblog.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.xiaogang.onegirloneblog.Data.ImgSaveUtil;
import com.example.xiaogang.onegirloneblog.Data.TouchImageView;
import com.example.xiaogang.onegirloneblog.R;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    private String url = "url";
    private String desc = "desc";
    //ImageView imageView;
    TouchImageView img;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle("查看图片");   用法错误，除非在getactionbar之前设定。
        desc = getIntent().getStringExtra(desc);
        getSupportActionBar().setTitle(desc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img = (TouchImageView) findViewById(R.id.image2);
        img.setMaxZoom(4f);
        url = getIntent().getStringExtra(url);

        //Picasso.with(this).load(url).into(img);
        Glide.with(this)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {// 先下载图片然后再做一些合成的功能，比如项目中出现的图文混排
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        img.setImageBitmap(resource);
                        bitmap = resource;
                    }
                });




    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_gril, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.action_save) {
            ImgSaveUtil.saveImage(this,url,bitmap,img,"saveImg");
            Toast.makeText(this,"已保存",Toast.LENGTH_SHORT ).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
