package com.example.xiaogang.onegirloneblog.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaogang.onegirloneblog.Data.zhihu;
import com.example.xiaogang.onegirloneblog.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by xiaogang on 16/10/15.
 */

public class ZhihuAdapter extends RecyclerView.Adapter<ZhihuAdapter.ViewHolder> implements View.OnClickListener {


    private Context context;
    private List<zhihu.Stories> datas;

    public ZhihuAdapter(Context context, List<zhihu.Stories> datas) {
        this.context = context;
        this.datas = datas;
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);

    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }




    @Override
    public ZhihuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.zhihu_item,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ZhihuAdapter.ViewHolder holder, int position) {
        //String url = datas.get(position).getImages().get(position);
        holder.textView.setText(datas.get(position).getTitle());
        Picasso.with(context).load(datas.get(position).getString().get(0)).into(holder.image);
        System.out.println("333");
//        datas.get(position).getStories().get(position).getTitle()
    }

    @Override
    public int getItemCount() {
        return datas.size() ;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {

            mOnItemClickListener.onItemClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.zhihu_image);
            textView = (TextView) itemView.findViewById(R.id.zhihutitle);
        }
    }
}
