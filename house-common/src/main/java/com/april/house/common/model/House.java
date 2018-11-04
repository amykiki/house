package com.april.house.common.model;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class House implements Serializable {
    private static final long serialVersionUID = 7193716309781177673L;

    /** 数据库字段 */
    private Long id;
    private Integer type;
    private Integer price;
    private String name;
    private String images;
    /** 面积 */
    private Integer area;
    /** 卧室 */
    private Integer beds;
    /** 浴室 */
    private Integer baths;
    /** 评分 */
    private Double rating;

    private String remarks;
    private String properties;
    /** 户型图 */
    private String floorPlan;
    private String tags;
    private Date createTime;
    private Integer cityId;
    private Integer communityId;
    private String address;
    /** 1-上架 2-下架 */
    private Integer state;

    /** 非数据库字段 */

    /** 房子展示第一张图 */
    private String firstImg;
    private Integer roundRating = 0;
    private List<String> imageList = Lists.newArrayList();
    private List<String> floorPlanList = Lists.newArrayList();
    private List<String> featureList = Lists.newArrayList();
    private List<MultipartFile> houseFiles;
    private List<MultipartFile> floorPlanFiles;
    private String priceStr;
    private String typeStr;
    
    /** 以下字段用于sql查询条件 */
    /** 关联用户id */
    private Long userId;
    /** 是否被收藏 */
    private Boolean bookmarked;
    private List<Long> ids;
    /** 用于sql排序: price_desc, price_asc, time_desc */
    private String sort = "time desc";





}
