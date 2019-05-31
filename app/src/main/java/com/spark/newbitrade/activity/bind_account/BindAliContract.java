package com.spark.newbitrade.activity.bind_account;


import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.base.BaseContract;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface BindAliContract {
    interface View extends BaseContract.BaseView {

        void uploadBase64PicSuccess(String obj);

        void doBindAliOrWechatSuccess(MessageResult obj);

        void doUpdateAliOrWechatSuccess(MessageResult obj);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void uploadBase64Pic(String base64);

        void getBindAliOrWechat(String payType, String payAddress, String bankNum, String branch, String tradePassword, String qrCodeUrl);

        void getUpdateAliOrWechat(Long id, String payType, String payAddress, String bankNum, String branch, String tradePassword, String qrCodeUrl);
    }


}
