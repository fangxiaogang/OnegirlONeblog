package com.example.xiaogang.onegirloneblog.Adapter;//package com.example.xiaogang.onegirloneblog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaogang.onegirloneblog.Data.Meizi;
import com.example.xiaogang.onegirloneblog.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by xiaogang on 16/8/14.
 */
public  class StaggeredAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private List<Meizi> datas;

    public StaggeredAdapter(Context context, List<Meizi> datas) {
        mContext=context;
        this.datas=datas;
    }


    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
        void onItemLongClick(View view);

    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (!TextUtils.isEmpty(datas.get(position).getUrl())) {
//            return 0;
//        } else {
//            return 1;
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

            View view = LayoutInflater.from(mContext
                    ).inflate(R.layout.image_item, parent,
                    false);
            MyViewHolder holder = new MyViewHolder(view);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            return holder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            Picasso.with(mContext).load(datas.get(position).getUrl()).into((ImageView) ((MyViewHolder) holder).image);
        }

    }

    @Override
    public int getItemCount()
    {
        return datas.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {

            mOnItemClickListener.onItemClick(v);
        }

    }


    @Override
    public boolean onLongClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemLongClick(v);
        }
        return false;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        private View image;

        public MyViewHolder(View view)
        {
            super(view);
            image = view.findViewById(R.id.img);
            //iv = (ImageButton) view.findViewById(R.id.iv);
        }
    }


}
