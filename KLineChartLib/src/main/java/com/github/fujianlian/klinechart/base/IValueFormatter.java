package com.github.fujianlian.klinechart.base;

/**
 * Value格式化接口
 * Created by tifezh on 2016/6/21.
 */

public interface IValueFormatter {
    /**
     * 格式化value
     *
     * @param value 传入的value值
     * @return 返回字符串
     */
    String format(float value);

    /**
     * 格式化value
     *
     * @param value 传入的value值
     * @param coinScale 传入的小数位数值
     * @return 返回字符串
     */
    String format(float value, int coinScale);
}
