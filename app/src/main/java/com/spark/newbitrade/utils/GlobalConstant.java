package com.spark.newbitrade.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/5.
 */

public class GlobalConstant {
    public static final String service = "service"; // 所有的订阅  都加参数service
    public static final String SPOT = "SPOT"; // 参数传的值
    public static final String LowercaseSPOT = "spot"; // 参数传的值

    public static HashMap<String, String> getMAP() {
        HashMap<String, String> map = new HashMap<>();
        map.put(service, SPOT);
        return map;
    }

    public static final boolean isDebug = true; // 是否代开log日志
    public static final boolean isOPenGoogle = false; // 是否打开谷歌验证
    public static final boolean isUpLoadFile = false; // 上传图片是否为文件上传

    //TOKEN失效错误码
    public static final int TOKEN_DISABLE1 = 404;
    public static final int TOKEN_DISABLE2 = -1;
    public static final int TOKEN_TRADE_LOGIN_ERROR = 504;
    public static final int BUSINESS_LOGIN_SUCCESS = 200;
    public static final int CAPTCH = 411; // 全局拦截，显示极验
    public static final int CAPTCH2 = 412; //解决验证码失效问题
    public static final int LOGIN_ERROR = 401; // 用户名密码错误/业务系统失效
    public static final int CAPTCHA_HADBEEN_SEND = 20001; // 用户名密码错误/业务系统失效
    //自定义错误码
    public static final int JSON_ERROR = -9999;
    public static final int VOLLEY_ERROR = -9998;
    public static final int TOAST_MESSAGE = -9997;
    public static final int OKHTTP_ERROR = -9996;
    public static final int NO_DATA = -9995;
    public static final int SERVER_ERROR = -9994;
    public static final int REQUEST_CANCEL = -9993;

    ///////////////////permission
    public static final int PERMISSION_CONTACT = 0;
    public static final int PERMISSION_CAMERA = 1;
    public static final int PERMISSION_STORAGE = 2;

    //常用常量
    public static final int TAKE_PHOTO = 10;
    public static final int CHOOSE_ALBUM = 11;

    /**
     * k线图对应tag值
     */
    public static final int TAG_DIVIDE_TIME = 0; // 分时图
    public static final int TAG_ONE_MINUTE = 1; // 1分钟
    public static final int TAG_FIVE_MINUTE = 2; // 5分钟
    public static final int TAG_AN_HOUR = 3; // 1小时
    public static final int TAG_DAY = 4; // 1天
    public static final int TAG_FIFTEEN_MINUTES = 5; // 15分钟
    public static final int TAG_THIRTY_MINUTE = 6; // 30分钟
    public static final int TAG_WEEK = 7; // 1周
    public static final int TAG_MONTH = 8; // 1月


    /**
     * 应用该自定义常量
     */
    public static final int SUCCESS_CODE = 200; // 正确的数据返回码
    public static final int SUCCESS_SEC_CODE = 6; // ac,otc登录成功码
    public static final String LIMIT_PRICE = "LIMIT_PRICE"; // 限价
    public static final String MARKET_PRICE = "MARKET_PRICE"; // 市价
    public static final int INT_LIMIT_PRICE = 1; // 限价
    public static final int INT_MARKET_PRICE = 0; // 市价
    public static final String BUY = "BUY"; // 买
    public static final String SELL = "SELL"; // 卖
    public static final int INT_BUY = 0; // 买
    public static final int INT_SELL = 1; // 卖
    public static final String CNY = "CNY"; // 人民币
    public static final String HK = "HK";
    public static final String USD = "USD";
    public static final String JPY = "JPY";
    public static final int PageSize = 10;
    public static final String UserSaveFileName = "user.info";
    public static final String LOGIN_LANGUAGE = "LOGIN_LANGUAGE";

    public static final String alipay = "alipay"; // 支付宝
    public static final String wechat = "wechat"; // 微信
    public static final String card = "card"; // 银行卡
    public static final String PAYPAL = "paypal"; // PAYPAL
    public static final String other = "other"; // 其他

    public static final int CODE_TRADE = 0; // 交易code
    public static final int CODE_CHAT = 1; // 聊天code
    public static final int CODE_MARKET = 2; // 行情code
    public static final int CODE_KLINE = 3; // k线code
    public static final String MerchantCode = "20180904094615217"; // 首页轮播图code
    public static final String COOKIE_NAME = "LOGIN_COOKIE";
    public static final int TYPE_UC = 1;
    public static final int TYPE_SPOT = 2;
    public static final int TYPE_AC = 3;
    public static final int TYPE_SPOT_TCP = 4;
    public static final int TYPE_OTC = 5;
    public static final int TYPE_OTC_SYSTEM = 6;

    public static final String COMMON = "COMMON";
    //public static final String ACP = "ACP";
    public static final String OTC = "OTC";


}
