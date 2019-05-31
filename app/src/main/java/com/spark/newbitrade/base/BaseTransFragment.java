package com.spark.newbitrade.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Administrator on 2018/1/9.
 */

public abstract class BaseTransFragment extends BaseFragment {
    protected boolean isFirst = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && !isFirst && isNeedLoad) loadData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (immersionBar != null) initImmersionBar();
            if (!isNeedLoad) return;
            rootView.post(new Runnable() {
                @Override
                public void run() {
                    loadData();
                    isFirst = false;
                }
            });
        }
    }

    protected abstract String getmTag();

}
