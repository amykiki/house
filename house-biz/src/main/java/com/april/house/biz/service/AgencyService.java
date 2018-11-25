package com.april.house.biz.service;

import com.april.house.biz.mapper.AgencyMapper;
import com.april.house.biz.mapper.UserMapper;
import com.april.house.common.enums.UserTypeEnum;
import com.april.house.common.model.Agency;
import com.april.house.common.model.User;
import com.april.house.common.page.PageParams;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyService {
    @Autowired
    private AgencyMapper agencyMapper;
    @Autowired
    private UserMapper userMapper;
    @Value("${file.prefix}")
    private String imgPrefix;

    public List<Agency> getAllAgency() {
        return getAllAgency(new Agency());
    }

    public List<Agency> getAllAgency(Agency agency) {
        return agencyMapper.selectAgency(agency);
    }

    public PageInfo<Agency> getPageAgency(Agency agency, PageParams pageParams) {
        PageHelper.startPage(pageParams.getPageNum(), pageParams.getPageSize(), "name asc");
        List<Agency> agencies = getAllAgency(agency);
        PageInfo<Agency> pageInfo = new PageInfo<>(agencies);
        return pageInfo;
    }

    public Agency getAgency(Integer id) {
        Agency agency = new Agency();
        agency.setId(id);
        List<Agency> agencies = getAllAgency(agency);
        if (CollectionUtils.isNotEmpty(agencies)) {
            return agencies.get(0);
        }
        return null;
    }

    public int add(Agency agency) {
        return agencyMapper.insert(agency);
    }

    /**
     * 经纪人分页
     * @return
     */
    public PageInfo<User> getAllAgent(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPageNum(), pageParams.getPageSize());
        User query = new User();
        query.setType(UserTypeEnum.AGENT.getCode());
        List<User> users = userMapper.selectUsersByQuery(query);
        setAgentImage(users);
        return new PageInfo<>(users);
    }

    public User getAgentDetail(Long agentId) {
        User agent = userMapper.selectAgentDetail(agentId);
        if (agent != null) {
            agent.setAvatar(imgPrefix + agent.getAvatar());
        }
        return agent;
    }

    private void setAgentImage(List<User> list) {
        list.forEach(a -> a.setAvatar(imgPrefix + a.getAvatar()));
    }
}
