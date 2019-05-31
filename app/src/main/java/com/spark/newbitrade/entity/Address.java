package com.spark.newbitrade.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/8.
 */

public class Address implements Serializable {
    private String remark;
    private String address;
    private String coinId;
    private Date createTime;
    private Date deleteTime;
    private Long id;
    private Long memberId;
    private Integer status;

    public String getAddress() {
        return address;
    }

    public String getRemark() {
        return remark;
    }

    public String getCoinId() {
        return coinId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Integer getStatus() {
        return status;
    }
}
