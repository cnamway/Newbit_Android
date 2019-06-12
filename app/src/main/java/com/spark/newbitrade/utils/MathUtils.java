package com.spark.newbitrade.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/8/30.
 */

public class MathUtils {

    public static String getRundNumber(double number, int n, String pattern) {
        if (StringUtils.isEmpty(pattern)) pattern = "########0.";
        String str = "";
        if (n == 0) {
            pattern = "########0";
        } else {
            pattern = "########0.";
        }
        for (int j = 0; j < n; j++) str += "0";
        pattern += str;
        int m = (int) Math.pow(10, n);
        number = (Math.floor(number * m)) / (m * 1.0);
        return new DecimalFormat(pattern).format(number);
    }

//    public static double getDoubleTransferString(String strDouble) {
//        double transferNumber = 0.0;
//        if (StringUtils.isNotEmpty(strDouble)) {
//            BigDecimal bigDecimal = new BigDecimal(strDouble);
//            transferNumber = bigDecimal.doubleValue();
//        }
//        return transferNumber;
//    }
//
//    public static double mul(double num1, double num2) {
//        BigDecimal b1 = new BigDecimal(num1);
//        BigDecimal b2 = new BigDecimal(num2);
//        return b1.multiply(b2).doubleValue();
//    }
//

    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     *
     * @return d
     */
    public static double transform(double d) {
        BigDecimal bigDecimal = new BigDecimal("" + d);//不以科学计数法显示，正常显示保留两位小数
        double f = Double.parseDouble(subZeroAndDot(bigDecimal.setScale(8, BigDecimal.ROUND_HALF_DOWN).toString()));
        return f;
    }

    /**
     * 过滤多余的0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (StringUtils.isNotEmpty(s)) {
            if (s.indexOf(".") > 0) {
                s = s.replaceAll("0+?$", "");//去掉多余的0
                s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return s;
        } else {
            return "";
        }
    }

    public static double getDoudleByBigDecimal(BigDecimal bigDecimal) {
        if (bigDecimal != null) {
            return bigDecimal.doubleValue();
        } else {
            return 0;
        }
    }

    /**
     * 完成率
     *
     * @param num
     * @param total
     * @return
     */
    public static String getRate(int num, int total) {
        if (total == 0) {
            return "0%";
        }
        //0表示的是小数点  之前没有这样配置有问题例如  num=1 and total=1000  结果是.1  很郁闷
        DecimalFormat df = new DecimalFormat("0%");
        //可以设置精确几位小数
        df.setMaximumFractionDigits(0);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num * 1.0 / total * 1.0;
        return df.format(accuracy_num);
    }


}
