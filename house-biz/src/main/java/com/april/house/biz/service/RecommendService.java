package com.april.house.biz.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RecommendService {
    private static final String HOT_HOUSE_KEY = "hot_house";
    private static final Logger logger = LogManager.getLogger(RecommendService.class);
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private HouseService houseService;

    public void increase(Long id) {

    }
}
