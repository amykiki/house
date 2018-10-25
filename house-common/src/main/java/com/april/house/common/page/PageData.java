package com.april.house.common.page;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Creation Date:  2018-10-25 18:19
 * --------  ---------  --------------------------
 */
@Getter
@Setter
public class PageData<T> {
    private List<T> list;
    private Pagination pagination;

    public PageData(List<T> list, Pagination pagination) {
        this.list = list;
        this.pagination = pagination;
    }

    public static <U> PageData<U> buildPage(List<U> list, Long count, Integer pageSize, Integer pageNum) {
        Pagination _pagination = new Pagination(pageNum, pageSize, count);
        return new PageData<>(list, _pagination);
    }
}
