package com.spark.newbitrade.activity.bind_account;

import com.android.volley.VolleyError;
import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.otc.PayControllerModel;

/**
 * Created by Administrator on 2019/3/15 0015.
 */
public class BindBankPresenterImpl implements BindBankContract.Presenter {

    private BindBankContract.View bankView;
    private PayControllerModel payControllerModel;

    public BindBankPresenterImpl(BindBankContract.View bankView) {
        this.bankView = bankView;
        this.payControllerModel = new PayControllerModel();
    }

    @Override
    public void showLoading() {
        if (bankView != null) {
            bankView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (bankView != null) {
            bankView.hideLoading();
        }
    }

    @Override
    public void destory() {
        bankView = null;
    }


    /**
     * 绑定银行账户
     *
     * @param payType
     * @param payAddress
     * @param bankNum
     * @param branch
     * @param tradePassword
     */
    @Override
    public void doBindBank(String payType, String payAddress, final String bankNum, String branch, String tradePassword) {
        showLoading();
        payControllerModel.doBindBank(payType, payAddress, bankNum, branch, tradePassword, "",
                new ResponseCallBack.SuccessListener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.doBindBankSuccess(response);
                        }
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.dealError(httpErrorEntity);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.dealError(volleyError);
                        }
                    }
                });
    }

    /**
     * 更新银行账户信息
     *
     * @param id
     * @param payType
     * @param payAddress
     * @param bankNum
     * @param branch
     * @param tradePassword
     */
    @Override
    public void doUpdateBank(Long id, String payType, String payAddress, String bankNum, String branch, String tradePassword) {
        showLoading();
        payControllerModel.doUpdateBank(id, payType, payAddress, bankNum, branch, tradePassword, "",
                new ResponseCallBack.SuccessListener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.doUpdateBankSuccess(response);
                        }
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.dealError(httpErrorEntity);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.dealError(volleyError);
                        }
                    }
                });
    }
}
