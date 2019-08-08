package com.spark.newbitrade.utils;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式化聊天时间
 * Created by ccs on 2018\8\28 0028.
 */

public class TimeUtil {
    /**
     * 日期格式化
     *
     * @return
     */
    public static String DateformatTimeForList(Date date) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换为日期
        long time = date.getTime();
        if (isThisYear(date)) {//今年
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            if (isToday(date)) { //今天
                return simpleDateFormat.format(date);
            } else {
                if (isYestYesterday(date)) {//昨天，显示昨天
                    return MyApplication.getApp().getString(R.string.str_time_format_yesterday);
                } else if (isThisWeek(date)) {//本周,显示周几
                    String weekday = null;
                    if (date.getDay() == 1) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_monday);
                    }
                    if (date.getDay() == 2) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_tuesday);
                    }
                    if (date.getDay() == 3) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_wednesday);
                    }
                    if (date.getDay() == 4) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_thursday);
                    }
                    if (date.getDay() == 5) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_friday);
                    }
                    if (date.getDay() == 6) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_saturday);
                    }
                    if (date.getDay() == 0) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_sunday);
                    }
                    return weekday;
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(date);
                }
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(date);
        }
    }

    /**
     * 日期格式化
     *
     * @return
     */
    public static String DateformatTime2(Date date) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换为日期
        long time = date.getTime();
        if (isThisYear(date)) {//今年
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            if (isToday(date)) { //今天
                int minute = minutesAgo(time);
                if (minute < 10) {//1小时之内
                    if (minute <= 1) {//一分钟之内，显示刚刚
                        return MyApplication.getApp().getString(R.string.str_time_format_just);
                    } else {
                        return minute + " " + MyApplication.getApp().getString(R.string.str_time_format_minutes_ago);
                    }
                } else {
                    return simpleDateFormat.format(date);
                }
            } else {
                if (isYestYesterday(date)) {//昨天，显示昨天
                    return MyApplication.getApp().getString(R.string.str_time_format_yesterday) + " " + simpleDateFormat.format(date);
                } else if (isThisWeek(date)) {//本周,显示周几
                    String weekday = null;
                    if (date.getDay() == 1) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_monday);
                    }
                    if (date.getDay() == 2) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_tuesday);
                    }
                    if (date.getDay() == 3) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_wednesday);
                    }
                    if (date.getDay() == 4) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_thursday);
                    }
                    if (date.getDay() == 5) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_friday);
                    }
                    if (date.getDay() == 6) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_saturday);
                    }
                    if (date.getDay() == 0) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_sunday);
                    }
                    return weekday + " " + simpleDateFormat.format(date);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                    return sdf.format(date);
                }
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(date);
        }
    }

    /**
     * String型时间戳格式化
     *
     * @return
     */
    public static String LongFormatTime(String time) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换为日期
        Date date = new Date();
        date.setTime(Long.parseLong(time));
        if (isThisYear(date)) {//今年
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            if (isToday(date)) { //今天
                int minute = minutesAgo(Long.parseLong(time));
                if (minute < 60) {//1小时之内
                    if (minute <= 1) {//一分钟之内，显示刚刚
                        return MyApplication.getApp().getString(R.string.str_time_format_just);
                    } else {
                        return minute + " " + MyApplication.getApp().getString(R.string.str_time_format_minutes_ago);
                    }
                } else {
                    return simpleDateFormat.format(date);
                }
            } else {
                if (isYestYesterday(date)) {//昨天，显示昨天
                    return MyApplication.getApp().getString(R.string.str_time_format_yesterday) + " " + simpleDateFormat.format(date);
                } else if (isThisWeek(date)) {//本周,显示周几
                    String weekday = null;
                    if (date.getDay() == 1) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_monday);
                    }
                    if (date.getDay() == 2) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_tuesday);
                    }
                    if (date.getDay() == 3) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_wednesday);
                    }
                    if (date.getDay() == 4) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_thursday);
                    }
                    if (date.getDay() == 5) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_friday);
                    }
                    if (date.getDay() == 6) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_saturday);
                    }
                    if (date.getDay() == 0) {
                        weekday = MyApplication.getApp().getString(R.string.str_time_format_sunday);
                    }
                    return weekday + " " + simpleDateFormat.format(date);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                    return sdf.format(date);
                }
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(date);
        }
    }

    private static int minutesAgo(long time) {
        return (int) ((System.currentTimeMillis() - time) / (60000));
    }

    private static boolean isToday(Date date) {
        Date now = new Date();
        return (date.getYear() == now.getYear()) && (date.getMonth() == now.getMonth()) && (date.getDate() == now.getDate());
    }

    private static boolean isYestYesterday(Date date) {
        Date now = new Date();
        return (date.getYear() == now.getYear()) && (date.getMonth() == now.getMonth()) && (date.getDate() + 1 == now.getDate());
    }

    private static boolean isThisWeek(Date date) {
        Date now = new Date();
        if ((date.getYear() == now.getYear()) && (date.getMonth() == now.getMonth())) {
            if (now.getDay() - date.getDay() < now.getDay() && now.getDate() - date.getDate() > 0 && now.getDate() - date.getDate() < 7) {
                return true;
            }
        }
        return false;
    }

    private static boolean isThisYear(Date date) {
        Date now = new Date();
        return date.getYear() == now.getYear();
    }
}