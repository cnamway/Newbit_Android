package com.github.fujianlian.klinechart.formatter;

import com.github.fujianlian.klinechart.base.IValueFormatter;

import java.util.Locale;

/**
 * Value格式化类
 * Created by tifezh on 2016/6/21.
 */

public class ValueFormatter implements IValueFormatter {
    @Override
    public String format(float value) {
        return String.format("%.2f", value);
    }

    @Override
    public String format(float value, int coinScale) {
        return String.format(Locale.getDefault(), "%." + coinScale + "f", value);
    }
}
