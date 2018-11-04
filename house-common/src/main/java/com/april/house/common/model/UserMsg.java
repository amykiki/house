package com.april.house.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserMsg {
    /** 数据库字段 */
    private Long id;
    private String msg;
    private Long userId;
    private Date createTime;
    private Long agentId;
    private Long houseId;
    private String userName;
    
    /** 非数据库字段 */
    private String email;



}
