package com.april.house.common.page;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description : 分页参数
 * @Creation Date:  2018-10-25 18:06
 * --------  ---------  --------------------------
 */
@Getter
@Setter
public class PageParams {
    private static final Integer PAGE_SIZE = 5;

    private Integer pageSize;
    private Integer pageNum;
    private Integer offset;
    private Integer limit;
    private Boolean count;
    private String orderBy;

    public PageParams(Integer pageSize, Integer pageNum, Boolean count) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.offset = pageSize * (pageNum - 1);
        this.limit = pageSize;
        this.count = count;
    }

    /**
     * 默认第一页，默认pageSize
     */
    public PageParams() {
        this(PAGE_SIZE, 1, true);
    }

    public static PageParams build(Integer pageSize, Integer pageNum) {
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        if (pageNum == null) {
            pageNum = 1;
        }

        return new PageParams(pageSize, pageNum, true);
    }

    public static PageParams build(Integer pageSize, Integer pageNum, Boolean count) {
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        if (pageNum == null) {
            pageNum = 1;
        }

        if (count == null) {
            count = true;
        }

        return new PageParams(pageSize, pageNum, count);
    }


}
