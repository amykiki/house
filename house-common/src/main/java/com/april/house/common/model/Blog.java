package com.april.house.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Blog {
    /** 数据库字段 */
    private Integer id;
    private String tags;
    private String content;
    private String title;
    private Date createTime;
    /** 分类 1-准备买房 2-看房/选房 3-签约/订房 4-全款/贷款 5-缴税/过户 6-入住/交接 7-买房风险*/
    private Integer cat;

    /** 非数据库字段 */
    /** 摘要 */
    private String digest;

}
