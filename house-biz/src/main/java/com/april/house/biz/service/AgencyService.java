package com.april.house.biz.service;

import com.april.house.biz.mapper.AgencyMapper;
import com.april.house.biz.mapper.UserMapper;
import com.april.house.common.model.Agency;
import com.april.house.common.model.User;
import com.april.house.common.page.PageData;
import com.april.house.common.page.PageParams;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyService {
    @Autowired
    private AgencyMapper agencyMapper;
    @Autowired
    private UserMapper userMapper;

    public List<Agency> getAllAgency() {
        return getAllAgency(new Agency());
    }

    public List<Agency> getAllAgency(Agency agency) {
        return agencyMapper.selectAgency(agency);
    }

    public PageInfo<Agency> getPageAgency(Agency agency) {
        PageHelper.startPage(3, 2, "name asc");
        List<Agency> agencies = getAllAgency(agency);
        PageInfo<Agency> pageInfo = new PageInfo<>(agencies);
        return pageInfo;
    }

    /**
     * 经纪人分页
     * @return
     */
    public PageInfo<User> getPageAgent(int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);

        return null;
    }
}
