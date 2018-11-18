package com.april.house.biz.mapper;

import com.april.house.common.model.User;
import com.april.house.common.page.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    void insert(User account);

    int deleteByEmail(String email);

    List<User> selectUsersByQuery(User user);

    int updateByEmail(User updateUser);

    int updatePassword(User updateUser);

    List<User> selectAgent(@Param("user") User user);

    User selectAgentDetail(Long agentId);



}
