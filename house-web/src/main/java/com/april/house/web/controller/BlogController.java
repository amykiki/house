package com.april.house.web.controller;

import com.april.house.biz.service.BlogService;
import com.april.house.biz.service.CommentService;
import com.april.house.biz.service.RecommendService;
import com.april.house.common.constants.CommonConstants;
import com.april.house.common.model.Blog;
import com.april.house.common.model.Comment;
import com.april.house.common.model.House;
import com.april.house.common.page.PageParams;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Creation :  2018-11-20 12:22
 * --------  ---------  --------------------------
 */
@Controller("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RecommendService recommendService;


    @RequestMapping(value = "/list")
    public String list(Integer pageSize, Integer pageNum, Blog query, ModelMap modelMap) {
        PageInfo<Blog> ps = blogService.queryPageBlog(query, PageParams.build(pageSize, pageNum));
        modelMap.put("ps", ps);
        getAndSetHotHouses(modelMap);
        return "/blog/listing";
    }


    @RequestMapping("/detail")
    public String blogDetail(int id, ModelMap modelMap) {
        Blog blog = blogService.queryOneBlog(id);
        List<Comment> comments = commentService.getBlogComments(id, 8);
        modelMap.put("blog", blog);
        modelMap.put("commentList", comments);
        getAndSetHotHouses(modelMap);
        return "/blog/detail";
    }
    private void getAndSetHotHouses(ModelMap modelMap) {
        List<House> houses = recommendService.getHotHouses(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);
    }

}
