package com.april.house.biz.service;

import com.april.house.common.model.RedisMailMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MailService {
    //开发环境不真实发邮件
    /*@Autowired
    private JavaMailSender mailSender;*/

    @Value("${spring.mail.username}")
    private String from;

    @Value(("${domain.name}"))
    private String domainName;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final double expire = 1000*60*10;

    //发送邮件
    @Async(value = "defaultThreadPool")
    public void sendMail(String title, String url, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(title);
        message.setTo(email);
        message.setText(url);
//        mailSender.send(message);
    }

    @Async(value = "defaultThreadPool")
    public void registerNotify(String email, String randomKey) {
        String url = "http://" + domainName + "/accounts/verify?key=" + randomKey;
        sendMail("房产平台激活邮件", url, email);
    }

    @Async(value = "defaultThreadPool")
    public void resetNotify(String email, String randomKey) {
        String url = "http://" + domainName + "/accounts/reset?key=" + randomKey;
        sendMail("房产平台密码重置邮件", url, email);
    }

    //使用redis代替真实的email，只供本地测试用
    public void sendRedisMail(String title, String text, String email) {
       RedisMailMsg message = new RedisMailMsg();
        message.setFrom(from);
        message.setTitle(title);
        message.setTo(email);
        message.setText(text);

        String key = getMailKey(email);
        redisTemplate.opsForZSet().add(key, message, System.currentTimeMillis() + expire);
    }

    public List<String> getRedisMails(String email) {
        String key = getMailKey(email);
        Set<ZSetOperations.TypedTuple<Object>> sets = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        List<String> resutls = sets.stream().map(s -> {
            RedisMailMsg m = (RedisMailMsg)s.getValue();
            return new BigDecimal(s.getScore()).toString() + ":" + m.getFrom() + "-" + m.getTitle() + "-" + m.getText();
        }).collect(Collectors.toList());
        return resutls;
    }

    private String getMailKey(String email) {
        return "Email-To-" + email;
    }
}
