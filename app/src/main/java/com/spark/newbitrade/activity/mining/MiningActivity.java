package com.spark.newbitrade.activity.mining;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/12/26 0026.
 */

public class MiningActivity extends BaseActivity {

    @BindView(R.id.tvGoto)
    TextView tvGoto;
    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_mining;
    }

    @Override
    protected void initView() {
        super.initView();
        llTitle.setBackgroundResource(R.color.transparent);
        ivBack.setBackgroundResource(R.color.transparent);
        tvGoto.setBackgroundResource(R.color.transparent);
        ivBack.setImageResource(R.mipmap.icon_back_white);
        tvTitle.setTextColor(getResources().getColor(R.color.white));
        tvGoto.setTextColor(getResources().getColor(R.color.white));
        setSetTitleAndBack(false, true);
        tvTitle.setText(R.string.str_mining);
        tvGoto.setVisibility(View.VISIBLE);
        tvGoto.setText(R.string.str_ruler);
    }


    @OnClick(R.id.tvGoto)
    public void onViewClicked() {

    }
}
