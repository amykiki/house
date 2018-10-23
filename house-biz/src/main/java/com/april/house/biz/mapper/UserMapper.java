package com.april.house.biz.mapper;

import com.april.house.common.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void insert(User account);

    int deleteByEmail(String email);

    List<User> selectUsersByQuery(User user);

    int updateByEmail(User updateUser);
}
