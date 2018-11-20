package com.april.house.web.controller;

import com.april.house.biz.service.*;
import com.april.house.common.constants.CommonConstants;
import com.april.house.common.enums.HouseStateEnum;
import com.april.house.common.enums.HouseUserTypeEnum;
import com.april.house.common.model.*;
import com.april.house.common.page.PageParams;
import com.april.house.common.result.ResultMsg;
import com.april.house.common.util.UserContext;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Creation :  2018-11-19 16:52
 * --------  ---------  --------------------------
 */

@Controller ("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;
    @Autowired
    private CityService cityService;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private CommentService commentService;

    /**
     * 1. 实现分页
     * 2. 支持小区搜索，类型搜素
     * 3. 支持排序
     * 4. 支持展示图片，价格，标题，地址等信息
     * @param pageSize
     * @param pageNum
     * @param query
     * @param modelMap
     * @return
     */
    @RequestMapping("/list")
    public String houseList(Integer pageSize, Integer pageNum, House query, ModelMap modelMap) {
        PageInfo<House> ps = houseService.queryHouseByPage(query, PageParams.build(pageSize, pageNum));
        List<House> hotHouses = recommendService.getHotHouses(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", hotHouses);
        modelMap.put("ps", ps);
        modelMap.put("vo", query);
        return "house/listing";
    }

    /**
     * 跳转到添加房产页面
     * @param modelMap
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(ModelMap modelMap) {
        modelMap.put("citys", cityService.getAllCities());
        modelMap.put("communitys", houseService.getAllCommunities());
        return "/house/add";
    }

    /**
     * 添加房产
     * @param house
     * @return
     */
    @RequestMapping("/add")
    public String doAdd(House house) {
        User user = UserContext.getUser();
        house.setState(HouseStateEnum.FOR_SALE.getCode());
        houseService.addHouse(house, user);
        return CommonConstants.REDIRECT + "/house/ownlist";
    }

    @RequestMapping("/ownlist")
    public String ownlist(House house, Integer pageNum, Integer pageSize, ModelMap modelMap) {
        User user = UserContext.getUser();
        house.setUserId(user.getId());
        house.setBookmarked(false);
        modelMap.put("ps", houseService.queryHouseByPage(house, PageParams.build(pageSize, pageNum)));
        modelMap.put("pageType", "own");
        return "/house/ownlist";
    }

    /**
     * 查询房屋详情
     * 查询关联经纪人
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping("/detail")
    public String houseDetail(Long id, ModelMap modelMap) {
        House house = houseService.queryOneHouse(id);
        HouseUser houseUser = houseService.getSaleHouseUser(id);
        recommendService.increase(id);
        List<Comment> comments = commentService.getHouseComments(id, 8);
        if (houseUser.getUserId() != null && !houseUser.getUserId().equals(0)) {
            modelMap.put("agent", agencyService.getAgentDetail(houseUser.getUserId()));
        }
        List<House> rcHouses = recommendService.getHotHouses(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", rcHouses);
        modelMap.put("house", house);
        modelMap.put("commentList", comments);
        return "/house/detail";
    }

    //在detail页面操作
    @RequestMapping("/leaveMsg")
    public String houseMsg(UserMsg userMsg) {
        houseService.addUserMsg(userMsg);
        return CommonConstants.REDIRECT + "/house/detail?id=" + userMsg.getHouseId() + ResultMsg.successMsg("留言成功").asUrlParams();
    }


    //在detail页面操作
    //1. 评分
    @ResponseBody
    @RequestMapping("/rating")
    public ResultMsg houseRate(Double rating, Long id) {
        houseService.updateRating(id, rating);
        return ResultMsg.successMsg("ok");
    }

    //在detail页面操作
    //2. 收藏
    @ResponseBody
    @RequestMapping("/bookmark")
    public ResultMsg bookmark(Long id) {
        User user = UserContext.getUser();
        houseService.bindUser2House(id, user.getId(), true);
        return ResultMsg.successMsg("ok");
    }

    //在detail页面操作
    //3. 删除收藏
    @ResponseBody
    @RequestMapping("/unbookmark")
    public ResultMsg unbookMark(Long id) {
        User user = UserContext.getUser();
        houseService.unbindUser2House(id, user.getId(), HouseUserTypeEnum.BOOKMARK);
        return ResultMsg.successMsg("ok");
    }

    //4. 收藏列表
    @RequestMapping("/bookmarked")
    public String bookmarked(House house, Integer pageNum, Integer pageSize, ModelMap modelMap) {
        User user = UserContext.getUser();
        house.setBookmarked(true);
        house.setUserId(user.getId());
        modelMap.put("ps", houseService.queryHouseByPage(house, PageParams.build(pageSize, pageNum)));
        modelMap.put("pageType", "own");
        return "/house/ownlist";
    }

    @RequestMapping("/del")
    public String delsale(Long id, String pageType) {
        User user = UserContext.getUser();
        houseService.unbindUser2House(id, user.getId(), pageType.equalsIgnoreCase("own") ? HouseUserTypeEnum.SOLD :HouseUserTypeEnum.BOOKMARK);
        return CommonConstants.REDIRECT + "/house/ownlist";
    }

}
