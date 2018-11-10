package com.april.house.test;

import com.april.house.biz.service.CommentService;
import com.april.house.common.model.Comment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CommentTest extends BaseTest {
    @Autowired
    private CommentService commentService;

    @Test
    public void testAddBlogComment() {
//        Comment comment = new Comment();
//        comment.setBlogId(2);
//        comment.setUserId(18L);
//        comment.setContent("信息很详细，不错");
        int result = commentService.addBlogComment(2, "信息很详细，不错", 18L);
        System.out.println("result = " + result);
    }

    @Test
    public void testGetBlogComments() {
        List<Comment> comments = commentService.getBlogComments(2, 10);
        System.out.println("comments.size() = " + comments.size());
    }
}
