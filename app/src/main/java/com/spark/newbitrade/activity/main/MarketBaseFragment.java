package com.spark.newbitrade.activity.main;

import android.content.Context;

import com.spark.newbitrade.base.BaseLazyFragment;
import com.spark.newbitrade.entity.Currency;

/**
 * Created by Administrator on 2018/2/26.
 */

public abstract class MarketBaseFragment extends BaseLazyFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof MarketOperateCallback)) {
            throw new RuntimeException("The Activity which this fragment is located must implement the MarketOperateCallback interface!");
        }
    }


    public interface MarketOperateCallback {
        int TYPE_SWITCH_SYMBOL = 0;
        int TYPE_TO_KLINE = 1;

        void itemClick(Currency currency, int type);
    }

}
