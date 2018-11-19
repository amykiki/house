package com.april.house.biz.service;

import com.april.house.common.model.House;
import com.april.house.common.page.PageParams;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendService {
    private static final String HOT_HOUSE_KEY = "hot_house";
    private static final Logger logger = LogManager.getLogger(RecommendService.class);
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private HouseService houseService;

    public void increase(Long id) {
        redisTemplate.opsForZSet().incrementScore(HOT_HOUSE_KEY, id, 1.0D);
        redisTemplate.opsForZSet().removeRange(HOT_HOUSE_KEY, 0, -11);
    }

    public List<Long> getHot() {
        Set<Object> idSets = redisTemplate.opsForZSet().reverseRange(HOT_HOUSE_KEY, 0L, -1L);
        List<Long> ids = idSets.stream().map(String::valueOf).map(Long::valueOf).collect(Collectors.toList());
        return ids;
    }

    public List<House> getHotHouses(Integer size) {
        House query = new House();
        List<Long> list = getHot();
        list = list.subList(0, Math.min(list.size(), size));
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }

        query.setIds(list);
        //最火的房子的id集合
        List<Long> order = list;
        List<House> houses = houseService.queryHouses(query);
        Ordering<House> houseOrdering = Ordering.natural().onResultOf(hs -> order.indexOf(hs.getId()));
        return houseOrdering.sortedCopy(houses);

    }

    public List<House> getLastest() {
        House query = new House();
        PageParams pageParams = PageParams.build(8, 1, false);
        pageParams.setOrderBy("create_time");
        List<House> houses = houseService.queryHouse(query, pageParams);
        return houses;
    }
}
