package com.april.house.biz.service;

import com.april.house.common.util.DateTimeUtil;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
public class ScheduleService {
    private static final Logger logger = LogManager.getLogger(ScheduleService.class);
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private MailService mailService;

    /**
     * 该方法是用于测试定时任务并发所用
     */

//    @Async(value = "schedulePoolExecutor")
//    @Scheduled(cron = "*/10 * * * * ?")
//    public void scheduleA() {
//        try {
//            TimeUnit.SECONDS.sleep(20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        logger.info("scheduleA每10秒运行一次********{}", DateTimeUtil.dateToString(new Date()));
//    }

    @Async(value = "schedulePoolExecutor")
    @Scheduled(cron = "0 0 12 * * ?")
    public void remRedisMails() {
        logger.info("Redis Rem每12小时运行一次********{}", DateTimeUtil.dateToString(new Date()));
        Set<String> keys = redisTemplate.keys("Email-To-*");
        if (!CollectionUtils.isEmpty(keys)) {
            keys.forEach(k -> {
                long removed = redisTemplate.opsForZSet().removeRangeByScore(k, 0, System.currentTimeMillis());
                logger.info("{} ******** Redis Remove mails {}", DateTimeUtil.dateToString(new Date()), removed);
            });
        }
    }

}
