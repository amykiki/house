package com.april.house.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HouseUser {
    private Long id;
    private Long houseId;
    private Long userId;
    private Date createTime;
    /** 1-售卖 2-收藏 */
    private Integer type;
}
