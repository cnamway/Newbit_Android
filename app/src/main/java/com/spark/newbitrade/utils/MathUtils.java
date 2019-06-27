package com.spark.newbitrade.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/8/30.
 */

public class MathUtils {

    /**
     * 小数精确到具体位数
     *
     * @param number
     * @param n
     * @param pattern
     * @return
     */
    public static String getRundNumber(double number, int n, String pattern) {
        String text = Double.toString(Math.abs(number));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        if (decimalPlaces > n) {
            if (StringUtils.isEmpty(pattern)) pattern = "########0.";
            String str = "";
            for (int j = 0; j < n; j++) str += "0";
            pattern += str;
            int m = (int) Math.pow(10, n);
            number = (Math.floor(number * m)) / (m * 1.0);
            return new DecimalFormat(pattern).format(number);
        } else {
            return number + "";
        }
    }

    public static double getDoubleTransferString(String strDouble) {
        double transferNumber = 0.0;
        if (StringUtils.isNotEmpty(strDouble)) {
            BigDecimal bigDecimal = new BigDecimal(strDouble);
            transferNumber = bigDecimal.doubleValue();
        }
        return transferNumber;
    }

    /**
     * 多于8位以上的数字正常显示，不使用科学计数法
     *
     * @param doubleNum
     * @return
     */
    public static String formatENumber(double doubleNum) {
        return new DecimalFormat("#").format(doubleNum);
    }

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
     * 加（精确度）
     *
     * @param number1
     * @param number2
     * @param n
     * @return
     */
    public static String getBigDecimalAddWithScale(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal resultBD = aBD.add(bBD).setScale(n, BigDecimal.ROUND_HALF_UP);
            return resultBD.toPlainString();
        }
        return "0";
    }

    /**
     * 加
     *
     * @param number1
     * @param number2
     * @return
     */
    public static String getBigDecimalAdd(String number1, String number2) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal b1 = new BigDecimal(number1);
            BigDecimal b2 = new BigDecimal(number2);
            return b1.add(b2).toPlainString();
        }
        return "0";
    }


    /**
     * 减（精确度）
     *
     * @param number1
     * @param number2
     * @param n
     * @return
     */
    public static String getBigDecimalSubtractWithScale(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal resultBD = aBD.subtract(bBD).setScale(n, BigDecimal.ROUND_HALF_UP);
            return resultBD.toPlainString();
        }
        return "0";
    }

    /**
     * 减
     *
     * @param number1
     * @param number2
     * @return
     */
    public static String getBigDecimalSubtract(String number1, String number2) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal b1 = new BigDecimal(number1);
            BigDecimal b2 = new BigDecimal(number2);
            return b1.subtract(b2).toPlainString();
        }
        return "0";
    }

    /**
     * 乘（精确度）
     *
     * @param number1
     * @param number2
     * @param n
     * @return
     */
    public static String getBigDecimalMultiplyWithScale(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal resultBD = aBD.multiply(bBD).setScale(n, BigDecimal.ROUND_HALF_UP);
            return resultBD.toPlainString();
        }
        return "0";

    }

    /**
     * 乘
     *
     * @param number1
     * @param number2
     * @param n
     * @return
     */
    public static String getBigDecimalMultiply(String number1, String number2) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal b1 = new BigDecimal(number1);
            BigDecimal b2 = new BigDecimal(number2);
            return b1.multiply(b2).toPlainString();
        }
        return "0";
    }


    /**
     * 除（精确度）
     *
     * @param number1
     * @param number2
     * @param n
     * @return
     */
    public static String getBigDecimalDivideWithScale(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal resultBD = aBD.divide(bBD, n, BigDecimal.ROUND_HALF_UP);
            return resultBD.toPlainString();
        }
        return "0";
    }

    /**
     * 除
     *
     * @param number1
     * @param number2
     * @return
     */
    public static String getBigDecimalDivide(String number1, String number2) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal b1 = new BigDecimal(number1);
            BigDecimal b2 = new BigDecimal(number2);
            return b1.divide(b2).toPlainString();
        }
        return "0";
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
