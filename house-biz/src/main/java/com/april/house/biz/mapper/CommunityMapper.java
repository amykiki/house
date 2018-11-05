package com.april.house.biz.mapper;

import com.april.house.common.model.Community;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface CommunityMapper {
    List<Community> selectCommunity(Community community);
}
