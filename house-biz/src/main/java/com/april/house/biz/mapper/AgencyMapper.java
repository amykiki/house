package com.april.house.biz.mapper;

import com.april.house.common.model.Agency;

import java.util.List;

public interface AgencyMapper {
    //查询经济机构
    List<Agency> selectAgency(Agency agency);
    int insert(Agency agency);
}
