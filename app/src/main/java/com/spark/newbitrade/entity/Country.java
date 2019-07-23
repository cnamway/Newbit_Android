package com.spark.newbitrade.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Administrator on 2018/3/1.
 */

public class Country implements Serializable {
    @SerializedName("areaCode")
    private String areaCode = null;
    @SerializedName("enName")
    private String enName = null;
    @SerializedName("language")
    private String language = null;
    @SerializedName("localCurrency")
    private String localCurrency = null;
    @SerializedName("sort")
    private Integer sort = null;
    @SerializedName("sysLanguage")
    private String sysLanguage = null;
    @SerializedName("zhName")
    private String zhName = null;

    public Country() {
    }

    @ApiModelProperty("编码")
    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @ApiModelProperty("英文名称")
    public String getEnName() {
        return this.enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    @ApiModelProperty("语言")
    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @ApiModelProperty("法定货币")
    public String getLocalCurrency() {
        return this.localCurrency;
    }

    public void setLocalCurrency(String localCurrency) {
        this.localCurrency = localCurrency;
    }

    @ApiModelProperty("查询排序")
    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @ApiModelProperty("")
    public String getSysLanguage() {
        return this.sysLanguage;
    }

    public void setSysLanguage(String sysLanguage) {
        this.sysLanguage = sysLanguage;
    }

    @ApiModelProperty("中文名称")
    public String getZhName() {
        return this.zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Country {\n");
        sb.append("  areaCode: ").append(this.areaCode).append("\n");
        sb.append("  enName: ").append(this.enName).append("\n");
        sb.append("  language: ").append(this.language).append("\n");
        sb.append("  localCurrency: ").append(this.localCurrency).append("\n");
        sb.append("  sort: ").append(this.sort).append("\n");
        sb.append("  sysLanguage: ").append(this.sysLanguage).append("\n");
        sb.append("  zhName: ").append(this.zhName).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
