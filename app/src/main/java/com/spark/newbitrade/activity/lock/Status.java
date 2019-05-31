package com.spark.newbitrade.activity.lock;

import com.spark.newbitrade.R;

/**
 * Created by Administrator on 2018/3/25.
 */

public enum Status {
    CREDIT("请输入手势密码", R.color.grey_a5a5a5),
    //默认的状态，刚开始的时候（初始化状态）
    DEFAULT("绘制解锁图案", R.color.grey_a5a5a5),
    //第一次记录成功
    CORRECT("再次绘制解锁图案", R.color.grey_a5a5a5),
    //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
    LESSERROR("至少连接4个点，请重新输入", R.color.main_font_red_hover),
    //二次确认错误
    CONFIRMERROR("与上一次绘制不一致，请重新绘制", R.color.main_font_red_hover),
    //二次确认正确
    CONFIRMCORRECT("设置成功", R.color.grey_a5a5a5);

    private Status(String strId, int colorId) {
        this.strId = strId;
        this.colorId = colorId;
    }

    private String strId;
    private int colorId;

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }
}
