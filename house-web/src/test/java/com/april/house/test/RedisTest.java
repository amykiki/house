package com.april.house.test;

import com.april.house.common.model.House;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;

/**
 * @Creation :  2018-11-16 17:22
 * --------  ---------  --------------------------
 */
public class RedisTest extends BaseTest{
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void RedisTestForString() {
        stringRedisTemplate.opsForValue().set("email", "abc@gmail.com");
        String email = stringRedisTemplate.opsForValue().get("email");
        System.out.println("email = " + email);
    }

    @Test
    public void RedisTestForObject() {
        String key = "house";
        House house = new House();
        house.setId(2L);
        house.setImages("abc.image");
        house.setCreateTime(new Date());
        house.setImageList(Lists.newArrayList("aaa", "bbb", "ccc"));

        redisTemplate.opsForValue().set(key, house);
        House house1 = (House) redisTemplate.opsForValue().get(key);
        System.out.println("house1 = " + house1);
    }

}
