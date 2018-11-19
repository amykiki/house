package com.april.house.test;

import com.april.house.common.model.JsonModel;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.mail.SimpleMailMessage;

import java.util.Set;

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

    @Test
    public void testCmds() {
        String key = "hots";
//        redisTemplate.opsForZSet().incrementScore("hots", 123L, 1D);
        redisTemplate.opsForZSet().removeRange(key, 0, -1L);
        redisTemplate.opsForZSet().add(key, "a", 1D);
        redisTemplate.opsForZSet().add(key, "b", 2D);
        redisTemplate.opsForZSet().add(key, "c", 3D);
        redisTemplate.opsForZSet().add(key, "d", 4D);
        redisTemplate.opsForZSet().add(key, "e", 5D);
        redisTemplate.opsForZSet().add(key, "f", 6D);
        printZsets(key);
        redisTemplate.opsForZSet().incrementScore(key, "c", 2D);
        printZsets(key);
        redisTemplate.opsForZSet().removeRange(key, 0L, -4L);
        printZsets(key);
    }

    private void printZsets(String key) {
        Set<ZSetOperations.TypedTuple<Object>> sets = redisTemplate.opsForZSet().rangeWithScores(key, 0L, -1L);
        System.out.println("\n======Begin=======");
        sets.forEach(s -> {
            Double score = s.getScore();
            String value = s.getValue().toString();
            System.out.printf("score: %f, value: %s\n", score, value);
        });
        System.out.println("======End=======");
    }

}
