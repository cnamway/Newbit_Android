package com.spark.newbitrade.factory;


public class HttpUrls {

    /**
     * 主网络请求地址和cas登录地址
     */
    //public static final String HOST = "http://cas.www.allbtrade.com";

    public static final String HOST_LOGIN = "http://cas.www.allbtrade.com";
    public static final String UC_HOST = "http://api.allbtrade.com/uc";
    public static final String AC_HOST = "http://api.allbtrade.com/ac";
    public static final String OTC_HOST = "http://api.allbtrade.com/otc";
    public static final String CMS_HOST = "";

    /**
     * AC,UC,OTC模块地址后缀
     */
    public static final String TYPE_AC = "ac";
    public static final String TYPE_UC = "uc";
    public static final String TYPE_OTC = "otc";

    /**
     * 登录cas
     *
     * @return
     */
    public static String getCasLogin() {
        return HOST_LOGIN + "/cas/v1/tickets";
    }

    /**
     * 2.3 业务系统登录接口
     */
    public static String getCasTickets(String type) {
        return getBusinessUrl(type) + "/cas";
    }

    /**
     * 2.4 业务系统登录状态查询接口
     */
    public static String getCheckTicket(String type) {
        return getBusinessUrl(type) + "/check";
    }

    /**
     * 登录时需要调用的service，跟上面的2.3需要一直
     */
    public static String getService(String intType) {
        return getBusinessUrl(intType) + "/cas?client_name=CasClient";
    }

    private static String getBusinessUrl(String bussineType) {
        String url = "";
        switch (bussineType) {
            case TYPE_AC:
                url = AC_HOST;
                break;
            case TYPE_UC:
                url = UC_HOST;
                break;
            case TYPE_OTC:
                url = OTC_HOST;
                break;
        }
        return url;
    }

    /**
     * 获取手机验证
     */
    public static String getSendVertifyCodeUrl() {
        return HOST_LOGIN + "/cas/captcha/permission";
    }

    /**
     * 验证短信验证
     */
    public static String getVertifyCodeUrl() {
        return HOST_LOGIN + "/cas/captcha/check";
    }

    /**
     * 上传头像
     *
     * @return
     */
    public static String getAvatarUrl() {
        return UC_HOST + "/info/update/avatar";
    }

    /**
     * 绑定手机号
     *
     * @return
     */
    public static String getBindPhoneUrl() {
        return UC_HOST + "/bind/phone";
    }

    /**
     * 获取当前绑定手机的验证码
     *
     * @return
     */
    public static String getSendCodeAfterLoginUrl() {
        return UC_HOST + "/mobile/auth/code";
    }

    /**
     * 修改手机号
     *
     * @return
     */
    public static String getChangePhoneUrl() {
        return UC_HOST + "/info/change/phone";
    }


    /**
     * 推广好友
     *
     * @return
     */
    public static String getPromotionUrl() {
        return UC_HOST + "/promotion/record/page";
    }

    /**
     * 我的佣金
     *
     * @return
     */
    public static String getPromotionRewardUrl() {
        return UC_HOST + "/promotion/commission/record";
    }

    //获取APP版本更新信息
    public static String getAppVersionUsingGet() {
        return OTC_HOST + "/app/check-version?platform=1";
    }


    public static String getGeetest() {
        return UC_HOST + "/captcha/mm/gee";
    }
}
