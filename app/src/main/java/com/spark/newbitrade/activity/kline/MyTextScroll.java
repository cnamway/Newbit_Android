package com.spark.newbitrade.activity.kline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by wuzongjie on 2018/10/24
 */
public class MyTextScroll extends ScrollView{
    public MyTextScroll(Context context) {
        super(context);
    }

    public MyTextScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN || ev.getPointerCount() > 1) {
            return false;
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getX();
            downY = ev.getY();

        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float diffMoveX = Math.abs(ev.getX() - downX);
            float diffMoveY = Math.abs(ev.getY() - downY);

            if ((isVerticalMove || diffMoveY > diffMoveX + 5) && !isHorizontalMove) {
                isVerticalMove = true;
                return true;

            } else if ((isHorizontalMove || diffMoveX > diffMoveY + 5) && !isVerticalMove) {
                isHorizontalMove = true;
                return false;
            }

        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            isVerticalMove = false;
            isHorizontalMove = false;
        }
        return super.onInterceptTouchEvent(ev);

    }

    private boolean isVerticalMove = true;
    private boolean isHorizontalMove = true;
    float downX = 0;
    float downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            isVerticalMove = false;
            isHorizontalMove = false;
        }
        return super.onTouchEvent(ev);
    }

}
