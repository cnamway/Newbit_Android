package com.spark.newbitrade.activity.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.ChatListAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.ChatTable;
import com.spark.newbitrade.entity.ChatTipEvent;
import com.spark.newbitrade.entity.OrderDetial;
import com.spark.newbitrade.utils.DatabaseUtils;
import com.spark.newbitrade.utils.DpPxUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * 聊天列表
 */
public class ChatListActivity extends BaseActivity {
    @BindView(R.id.rvChat)
    SwipeMenuRecyclerView rvChat;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    private ChatListAdapter adapter;
    private List<ChatTable> chatLists = new ArrayList<>();
    private DatabaseUtils databaseUtils;
    private boolean hasNew = false;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_chat_list;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.chat_list));
        databaseUtils = new DatabaseUtils();
        addData();
        EventBus.getDefault().register(this);
    }

    private void addData() {
        List<ChatTable> chatTables = databaseUtils.findAll();
        if (chatTables == null || chatTables.size() == 0) {
            rvChat.setVisibility(View.GONE);
            rlEmpty.setVisibility(View.VISIBLE);
        } else {
            chatLists.clear();
            chatLists.addAll(chatTables);
            rvChat.setVisibility(View.VISIBLE);
            rlEmpty.setVisibility(View.GONE);
            initRvChat();
        }
    }

    /**
     * 初始化聊天列表
     */
    private void initRvChat() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvChat.setLayoutManager(manager);
        rvChat.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(ChatListActivity.this)
                        .setBackgroundColorResource(R.color.main_font_red)
                        .setText(getString(R.string.delete)) // 文字。
                        .setTextColor(Color.WHITE) // 文字颜色。
                        .setTextSize(16) // 文字大小。
                        .setWidth(DpPxUtils.dip2px(ChatListActivity.this, 80))
                        .setHeight(DpPxUtils.dip2px(ChatListActivity.this, 80));
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。.
            }
        });
        rvChat.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                databaseUtils.deleteByOrderId(chatLists.get(adapterPosition).getOrderId());
                chatLists.remove(adapterPosition);
                adapter.notifyDataSetChanged();
            }
        });
        Collections.reverse(chatLists);
        adapter = new ChatListAdapter(R.layout.item_chat_list, chatLists, this);
        rvChat.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ChatTable table = chatLists.get(position);
                table.setHasNew(false);
                table.setRead(true);
                table.setContent(table.getContent());
                table.setSendTimeStr(table.getSendTimeStr());
                table.setSendTime(table.getSendTime());
                databaseUtils.update(table);
                adapter.notifyDataSetChanged();
                OrderDetial orderDetial = new OrderDetial();
                orderDetial.setOrderSn(table.getOrderId());
                orderDetial.setMyId(table.getUidTo());
                orderDetial.setHisId(table.getUidFrom());
                orderDetial.setOtherSide(table.getNameFrom());
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderDetial", orderDetial);
                bundle.putBoolean("isChatList", true);
                showActivity(ChatActivity.class, bundle);
            }
        });
    }


    @Override
    protected void loadData() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getGroupChatEvent(ChatTipEvent tipEvent) {
        hasNew = tipEvent.isHasNew();
        if (hasNew) {
            chatLists.clear();
            chatLists.addAll(databaseUtils.findAll());
            for (ChatTable table : chatLists) {
                if (table.getOrderId().equals(tipEvent.getOrderId())) {
                    table.setHasNew(hasNew);
                    databaseUtils.deleteByOrderId(tipEvent.getOrderId());
                    databaseUtils.saveChat(table);
                }
            }
            chatLists.clear();
            chatLists.addAll(databaseUtils.findAll());
            Collections.reverse(chatLists);
            adapter.notifyDataSetChanged();
        } else {
            chatLists.clear();
            chatLists.addAll(databaseUtils.findAll());
            for (ChatTable table : chatLists) {
                if (table.getOrderId().equals(tipEvent.getOrderId())) {
                    table.setHasNew(hasNew);
                    databaseUtils.deleteByOrderId(tipEvent.getOrderId());
                    databaseUtils.saveChat(table);
                }
            }
            chatLists.clear();
            chatLists.addAll(databaseUtils.findAll());
            Collections.reverse(chatLists);
            adapter.notifyDataSetChanged();
        }
    }

}
