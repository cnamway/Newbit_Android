package com.spark.newbitrade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.DepthResult;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.MathUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/29.
 */

public class DepthAdapter extends RecyclerView.Adapter<DepthAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<DepthResult.DepthListInfo> objBuyList;
    private ArrayList<DepthResult.DepthListInfo> objSellList;
    private double buyAccount;
    private double sellAccount;
    private int countWidth;
    private double alBuyAccount;
    private double alSellccount;
    private int baseCoinScale;
    private int coinScale;

    public void setBaseCoinScale(int baseCoinScale, int coinScale) {
        this.baseCoinScale = baseCoinScale;
        this.coinScale = coinScale;
    }

    public DepthAdapter(Context context) {
        this.context = context;
    }

    public void setObjBuyList(ArrayList<DepthResult.DepthListInfo> objBuyList) {
        this.objBuyList = objBuyList;
        if (objBuyList != null) {
            alBuyAccount = 0;
            for (int i = 0; i < objBuyList.size(); i++) {
                if (objBuyList.get(i).getAmount() != -1) {
                    alBuyAccount += objBuyList.get(i).getAmount();
                }
            }
        }
    }

    public void setObjSellList(ArrayList<DepthResult.DepthListInfo> objSellList) {
        this.objSellList = objSellList;
        if (objSellList != null) {
            alSellccount = 0;
            for (int i = 0; i < objSellList.size(); i++) {
                if (objSellList.get(i).getAmount() != -1) {
                    alSellccount += objSellList.get(i).getAmount();
                }
            }
        }
    }


    public void setWidth(int countWidth) {
        this.countWidth = countWidth;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_depth, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DepthResult.DepthListInfo buyInfo = objBuyList.get(position);
        DepthResult.DepthListInfo sellInfo = objSellList.get(position);
        holder.tvBuy.setText((position + 1) + "");
        holder.tvBuyNumber.setText(buyInfo.getAmount() == -1 ? "-- --" : MathUtils.subZeroAndDot(MathUtils.getRundNumber(buyInfo.getAmount(), coinScale, null)));
        holder.tvBuyPrice.setText(buyInfo.getPrice() == -1 ? "-- --" : MathUtils.subZeroAndDot(MathUtils.getRundNumber(buyInfo.getPrice(), baseCoinScale, null)));
        holder.tvSell.setText((position + 1) + "");
        holder.tvSellNumber.setText(sellInfo.getAmount() == -1 ? "-- --" : MathUtils.subZeroAndDot(MathUtils.getRundNumber(sellInfo.getAmount(), coinScale, null)));
        holder.tvSellPrice.setText(sellInfo.getPrice() == -1 ? "-- --" : MathUtils.subZeroAndDot(MathUtils.getRundNumber(sellInfo.getPrice(), baseCoinScale, null)));
        if (buyInfo.getAmount() != -1) {
            buyAccount = 0;
            for (int i = 0; i <= position; i++) {
                buyAccount += objBuyList.get(i).getAmount();
            }
            double buyScale = (buyAccount / alBuyAccount);
            RelativeLayout.LayoutParams buyParams = (RelativeLayout.LayoutParams) holder.buyView.getLayoutParams();
            buyParams.width = (int) (countWidth * buyScale);
            LogUtils.i(" buyParams.width ==" + buyParams.width);
            holder.buyView.setLayoutParams(buyParams);
        }
        if (sellInfo.getAmount() != -1) {
            sellAccount = 0;
            for (int i = 0; i <= position; i++) {
                sellAccount += objSellList.get(i).getAmount();
            }
            double sellScale = (sellAccount / alSellccount);
            RelativeLayout.LayoutParams sellParams = (RelativeLayout.LayoutParams) holder.sellView.getLayoutParams();
            sellParams.width = (int) (countWidth * sellScale);
            holder.sellView.setLayoutParams(sellParams);
        }
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBuy;
        private TextView tvBuyNumber;
        private TextView tvBuyPrice;
        private TextView tvSell;
        private TextView tvSellNumber;
        private TextView tvSellPrice;
        private ImageView sellView;
        private ImageView buyView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvBuy = itemView.findViewById(R.id.tvBuy);
            tvBuyNumber = itemView.findViewById(R.id.tvBuyNumber);
            tvBuyPrice = itemView.findViewById(R.id.tvBuyPrice);
            tvSell = itemView.findViewById(R.id.tvSell);
            tvSellNumber = itemView.findViewById(R.id.tvSellNumber);
            tvSellPrice = itemView.findViewById(R.id.tvSellPrice);
            sellView = itemView.findViewById(R.id.sellView);
            buyView = itemView.findViewById(R.id.buyView);
        }


    }
}
