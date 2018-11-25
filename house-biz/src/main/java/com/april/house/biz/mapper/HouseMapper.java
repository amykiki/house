package com.april.house.biz.mapper;

import com.april.house.common.model.House;
import com.april.house.common.page.PageParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseMapper {
//    house表相关操作
    List<House> selectPageHouse(@Param("house") House house, @Param("pageParams") PageParams pageParams);
    Long selectPageCount(@Param("house") House query);
    int insert(House house);
    int updateHouseRating(House house);
    int downHouse(Long id);
    List<House> queryHouses(@Param("house") House house);
}
