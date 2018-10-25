package com.april.house.common.page;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description : 分页数据，包含总的查询数量
 * @Creation Date:  2018-10-25 18:14
 * --------  ---------  --------------------------
 */
@Getter
@Setter
public class Pagination {
    private Integer pageNum;
    private Integer pageSize;
    private Long totalCount;
    private List<Integer> pages = Lists.newArrayList();

    public Pagination(Integer pageNum, Integer pageSize, Long totalCount) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        long pageCount = totalCount/pageSize + ((totalCount % pageSize == 0) ? 0 : 1);

        for (int i = 1; i <= pageCount; i++) {
            pages.add(i);
        }
    }
}
