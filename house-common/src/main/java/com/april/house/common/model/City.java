package com.april.house.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {
    /** 数据库字段 */
    private Integer id;
    private String cityName;
    private String cityCode;
}
