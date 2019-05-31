package com.spark.newbitrade.utils;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.base.DatabaseOpenHelper;
import com.spark.newbitrade.entity.ChatTable;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/19 0019.
 * 数据库操作工具类
 */

public class DatabaseUtils {
    private DbManager db;

    //接收构造方法初始化的DbManager对象
    public DatabaseUtils() {
        db = DatabaseOpenHelper.getInstance();
    }


    public void saveChat(ChatTable entity) {
        try {
            db.saveBindingId(entity);
            LogUtils.i("save成功   " + entity.toString());
        } catch (DbException e) {
            e.printStackTrace();
            LogUtils.i("save失败  " + e.toString());
        }
    }

    public List<ChatTable> findAll() {
        List<ChatTable> list = new ArrayList<>();
        try {
            list = db.selector(ChatTable.class).where("uidTo", "=", MyApplication.app.getCurrentUser().getId()).findAll();
            //LogUtils.logi("DatabaseUtils","findAll成功   "+list.size());
        } catch (DbException e) {
            e.printStackTrace();
            LogUtils.i("findAll失败  " + e.toString());
        }
        if (list == null) {
            return null;
        } else return list;
    }

    public List<ChatTable> findByOrder(String orderId) {
        List<ChatTable> list = new ArrayList<>();
        try {
            list = db.selector(ChatTable.class).where("orderId", "=", orderId).findAll();
//            LogUtils.logi("DatabaseUtils","findByOrder成功   "+orderId+"   list.size   "+list.size());
        } catch (DbException e) {
            e.printStackTrace();
            LogUtils.i("findByOrder失败  " + e.toString());
        }
        if (list == null) {
            return null;
        } else return list;
    }

    public void update(ChatTable table) {
        try {
            db.update(table, "content", "isRead", "hasNew");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void deleteByOrderId(String orderId) {
        WhereBuilder builder = WhereBuilder.b("orderId", "=", orderId);
        try {
            db.delete(ChatTable.class, builder);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
