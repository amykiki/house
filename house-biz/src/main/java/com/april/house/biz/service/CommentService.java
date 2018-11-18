package com.april.house.biz.service;

import com.april.house.biz.mapper.CommentMapper;
import com.april.house.common.enums.CommentTypeEnum;
import com.april.house.common.model.Comment;
import com.april.house.common.util.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Value("${file.prefix}")
    private String imgPrefix;

    @Transactional(rollbackFor = Exception.class)
    public int addHouseComment(Long houseId, String content, Long userId) {
        Comment comment = new Comment();
        comment.setHouseId(houseId);
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setType(CommentTypeEnum.HOUSE_CMT.getCode());
        return addComment(comment);
    }


    @Transactional(rollbackFor = Exception.class)
    public int addBlogComment(Integer blogId, String content, Long userId) {
        Comment comment = new Comment();
        comment.setBlogId(blogId);
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setType(CommentTypeEnum.BLOG_CMT.getCode());
        return addComment(comment);
    }

    private int addComment(Comment comment) {
        BeanHelper.onInsert(comment);
        return commentMapper.insert(comment);
    }

    public List<Comment> getBlogComments(Integer blogId, int size) {
        List<Comment> comments = commentMapper.selectCommentsWithType(CommentTypeEnum.BLOG_CMT.getCode(), blogId, size);
        return comments;
    }

    public List<Comment> getHouseComments(Integer houseId, int size) {
        List<Comment> comments = commentMapper.selectCommentsWithType(CommentTypeEnum.HOUSE_CMT.getCode(), houseId, size);
        return comments;
    }

    private void setUserAvatar(List<Comment> list) {
        list.forEach(c -> c.setAvatar(imgPrefix + c.getAvatar()));
    }

}
