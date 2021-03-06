package com.april.house.biz.service;

import com.april.house.biz.mapper.BlogMapper;
import com.april.house.common.model.Blog;
import com.april.house.common.page.PageParams;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;

    public int insert(Blog blog) {
        return blogMapper.insert(blog);
    }

    public List<Blog> queryBlogs(Blog blog) {
        Example example = selectField();
        Example.Criteria criteria = example.createCriteria();
        if (blog.getId() != null && blog.getId() != 0) {
            criteria.andEqualTo("id", blog.getId());
        }

        if (blog.getCat() != null && blog.getCat() != 0) {
            criteria.andEqualTo("cat", blog.getCat());
        }

        if (StringUtils.isNotBlank(blog.getTitle())) {
            criteria.andLike("title", "%" + blog.getTitle() + "%");
        }

        if (StringUtils.isNotBlank(blog.getTags())) {
            criteria.andCondition("find_in_set('" + blog.getTags() + "', tags)");
        }

        if (blog.getCreateTime() != null) {
            criteria.andLessThanOrEqualTo("createTime", blog.getCreateTime());
        }
        List<Blog> blogs = queryBlogs(example);
        return blogs;
    }
    public PageInfo<Blog> queryPageBlog(Blog blog, PageParams pageParams) {
        PageHelper.startPage(pageParams.getPageNum(), pageParams.getPageSize());
        List<Blog> blogs = queryBlogs(blog);
        return new PageInfo<Blog>(blogs);
    }

    public Blog queryOneBlog(int id) {
        Example example = selectField();
        example.createCriteria().andEqualTo("id", id);
        List<Blog> blogs = queryBlogs(example);
        if (!blogs.isEmpty()) {
            return blogs.get(0);
        }
        return null;
    }

    private Example selectField() {
        Example example = new Example(Blog.class);
        example.selectProperties("id", "tags", "content", "title", "createTime");
        return example;
    }
    private List<Blog> queryBlogs(Example example) {
        example.orderBy("createTime").desc();
        List<Blog> blogs = blogMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(blogs)) {
            blogs.forEach(blog -> {
                String stripped = Jsoup.parse(blog.getContent()).text();
                blog.setDigest(stripped.substring(0, Math.min(stripped.length(), 40)));
                String tags = blog.getTags();
                blog.getTagList().addAll(Lists.newArrayList(Splitter.on(",").split(tags)));
            });
        }
        return blogs;
    }
}
