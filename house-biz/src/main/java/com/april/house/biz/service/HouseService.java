package com.april.house.biz.service;

import com.april.house.biz.mapper.HouseMapper;
import com.april.house.biz.mapper.HouseUserMapper;
import com.april.house.common.enums.HouseUserTypeEnum;
import com.april.house.common.model.House;
import com.april.house.common.model.HouseUser;
import com.april.house.common.model.User;
import com.april.house.common.page.PageParams;
import com.april.house.common.util.BeanHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public class HouseService {
    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private HouseUserMapper houseUserMapper;
    @Autowired
    private FileService fileService;


    @Value("${file.prefix}")
    private String imgPrefix;


    public List<House> queryHouses(House query) {
        List<House> houses = houseMapper.queryHouses(query);
        houses.forEach(h -> {
            h.setFirstImg(imgPrefix + h.getFirstImg());
            h.setImageList(h.getImageList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
            h.setFloorPlanList(h.getFloorPlanList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
        });
        return houses;
    }

    // TODO: 2018/11/10  
    public PageInfo<House> queryHouseByPage(House query, PageParams pageParams) {

        PageHelper.startPage(pageParams.getPageNum(), pageParams.getPageSize());
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

    public HouseUser getSaleHouseUser(Long houseId) {
        HouseUser houseUser = houseUserMapper.selectSaleHouseUser(houseId);
        return houseUser;
    }


}
