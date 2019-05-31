package com.spark.newbitrade.utils;

import java.util.regex.Pattern;

/**
 * 校验正则
 */

public class StringFormatUtils {
    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", email);
    }

    /**
     * 判断字符串是否符合手机号码格式
     * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
     * 电信号段: 133,149,153,170,173,177,180,181,189
     */
    public static boolean isMobile(String mobileNums) {
        String telRegex = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
        return Pattern.matches(telRegex, mobileNums);
    }

    /**
     * 判断是否为身份证号
     *
     * @param id
     * @return
     */
    public static boolean isIDCard(String id) {
        String idRegex = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        return Pattern.matches(idRegex, id);
    }


    /**
     * 过滤
     *
     * @param content
     * @return
     */
    public static String formatHtml(String content) {
        if (StringUtils.isNotEmpty(content)) {
            content = content.replaceAll("<em>", "");
            content = content.replace("</em>", "");
            content = content.replace("<br>", "\n");
            content = content.replace("</br>", "");
            content = content.replace("&nbsp;", " ");
            content = content.replace("<div>", "\n");
            content = content.replace("</div>", "");
            content = content.replace("<p>", "");
            content = content.replace("</p>", "");
            content = content.replaceAll("，", ",");
            return content;
        }
        return "";
    }
}
