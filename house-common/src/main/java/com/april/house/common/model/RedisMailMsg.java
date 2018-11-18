package com.april.house.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
/**
 * 之所以要新建该类，是因为SimpleMailMessage在redis反序列化时，
 * 因为to为String[]类型，但是其set函数又为 setTo(String to)或者setTo(String... to)
 * 导致在反序列化时，报错Cannot deserialize instance of `java.lang.String` out of START_ARRAY token
 */
public class RedisMailMsg implements Serializable {
    private static final long serialVersionUID = -8313188559834154326L;
    private String from;
    private String to;
    private String title;
    private String text;
}
