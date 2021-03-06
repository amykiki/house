package com.april.house.biz.mapper;

import com.april.house.common.model.Comment;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CommentMapper extends Mapper<Comment> {
//    List<Comment> selectHouseComments(@Param("houseId") Long houseId, @Param("size") int size);

    List<Comment> selectCommentsWithType(@Param("type") Integer type, @Param("typeId") Long typeId, @Param("size") int size);


}