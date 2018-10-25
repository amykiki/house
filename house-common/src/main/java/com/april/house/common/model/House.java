package com.april.house.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description :
 * @Creation Date:  2018-10-25 18:31
 * --------  ---------  --------------------------
 */
@Getter
@Setter
@ToString
public class House implements Serializable {
    private static final long serialVersionUID = 7193716309781177673L;

    private Long id;
    /** 参考 HouseTypeEnum */
    private Integer type;




}
