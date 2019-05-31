package com.spark.newbitrade.activity.myinfo;


import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.User;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface MyInfoContract {

    interface MyInfoView extends BaseContract.BaseView {

        void uploadBase64PicSuccess(String obj);

        void avatarSuccess(String obj);

        void getUserInfoSuccess(User user);
    }

    interface MyInfoPresenter extends BaseContract.BasePresenter {

        void uploadBase64Pic(String base64);

        void avatar(String url);

        void getUserInfo();
    }


}
