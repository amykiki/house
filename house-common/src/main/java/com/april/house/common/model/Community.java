package com.april.house.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Community {
    /** 数据库字段 */
    private Integer id;
    private String cityCode;
    /** 城市名称 */
    private String cityName;
    /** 小区名称 */
    private String name;
}
