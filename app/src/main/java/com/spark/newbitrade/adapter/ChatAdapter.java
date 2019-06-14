package com.spark.newbitrade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spark.newbitrade.R;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.entity.ChatEntity;
import com.spark.newbitrade.utils.DateUtils;
import com.spark.newbitrade.utils.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 聊天
 * Created by Administrator on 2018/4/12.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ChatEntity> datas;
    private String myId;
    private Context context;

    public ChatAdapter(Context context, List<ChatEntity> datas, String myId) {
        this.datas = datas;
        this.myId = myId;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (myId.equals(datas.get(position).getUidFrom())) {
            holder.llLeft.setVisibility(View.GONE);
            holder.llRight.setVisibility(View.VISIBLE);
            if (MyApplication.app.getCurrentUser().getAvatar() == null) {

            } else {
                Glide.with(context).load(MyApplication.app.getCurrentUser().getAvatar())
                        .placeholder(R.mipmap.icon_avatar).into(holder.ivHeaderRight);
            }
            holder.tvMessageRight.setText(datas.get(position).getContent());
            if (StringUtils.isNotEmpty(datas.get(position).getSendTime())) {
                if (StringUtils.isNotEmpty(datas.get(position).getSendTime())) {
                    if (datas.get(position).getSendTime().length() != 13) {
                        Date date = new Date(datas.get(position).getSendTime());
                        holder.tvTimeRight.setText(DateUtils.getFormatTime(null, date));
                    } else {
                        Date date = new Date(Long.parseLong(datas.get(position).getSendTime()));
                        holder.tvTimeRight.setText(DateUtils.getFormatTime(null, date));
                    }
                }
            }
        } else {
            holder.llLeft.setVisibility(View.VISIBLE);
            holder.llRight.setVisibility(View.GONE);
            Glide.with(context).load(datas.get(position).getFromAvatar())
                    .placeholder(R.mipmap.icon_avatar).into(holder.ivHeaderLeft);
            holder.tvMessageLeft.setText(datas.get(position).getContent());
            if (StringUtils.isNotEmpty(datas.get(position).getSendTime())) {
                if (datas.get(position).getSendTime().length() != 13) {
                    Date date = new Date(datas.get(position).getSendTime());
                    holder.tvTimeLeft.setText(DateUtils.getFormatTime(null, date));
                } else {
                    Date date = new Date(Long.parseLong(datas.get(position).getSendTime()));
                    holder.tvTimeLeft.setText(DateUtils.getFormatTime(null, date));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivHeaderLeft;
        public TextView tvMessageLeft;
        public TextView tvTimeLeft;
        public ImageView ivHeaderRight;
        public TextView tvMessageRight;
        public TextView tvTimeRight;
        public RelativeLayout llLeft;
        public RelativeLayout llRight;

        public ViewHolder(View itemView) {
            super(itemView);
            ivHeaderLeft = itemView.findViewById(R.id.ivHeaderLeft);
            tvMessageLeft = itemView.findViewById(R.id.tvMessageLeft);
            tvTimeLeft = itemView.findViewById(R.id.tvTimeLeft);
            ivHeaderRight = itemView.findViewById(R.id.ivHeaderRight);
            tvMessageRight = itemView.findViewById(R.id.tvMessageRight);
            tvTimeRight = itemView.findViewById(R.id.tvTimeRight);
            llLeft = itemView.findViewById(R.id.llLeft);
            llRight = itemView.findViewById(R.id.llRight);
        }
    }
}
