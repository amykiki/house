package com.april.house.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.*;

@Getter
@Setter
@Table(name = "`comment`")
public class Comment {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 评论内容
     */
    @Column(name = "`content`")
    private String content;

    /**
     * 房屋id
     */
    @Column(name = "`house_id`")
    private Long houseId;

    /**
     * 博客id
     */
    @Column(name = "`blog_id`")
    private Integer blogId;

    /**
     * 类型1-房产评论 2-博客评论
     */
    @Column(name = "`type`")
    private Boolean type;

    /**
     * 评论用户
     */
    @Column(name = "`user_id`")
    private Long userId;

    /**
     * 发布时间戳
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /** 非数据库字段 */
    @Transient
    private String userName;
    @Transient
    private String avatar;


}