package com.april.house.common.model;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Table(name = "`blog`")
public class Blog {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 标签
     */
    @Column(name = "`tags`")
    private String tags;

    /**
     * 日期
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 标题
     */
    @Column(name = "`title`")
    private String title;

    /**
     * 分类1-准备买房 2-看房/选房 3-签约/订房 4-全款/贷款 5-缴税/过户 6-入住/交接 7-买房风险
     */
    @Column(name = "`cat`")
    private Integer cat;

    /**
     * 内容
     */
    @Column(name = "`content`")
    private String content;

    /** 非数据库字段 */
    /** 摘要 */
    @Transient
    private String digest;

    @Transient
    private List<String> tagList = Lists.newArrayList();
}