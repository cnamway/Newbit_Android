package com.spark.newbitrade.entity;

/**
 * Created by Administrator on 2018/10/9 0009.
 */

public class FilterBean {
    private String name;
    private boolean isSelected;
    private int intUpLoad;
    private String strUpload;

    public FilterBean(String name, String strUpload, boolean isSelected) {
        this.name = name;
        this.strUpload = strUpload;
        this.isSelected = isSelected;
    }

    public FilterBean() {
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntUpLoad(int intUpLoad) {
        this.intUpLoad = intUpLoad;
    }

    public void setStrUpload(String strUpload) {
        this.strUpload = strUpload;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public int getIntUpLoad() {
        return intUpLoad;
    }

    public String getStrUpload() {
        return strUpload;
    }
}
