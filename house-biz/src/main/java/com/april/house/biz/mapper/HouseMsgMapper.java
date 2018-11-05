package com.april.house.biz.mapper;

import com.april.house.common.model.UserMsg;
import org.apache.ibatis.annotations.Mapper;

public interface HouseMsgMapper {
    int insertUserMsg(UserMsg userMsg);
}
