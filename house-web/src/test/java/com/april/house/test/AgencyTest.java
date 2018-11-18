package com.april.house.test;

import com.april.house.biz.service.AgencyService;
import com.april.house.common.model.Agency;
import com.april.house.common.model.User;
import com.april.house.common.page.PageParams;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AgencyTest extends BaseTest {
    @Autowired
    private AgencyService agencyService;

    @Test
    public void testGetAllAgency() {
        Agency agency = new Agency();
        agency.setEmail("010@gmail.com");
        List<Agency> agencies = agencyService.getAllAgency(agency);
        System.out.println(agencies.get(0));

    }

    @Test
    public void testgetPageAgency() {
        Agency agency = new Agency();
        PageParams pageParams = PageParams.build(3, 2);
        PageInfo<Agency> pageInfo = agencyService.getPageAgency(agency, pageParams);
        System.out.println(pageInfo.getTotal());
    }

    @Test
    public void getAgentDetailTest() {
        Long agentId = 19L;
        User agent = agencyService.getAgentDetail(agentId);
        System.out.println("agent = " + agent);
    }
}
