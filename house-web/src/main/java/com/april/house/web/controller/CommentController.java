package com.april.house.web.controller;

import com.april.house.biz.service.CommentService;
import com.april.house.common.constants.CommonConstants;
import com.april.house.common.model.User;
import com.april.house.common.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Creation :  2018-11-20 15:01
 * --------  ---------  --------------------------
 */

@Controller("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping("/leaveComment")
    public String leaveComment(String content, Long houseId, ModelMap modelMap) {
        User user = UserContext.getUser();
        Long userId = user.getId();
        commentService.addHouseComment(houseId, content, userId);
        return CommonConstants.REDIRECT + "/house/detail?id=" + houseId;
    }

    @RequestMapping("/leaveBlogComment")
    public String leaveBlogComment(String content, Integer blogId, ModelMap modelMap) {
        User user = UserContext.getUser();
        Long userId = user.getId();
        commentService.addBlogComment(blogId, content, userId);
        return CommonConstants.REDIRECT + "/blog/detail?id=" + blogId;
    }
}
