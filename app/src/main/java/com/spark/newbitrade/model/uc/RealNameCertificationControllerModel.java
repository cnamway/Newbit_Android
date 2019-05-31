package com.spark.newbitrade.model.uc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.uc.api.RealNameCertificationControllerApi;
import com.spark.library.uc.model.MemberRealnameApply;
import com.spark.library.uc.model.MessageResult;
import com.spark.library.uc.model.MessageResultMemberRealnameApply;
import com.spark.library.uc.model.RealnameApplyDto;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.Credit;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;

import java.text.SimpleDateFormat;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;


/**
 * 实名认证
 */

public class RealNameCertificationControllerModel {
    private RealNameCertificationControllerApi realNameCertificationControllerApi;

    public RealNameCertificationControllerModel() {
        realNameCertificationControllerApi = new RealNameCertificationControllerApi();
        realNameCertificationControllerApi.setBasePath(HttpUrls.UC_HOST);
    }

    /**
     * 实名认证
     */
    public void credit(Long certifiedType, String idCardNumber, String identityCardImgFront, String identityCardImgInHand, String identityCardImgReverse, String realName,
                       final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final RealnameApplyDto realnameApplyDto = new RealnameApplyDto();
        realnameApplyDto.setCertifiedType(certifiedType);
        realnameApplyDto.setIdCardNumber(idCardNumber);
        realnameApplyDto.setIdentityCardImgFront(identityCardImgFront);
        realnameApplyDto.setIdentityCardImgInHand(identityCardImgInHand);
        realnameApplyDto.setIdentityCardImgReverse(identityCardImgReverse);
        realnameApplyDto.setRealName(realName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                realNameCertificationControllerApi.applyUsingPOST(realnameApplyDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getMessage());
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();
    }

    /**
     * 获取实名认证信息
     */
    public void getCreditInfo(final ResponseCallBack.SuccessListener<Credit.DataBean> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                realNameCertificationControllerApi.detailUsingPOST(new Response.Listener<MessageResultMemberRealnameApply>() {
                    @Override
                    public void onResponse(MessageResultMemberRealnameApply response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null) {
                                MemberRealnameApply data = response.getData();
                                Credit.DataBean dataBean = new Credit.DataBean();
                                if (data != null) {
                                    dataBean.setId(data.getId().intValue());
                                    dataBean.setRealName(data.getRealName());
                                    dataBean.setIdCard(data.getIdCardNumber());
                                    dataBean.setIdentityCardImgFront(data.getIdentityCardImgFront());
                                    dataBean.setIdentityCardImgReverse(data.getIdentityCardImgReverse());
                                    dataBean.setIdentityCardImgInHand(data.getIdentityCardImgInHand());
                                    dataBean.setAuditStatus(data.getAuditStatus());
                                    dataBean.setRejectReason(data.getRejectReason());
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    if (data.getCreateTime() != null)
                                        dataBean.setCreateTime(simpleDateFormat.format(data.getCreateTime()));
                                    if (data.getUpdateTime() != null)
                                        dataBean.setUpdateTime(simpleDateFormat.format(data.getUpdateTime()));
                                    dataBean.setCertifiedType(data.getCertifiedType().toString());
                                }
                                successListener.onResponse(dataBean);
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();
    }
}
