package com.spark.newbitrade.activity.mychart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wuzongjie on 2018/10/23
 * K线推送下来的数据
 */
public class KSocketBean {

    private float closePrice;
    private float count;
    private float highestPrice;
    private float lowestPrice;
    private float openPrice;
    private String period;
    private long time;
    private float volume;

    public float getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(float closePrice) {
        this.closePrice = closePrice;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public float getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(float highestPrice) {
        this.highestPrice = highestPrice;
    }

    public float getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(float lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
    public long getTimeLong(){
        return time;
    }
    public String getTime() {
        return DataParse.getFormatTime("MM-dd HH:mm", new Date(time));
    }

    /**
     * 判断2个字符的时间 哪个大
     * @return 第一个大则为true
     */
    public static boolean compare(String time1,String time2,int tag) {
        SimpleDateFormat sdf = null;
        if(tag == 4 || tag == 6 || tag == 7){
            sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }else {
            sdf = new SimpleDateFormat("MM-dd hh:mm", Locale.getDefault());
        }
        // 将字符串形式的时间转为Date类型的时间
        try {
            Date a = sdf.parse(time1);
            Date b = sdf.parse(time2);
            return a.getTime() > b.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
