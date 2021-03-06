package com.april.house.common.enums;

import lombok.Getter;

@Getter
public enum CommentTypeEnum {
    /** 房产评论 */
    HOUSE_CMT(1),
    /** 博客评论 */
    BLOG_CMT(2);
    private Integer code;

    CommentTypeEnum(Integer code) {
        this.code = code;
    }
}
