package com.april.house.common.enums;

import lombok.Getter;

@Getter
public enum HouseUserTypeEnum {
    SOLD("1"),
    BOOKMARK("2");
    private String code;

    HouseUserTypeEnum(String code) {
        this.code = code;
    }
}
