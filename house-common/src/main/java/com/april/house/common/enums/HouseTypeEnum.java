package com.april.house.common.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description :
 * @Creation Date:  2018-10-25 18:35
 * --------  ---------  --------------------------
 */
@Getter
public enum HouseTypeEnum {
    SALE(1),
    RENT(2);

    private Integer code;

    HouseTypeEnum(Integer code) {
        this.code = code;
    }
}
