package com.april.house.biz.service;

import com.april.house.biz.mapper.CommunityMapper;
import com.april.house.biz.mapper.HouseMapper;
import com.april.house.biz.mapper.HouseMsgMapper;
import com.april.house.biz.mapper.HouseUserMapper;
import com.april.house.common.enums.HouseUserTypeEnum;
import com.april.house.common.model.*;
import com.april.house.common.page.PageParams;
import com.april.house.common.util.BeanHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseService {
    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private HouseUserMapper houseUserMapper;
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private HouseMsgMapper houseMsgMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private MailService mailService;
    @Autowired
    private AgencyService agencyService;


    @Value("${file.prefix}")
    private String imgPrefix;


    /**
     * 根据条件查询房产信息
     * @param query
     * @return
     */
    public List<House> queryHouses(House query) {
        List<House> houses = houseMapper.queryHouses(query);
        houses.forEach(h -> {
            h.setFirstImg(imgPrefix + h.getFirstImg());
            h.setImageList(h.getImageList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
            h.setFloorPlanList(h.getFloorPlanList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
        });
        return houses;
    }

    /**
     * 根据条件查询房产信息并分页
     * @param query
     * @param pageParams
     * @return
     */
    public PageInfo<House> queryHouseByPage(House query, PageParams pageParams) {
        PageHelper.startPage(pageParams.getPageNum(), pageParams.getPageSize());
        if (StringUtils.isNotBlank(pageParams.getOrderBy())) {
            PageHelper.orderBy(pageParams.getOrderBy());
        }
        List<House> queryHouses = queryHouses(query);
        PageInfo<House> pageInfo = new PageInfo<>(queryHouses);
        return pageInfo;

    }

    /**
     * 仅返回查询list。不返回pageInfo消息
     * 常用于不进行count的查询。
     * @param query
     * @param pageParams
     * @return
     */
    public List<House> queryHouse(House query, PageParams pageParams) {
        PageHelper.startPage(pageParams.getPageNum(), pageParams.getPageSize(), pageParams.getCount());
        if (StringUtils.isNotBlank(pageParams.getOrderBy())) {
            PageHelper.orderBy(pageParams.getOrderBy());
        }
        List<House> queryHouses = queryHouses(query);
        return queryHouses;
    }

    public House queryOneHouse(Long id) {
        House query = new House();
        query.setId(id);
        List<House> houses = queryHouses(query);
        if (!houses.isEmpty()) {
            return houses.get(0);
        }

        return null;
    }


    /**
     * 添加房屋图片
     * 添加户型图片
     * 插入房产信息
     * 绑定用户到房产的关系
     * @param house
     * @param user
     */
    @Transactional
    public void addHouse(House house, User user) {
        if (CollectionUtils.isNotEmpty(house.getHouseFiles())) {
            String images = Joiner.on(",").join(fileService.getImgPaths(house.getHouseFiles()));
            house.setImages(images);
        }

        if (CollectionUtils.isNotEmpty(house.getFloorPlanFiles())) {
            String images = Joiner.on(",").join(fileService.getImgPaths(house.getFloorPlanFiles()));
            house.setFloorPlan(images);
        }

        BeanHelper.onInsert(house);
        houseMapper.insert(house);
        //绑定用户和房产的信息
        bindUser2House(house.getId(), user.getId(), false);
    }

    /**
     * 更新房产评分
     * @param id
     * @param rating
     */
    @Transactional
    public void updateRating(Long id, Double rating) {
        House house = queryOneHouse(id);
        Double oldRating = house.getRating();
        Long ratingNums = house.getRatingNums();
        Double newRating = oldRating.equals(0D) ? rating : Math.min((oldRating * ratingNums + rating) / (ratingNums + 1), 5);
        House updateHouse = new House();
        updateHouse.setRating(newRating);
        updateHouse.setRatingNums(ratingNums + 1);
        updateHouse.setId(id);
        BeanHelper.onUpdate(updateHouse);
        houseMapper.updateHouseRating(updateHouse);
    }

    /**绑定用户与房产
     * @param houseId
     * @param userId
     * @param collect
     */
    @Transactional
    public void bindUser2House(Long houseId, Long userId, boolean collect) {
        HouseUser existHouseUser = houseUserMapper.selectHouseUser(userId, houseId, collect ? HouseUserTypeEnum.BOOKMARK.getCode() : HouseUserTypeEnum.SOLD.getCode());
        if (existHouseUser != null) {
            return;
        }

        HouseUser houseUser = new HouseUser();
        houseUser.setHouseId(houseId);
        houseUser.setUserId(userId);
        houseUser.setType(collect ? HouseUserTypeEnum.BOOKMARK.getCode() : HouseUserTypeEnum.SOLD.getCode());
        BeanHelper.onInsert(houseUser);
        houseUserMapper.insertHouseUser(houseUser);
    }

    /**
     * 解绑用户与房产
     * @param id
     * @param userId
     * @param type
     */
    @Transactional
    public void unbindUser2House(Long id, Long userId, HouseUserTypeEnum type) {
        if (HouseUserTypeEnum.SOLD.equals(type)) {
            houseMapper.downHouse(id);
        } else {
            houseUserMapper.deleteHouseUser(id, userId, type.getCode());
        }

    }

    public HouseUser getSaleHouseUser(Long houseId) {
        HouseUser houseUser = houseUserMapper.selectSaleHouseUser(houseId);
        return houseUser;
    }

    /**
     * 查询所有社区信息
     * @return
     */
    public List<Community> getAllCommunities() {
        Community community = new Community();
        return communityMapper.selectCommunity(community);
    }


    @Transactional
    public void addUserMsg(UserMsg userMsg) {
        BeanHelper.onInsert(userMsg);
        houseMsgMapper.insertUserMsg(userMsg);
        User agent = agencyService.getAgentDetail(userMsg.getAgentId());
        mailService.sendRedisMail("来自用户" + userMsg.getEmail() + "留言", userMsg.getMsg(), agent.getEmail());
    }






}
