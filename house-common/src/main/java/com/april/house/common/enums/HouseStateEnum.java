package com.april.house.common.enums;

import lombok.Getter;

@Getter
public enum HouseStateEnum {
    /** 上架 */
    FOR_SALE(1),
    /** 下架 */
    NOT_SALE(2);
    private Integer code;

    HouseStateEnum(Integer code) {
        this.code = code;
    }
}
