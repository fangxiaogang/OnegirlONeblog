package com.example.xiaogang.onegirloneblog.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.xiaogang.onegirloneblog.Data.TouchImageView;
import com.example.xiaogang.onegirloneblog.R;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    private String url = "url";
    private String desc = "desc";
    //ImageView imageView;
    TouchImageView img;
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

//      imageView = (ImageView) findViewById(R.id.image_meizi);
        url = getIntent().getStringExtra(url);

        Picasso.with(this).load(url).into(img);





    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
