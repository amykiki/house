package com.april.house.test;

import com.april.house.biz.service.AgencyService;
import com.april.house.common.model.Agency;
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
        PageInfo<Agency> pageInfo = agencyService.getPageAgency(agency);
        System.out.println(pageInfo.getTotal());
    }
}
