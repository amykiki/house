package com.april.house.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Agency {
    /** 数据库字段 */
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String aboutUs;
    private String webSite;
    private String mobile;
}
