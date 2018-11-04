package com.april.house.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Comment {
    /** 数据库字段 */
    private Long id;
    private String content;
    private Long houseId;
    private Date createTime;
    private Integer blogId;
    /** 类型1-房产评论 2-博客评论 */
    private Integer type;
    private Long userId;

    /** 非数据库字段 */
    private String userName;
    private String avatar;
}
