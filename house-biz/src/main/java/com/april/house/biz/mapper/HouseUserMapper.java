package com.april.house.biz.mapper;

import com.april.house.common.model.HouseUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public interface HouseUserMapper {
    int insertHouseUser(HouseUser houseUser);
    int deleteHouseUser(@Param("houseId") Long houseId, @Param("userId") Long userId, @Param("type") Integer type);
    HouseUser selectHouseUser(@Param("userId") Long userId, @Param("houseId") Long houseId, @Param("type") Integer type);
    HouseUser selectSaleHouseUser(Long houseId);
    Integer selectBookmarkHouseUser(@Param("houseId") Long houseId, @Param("userId") Long userId);

}
