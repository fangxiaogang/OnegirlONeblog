package com.example.xiaogang.onegirloneblog.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiaogang.onegirloneblog.Activity.WebActivity;
import com.example.xiaogang.onegirloneblog.Data.Androidblog;
import com.example.xiaogang.onegirloneblog.R;

import java.util.List;

/**
 * Created by xiaogang on 16/8/28.
 */
public class AndroidBlogAdapter extends RecyclerView.Adapter<AndroidBlogAdapter.ViewHolder> {
    private Context context;
    private List<Androidblog> datas;

    public AndroidBlogAdapter(Context context, List<Androidblog> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public AndroidBlogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.androidblog_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AndroidBlogAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(datas.get(position).getDesc());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("url",datas.get(position).getUrl());
                intent.putExtra("title",datas.get(position).getDesc());
                context.startActivity(intent);
            }
        });
//        System.out.println(datas.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.androidblog_title);
        }
    }
}
