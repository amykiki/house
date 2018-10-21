package com.april.house.common.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class ModelBean {
    private Long id;
    private String name;
    private Date createTime;
    private Timestamp updateTime;
    private Boolean enable;
    private Character charname;
    private BigDecimal bd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Character getCharname() {
        return charname;
    }

    public void setCharname(Character charname) {
        this.charname = charname;
    }

    public BigDecimal getBd() {
        return bd;
    }

    public void setBd(BigDecimal bd) {
        this.bd = bd;
    }

    @Override
    public String toString() {
        return "ModelBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enable=" + enable +
                ", charname=" + charname +
                ", bd=" + bd +
                '}';
    }
}
