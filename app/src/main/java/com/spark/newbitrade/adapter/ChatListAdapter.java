package com.spark.newbitrade.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.ChatTable;
import com.spark.newbitrade.ui.CircleImageView;
import com.spark.newbitrade.utils.DateUtils;
import com.spark.newbitrade.utils.StringUtils;

import java.text.ParseException;
import java.util.List;

/**
 * 聊天记录
 * Created by Administrator on 2018/4/12.
 */

public class ChatListAdapter extends BaseQuickAdapter<ChatTable, BaseViewHolder> {
    private Context context;

    public ChatListAdapter(int layoutResId, @Nullable List<ChatTable> data, Context context) {
        super(layoutResId, data);
        this.context = context;

    }

    @Override
    protected void convert(BaseViewHolder helper, ChatTable item) {
        helper.setText(R.id.tvName, item.getNameFrom()).setText(R.id.tvMessage, item.getContent());
        Glide.with(context.getApplicationContext()).load(item.getFromAvatar()).placeholder(R.mipmap.icon_avatar).into((CircleImageView) helper.getView(R.id.ivHeader));
        if (!item.isRead) helper.setVisible(R.id.ivChatTip, true);
        else helper.setVisible(R.id.ivChatTip, false);
        long currentTime = System.currentTimeMillis();
        if (currentTime - item.getSendTime() <= 300000) {
            helper.setText(R.id.tvTime, context.getString(R.string.recently));
        } else try {
            if (StringUtils.isNotEmpty(item.getSendTimeStr())) {
                if (DateUtils.IsToday(item.getSendTimeStr())) {
                    helper.setText(R.id.tvTime, item.getSendTimeStr().split(" ")[1]);
                } else {
                    helper.setText(R.id.tvTime, item.getSendTimeStr().split(" ")[0]);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
