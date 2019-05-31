package com.spark.newbitrade.entity;

/**
 * Created by Administrator on 2018/5/3 0003.
 */

public class BankName {
    private String bankName;
    private boolean isSelected;


    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getBankName() {
        return bankName;
    }
}
