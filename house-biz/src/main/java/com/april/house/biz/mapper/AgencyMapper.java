package com.april.house.biz.mapper;

import com.april.house.common.model.Agency;
import com.april.house.common.model.User;
import com.april.house.common.page.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgencyMapper {
    //查询经济机构
    List<Agency> selectAgency(Agency agency);
    int insert(Agency agency);
}
