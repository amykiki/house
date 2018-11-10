package com.april.house.common.enums;

import lombok.Getter;

@Getter
public enum HouseUserTypeEnum {
    SOLD(1),
    BOOKMARK(2);
    private Integer code;

    HouseUserTypeEnum(Integer code) {
        this.code = code;
    }
}
