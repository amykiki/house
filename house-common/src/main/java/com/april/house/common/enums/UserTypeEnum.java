package com.april.house.common.enums;

import lombok.Getter;

@Getter
public enum UserTypeEnum {
    ORDINARY(1),
    AGENT(2);

    private Integer code;

    UserTypeEnum(Integer code) {
        this.code = code;
    }
}
