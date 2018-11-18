package com.april.house.test;

import com.april.house.common.model.House;
import com.april.house.common.model.JsonModel;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

/**
 * @Creation :  2018-11-16 17:22
 * --------  ---------  --------------------------
 */
public class RedisTest extends BaseTest{
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
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
        /*String key = "house";
        House house = new House();
        house.setId(2L);
        house.setImages("abc.image");
        house.setCreateTime(new Date());
        house.setImageList(Lists.newArrayList("aaa", "bbb", "ccc"));

        redisTemplate.opsForValue().set(key, house);
        House house1 = (House) redisTemplate.opsForValue().get(key);
        System.out.println("house1 = " + house1);*/

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kevin@126.com");
        message.setSubject("hello - 001");
        message.setTo("amy@gmail.com");
        message.setText("第一封邮件");

        redisTemplate.opsForValue().set("spmsg", message);
        SimpleMailMessage msg = (SimpleMailMessage) redisTemplate.opsForValue().get("spmsg");
        System.out.println(msg);
    }

    @Test
    public void testJsonModel() {
        JsonModel model = new JsonModel();
        model.setTo(new String[]{"amy@gmail.com"});

        redisTemplate.opsForValue().set("model", model);
        JsonModel model2 = (JsonModel) redisTemplate.opsForValue().get("model");
        System.out.println(1);
    }

}
