package com.spark.newbitrade.factory;

import com.spark.newbitrade.utils.GlobalConstant;

/**
 * Created by Administrator on 2018/1/29.
 */
public class UrlFactory {
//    private static final String mainHost = "http://www.newbtc.online";

    //测试
    private static final String mainHost = "http://192.168.2.245";
    private static final String loginHost = "http://192.168.2.245:8446";

    private static final String ucHost = "http://192.168.2.245:48888";
    private static final String acHost = "http://192.168.2.245:48881";
    private static final String otcHost = "http://192.168.2.245:48887";

    private static final String hostAC = acHost;

    private static final String hostUC = ucHost;

    private static final String hostOTC = otcHost;


    //线上
    private static final String host = mainHost;

//    private static final String hostAC = mainHost;
//
//    private static final String hostUC = mainHost;
//
//    private static final String hostOTC = mainHost;

    private static final String hostExchange = mainHost;

    private static final String hostHome = mainHost;

    private static final String hostChat = mainHost;

    public static String getHost() {
        return host;
    }

    public static String getCodeUrl() {
        return hostUC + "/uc/mobile/code";
    }

    public static String getEmailCodeUrl() {
        return hostUC + "/uc/register/email/code";
    }

    public static String getRateUrl() {
        return hostExchange + "/market/exchange-rate/usd-cny";
    } // 获取汇率

    public static String getSignUpByPhone() {
        return hostUC + "/uc/register/phone";
    }

    public static String getSignUpByEmail() {
        return hostUC + "/uc/register/email";
    }

    public static String getLoginUrl() {
        return hostUC + "/uc/sign/in";
    }

    public static String getUcLoginOutUrl() {
        return hostUC + "/uc/logout";
    }

    public static String getAcLoginOutUrl() {
        return hostAC + "/ac/logout";
    }

    public static String getOtcLoginOutUrl() {
        return hostOTC + "/otc/logout";
    }

    public static String getKDataUrl() {
        return hostExchange + "/market/history";
    }

    public static String getAllCurrency() {
        return hostExchange + "/market/symbol-thumb";
    }

    public static String getAllCurrencys() {
        return hostExchange + "/market/overview";
    }

    public static String getSymbolInfo() {
        return hostExchange + "/market/symbol-info";
    }

    public static String getFindUrl() {
        return hostExchange + "/exchange/favor/find";
    }

    public static String getDeleteUrl() {
        return hostExchange + "/exchange/favor/delete";
    }

    public static String getAddUrl() {
        return hostExchange + "/exchange/favor/add";
    }

    public static String getExChangeUrl() {
        return hostExchange + "/exchange/order/add";
    }

    public static String getWalletUrl() {
        return hostAC + "/ac/asset/wallet/";
    }

    public static String getAllUrl() {
        return hostOTC + "/otc/coin/all";
    }

    public static String getAdvertiseUrl() {
        return hostOTC + "/otc/advertise/page-by-unit";
    }

    public static String getCountryUrl() {
        return hostUC + "/uc/register/support/country";
    }

    public static String getReleaseAdUrl() {
        return hostOTC + "/otc/advertise/create";
    }

    public static String getUploadPicUrl() {
        return hostUC + "/uc/upload/oss/base64";
    }

    public static String getCreditUrl() {
        return hostUC + "/uc/real/name/apply";
    }

    public static String getAccountPwdUrl() {
        return hostAC + "/ac/trade/password";
    }

    public static String getAllAdsUrl() {
        return hostOTC + "/otc/advertise/all";
    }

    public static String getReleaseUrl() {
        return hostOTC + "/otc/advertise/on/shelves";
    }

    public static String getDeleteAdsUrl() {
        return hostOTC + "/otc/advertise/delete";
    }

    public static String getOffShelfUrl() {
        return hostOTC + "/otc/advertise/off/shelves";
    }

    public static String getAdDetailUrl() {
        return hostOTC + "/otc/advertise/detail";
    }

    public static String getUpdateAdUrl() {
        return hostOTC + "/otc/advertise/update";
    }

    public static String getC2CInfoUrl() {
        return hostOTC + "/otc/order/pre";
    }

    public static String getC2CBuyUrl() {
        return hostOTC + "/otc/order/buy";
    }

    public static String getC2CSellUrl() {
        return hostOTC + "/otc/order/sell";
    }

    public static String getMyOrderUrl() {
        return hostOTC + "/otc/order/self";
    }

    public static String getExtractinfoUrl() {
        return hostAC + "/ac/withdraw/support/coin/info";
    }

    public static String getExtractUrl() {
        return hostAC + "/ac/withdraw/apply";
    }

    public static String getExtractAddressUrl() {
        return hostAC + "/ac/withdraw/address/page";
    }

    public static String getAllTransactionUrl() {
        return hostAC + "/ac/asset/transaction/all";
    }

    public static String getSafeSettingUrl() {
        return hostAC + "/ac/trade/security/setting";
    }

    public static String getAccountSettingUrl() {
        return hostAC + "/ac/approve/account/setting";
    }

    public static String getAvatarUrl() {
        return hostUC + "/uc/info/update/avatar";
    }

    public static String getBindPhoneUrl() {
        return hostUC + "/uc/bind/phone";
    }


    public static String getSendCodeAfterLoginUrl() {
        return hostUC + "/uc/mobile/auth/code";
    }

    public static String getBindEmailUrl() {
        return hostUC + "/uc/bind/email";
    }

    public static String getSendEmailCodeUrl() {
        return hostUC + "/uc/bind/email/code";
    }

    public static String getEditLoginPwdUrl() {
        return hostUC + "/uc/mobile/update/password/code";
    }

    public static String getEditPwdUrl() {
        return hostUC + "/uc/info/update/password";
    }

    public static String getPlateUrl() {
        return hostExchange + "/market/exchange-plate";
    }

    public static String getEntrustUrl() {
        return hostExchange + "/exchange/order/current";
    } // 查询当前委托

    public static String getHistoryEntrus() {
        return hostExchange + "/exchange/order/history";
    } // 获取历史委托记录

    public static String getCancleEntrustUrl() {
        return hostExchange + "/exchange/order/cancel/";
    }


    public static String getEmailForgotPwdCodeUrl() {
        return hostUC + "/uc/info/email/code";
    }

    public static String getForgotPwdUrl() {
        return hostUC + "/uc/info/reset/login/password";
    }

    public static String getCaptchaUrl() {
        return hostUC + "/uc/gee/captcha";
    }

    public static String getChangePhoneUrl() {
        return hostUC + "/uc/info/change/phone";
    }

    public static String getMessageUrl() {
        return hostHome + "/home/api/cms/notice/findBy";
    }

    public static String getMessageDetailUrl() {
        return hostUC + "/uc/announcement/";
    }

    public static String getRemarkUrl() {
        return hostUC + "/uc/feedback";
    }

    public static String getAppInfoUrl() {
        return hostUC + "/uc/ancillary/website/info";
    }

    public static String getBannersUrl() {
        return hostHome + "/home/api/cms/webConfig/findInfo";
    }

    public static String getOrderDetailUrl() {
        return hostOTC + "/otc/order/detail";
    }

    public static String getCancleUrl() {
        return hostOTC + "/otc/order/cancel";
    }

    public static String getpayDoneUrl() {
        return hostOTC + "/otc/order/pay";
    }

    public static String getReleaseOrderUrl() {
        return hostOTC + "/otc/order/release";
    }

    public static String getAppealUrl() {
        return hostOTC + "/otc/appeal/apply";
    }

    public static String getEditAccountPwdUrl() {
        return hostAC + "/ac/trade/update/password";
    }

    public static String getResetAccountPwdUrl() {
        return hostAC + "/ac/trade/reset/password";
    }


    public static String getHistoryMessageUrl() {
        return hostChat + "/chat/getHistoryMessage";
    }


    public static String getCreditInfo() {
        return hostUC + "/uc/real/name/detail";
    }

    public static String getNewVision() {
        return hostUC + "/uc/ancillary/system/app/version/0";
    }

    public static String getSymbolUrl() {
        return hostExchange + "/market/symbol";
    }


    public static String getBindAccountUrl() {
        return hostAC + "/ac/trade/set/pay";
    }

    public static String getUpdateAccountUrl() {
        return hostAC + "/ac/trade/update/pay";
    }

    public static String getCheckMatchUrl() {
        return hostUC + "/uc/asset/wallet/match-check";
    }

    public static String getStartMatchUrl() {
        return hostUC + "/uc/asset/wallet/match";
    }

    public static String getPromotionUrl() {
        return hostUC + "/uc/promotion/record/page";
    }

    public static String getPromotionRewardUrl() {
        return hostUC + "/uc/promotion/commission/record";
    }

    public static String getGoogleCode() {
        return hostUC + "/uc/google/sendgoogle";
    }

    public static String getUnBindGoogleCode() {
        return hostUC + "/uc/google/jcgoogle";
    }

    public static String getBindGoogleCode() {
        return hostUC + "/uc/google/googleAuth";
    }

    public static String getDepth() {
        return hostExchange + "/market/exchange-plate-full";
    } // 获取深度图数据

    public static String getVolume() {
        return hostExchange + "/market/latest-trade";
    } // 获取成交数据

    public static String getUploadPicFileUrl() {
        return hostUC + "/uc/upload/oss/image";
    }

    public static String getTradeLogin() {
        return hostExchange + "/exchange/login";
    }

    public static String getUCLogin() {
        return hostUC + "/uc/login";
    }

    public static String getUserInfo() {
        return hostUC + "/uc/sign/getUserInfo";
    }

    public static String getACLogin() {
        return hostAC + "/ac/login";
    }

    public static String getOTCLogin() {
        return hostOTC + "/otc/login";
    }


    public static String getOldEmailCode() {
        return hostUC + "/uc/approve/untie/email/code";
    }

    public static String getNewEmailCode() {
        return hostUC + "/uc/approve/update/email/code";
    }

    public static String getUnbindEmail() {
        return hostUC + "/uc/approve/update/email";
    }

    public static String getAddExtractAddress() {
        return hostAC + "/ac/withdraw/address/add";
    }

    public static String getBusinessStatus() {
        return hostAC + "/ac/business/status";
    }

    public static String getNewTen() {
        return hostOTC + "/otc/advertise/newest";
    }

    public static String queryOtcCoinRate() {
        return host + "/otc/advertise/getOtcCoin";
    }

    /**
     * 2.1登录接口，会返回tgc字段 cas系统登录识别号
     *
     * @return
     */
    public static String getCasLogin() {
        return loginHost + "/cas/v1/tickets";
    }

    /**
     * 2.2获取业务系统登录凭据接口
     * 通过servcie和tgc获取到登录子系统的凭证，servcie的内容需要和后台系统协商好，需要保持一直，否则会导致无法识别。
     */
    public static String getServiceTickets() {
        return loginHost + "/cas/v1/tickets/";
    }

    /**
     * 2.3 业务系统登录接口
     */
    public static String getCasTickets(int type) {
        if (type == GlobalConstant.TYPE_UC) {
            return mainHost + "/uc/cas";
        } else if (type == GlobalConstant.TYPE_SPOT) {
            return mainHost + "/spot/cas";
        } else if (type == GlobalConstant.TYPE_AC) {
            return mainHost + "/ac/cas";
        } else if (type == GlobalConstant.TYPE_OTC) {
            return mainHost + "/otc/cas";
        } else {
            return mainHost + "/uc/cas";
        }
    }

    /**
     * 登录时需要调用的service，跟上面的2.3需要一直
     */
    public static String getService(int intType) {
        if (intType == GlobalConstant.TYPE_UC) {
            return mainHost + "/uc/cas?client_name=CasClient";
        } else if (intType == GlobalConstant.TYPE_SPOT) {
            return mainHost + "/spot/cas?client_name=CasClient";
        } else if (intType == GlobalConstant.TYPE_AC) {
            return mainHost + "/ac/cas?client_name=CasClient";
        } else if (intType == GlobalConstant.TYPE_OTC) {
            return mainHost + "/otc/cas?client_name=CasClient";
        } else {
            return mainHost + "/spot/cas?client_name=CasClient";
        }
    }

    /**
     * 2.4 业务系统登录状态查询接口
     */
    public static String getCheckTicket(int type) {
        if (type == GlobalConstant.TYPE_UC) {
            return mainHost + "/uc/check";
        } else if (type == GlobalConstant.TYPE_SPOT) {
            return mainHost + "/spot/check";
        } else if (type == GlobalConstant.TYPE_AC) {
            return mainHost + "/ac/check";
        } else if (type == GlobalConstant.TYPE_OTC) {
            return mainHost + "/otc/check";
        } else {
            return mainHost + "/uc/check";
        }
    }
}

