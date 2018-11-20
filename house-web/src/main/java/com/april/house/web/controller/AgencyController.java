package com.april.house.web.controller;

import com.april.house.biz.service.AgencyService;
import com.april.house.biz.service.HouseService;
import com.april.house.biz.service.MailService;
import com.april.house.biz.service.RecommendService;
import com.april.house.common.constants.CommonConstants;
import com.april.house.common.model.Agency;
import com.april.house.common.model.House;
import com.april.house.common.model.User;
import com.april.house.common.page.PageParams;
import com.april.house.common.result.ResultMsg;
import com.april.house.common.util.UserContext;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

/**
 * @Creation :  2018-11-20 11:30
 * --------  ---------  --------------------------
 */
@Controller("/agency")
public class AgencyController {
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private MailService mailService;

    @RequestMapping("/create")
    public String agencyCreate() {
        User user = UserContext.getUser();
        //判断权限
        if (user == null || !Objects.equals(user.getEmail(), "admin@gmail.com")) {
            return CommonConstants.REDIRECT + "/accounts/signin?" + ResultMsg.successMsg("请先登录，并确认有权增加机构").asUrlParams();
        }
        return "/user/agency/create";
    }

    @RequestMapping("/submit")
    public String agencySubmit(Agency agency) {
        User user = UserContext.getUser();
        //判断权限
        if (user == null || !Objects.equals(user.getEmail(), "admin@gmail.com")) {
            return CommonConstants.REDIRECT + "/accounts/signin?" + ResultMsg.successMsg("请先登录，并确认有权增加机构").asUrlParams();
        }
        agencyService.add(agency);
        return CommonConstants.REDIRECT + "/index?" + ResultMsg.successMsg("创建成功").asUrlParams();
    }


    @RequestMapping("/agentList")
    public String agentList(Integer pageSize, Integer pageNum, ModelMap modelMap) {
        if (pageSize == null) {
            pageSize = 6;
        }

        PageInfo<User> ps = agencyService.getAllAgent(PageParams.build(pageSize, pageNum));
        modelMap.put("ps", ps);
        getAndSetHotHouses(modelMap);
        return "/user/agent/agentList";
    }

    @RequestMapping("/agentDetail")
    public String agentDetail(Long id, ModelMap modelMap) {
        User user = agencyService.getAgentDetail(id);
        House query = new House();
        query.setUserId(id);
        query.setBookmarked(false);
        List<House> bindHouses = houseService.queryHouse(query, PageParams.build(3, 1));
        if (bindHouses != null) {
            modelMap.put("bindHouses", bindHouses);
        }
        modelMap.put("agent", user);
        modelMap.put("agencyName", user.getAgencyName());
        getAndSetHotHouses(modelMap);
        return "/user/agent/agentDetail";

    }

    @RequestMapping("/agentMsg")
    public String agentMsg(Long id, String msg, String name, String email, ModelMap modelMap) {
        User agent = agencyService.getAgentDetail(id);
        mailService.sendRedisMail("咨询", "name: " + name + ", email:" + email + ", msg" + msg, agent.getEmail());
        return CommonConstants.REDIRECT + "/agency/agentDetail?id=" + id + "&" + ResultMsg.successMsg("留言成功").asUrlParams();
    }

    @RequestMapping("/agencyDetail")
    public String agencyDetail(Integer id, ModelMap modelMap) {
        Agency agency = agencyService.getAgency(id);
        modelMap.put("agency", agency);
        getAndSetHotHouses(modelMap);
        return "/user/agency/agencyDetail";
    }

    @RequestMapping("/list")
    public String agencyList(ModelMap modelMap) {
        List<Agency> agencies = agencyService.getAllAgency();
        modelMap.put("agencyList", agencies);
        getAndSetHotHouses(modelMap);
        return "/user/agency/agencyList";
    }

    private void getAndSetHotHouses(ModelMap modelMap) {
        List<House> houses = recommendService.getHotHouses(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);
    }

}
