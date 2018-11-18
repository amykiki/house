package com.april.house.biz.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Creation :  2018-11-16 16:03
 * --------  ---------  --------------------------
 */
@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private String maxWaitTime;
    @Value("${spring.redis.timeout}")
    private String timeout;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        //最大连接连接数
        jedisPoolConfig.setMaxTotal(maxActive);
        int maxWait = Integer.parseInt(maxWaitTime.substring(0, maxWaitTime.length() - 2));
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        //注意，使用pool后，设置timeout就没有意义了
        return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setDatabase(database);
        configuration.setPort(port);
        if (StringUtils.isNotBlank(password))
            configuration.setPassword(RedisPassword.of(password));

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpb = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        jpb.poolConfig(jedisPoolConfig);
        JedisConnectionFactory factory = new JedisConnectionFactory(configuration, jpb.build());
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        //使用Jackson2JsonRedisResiraliazer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer valueSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        valueSerializer.setObjectMapper(mapper);

        //使用StringRedisSerializer来序列化和反序列化redis的key端
        RedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(valueSerializer);

        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }

    //不用手动设置，boot提供了默认的StringRedisTemplate
    /*@Bean
    public RedisTemplate<String, String> StringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringTemplate = new StringRedisTemplate();
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        stringTemplate.setKeySerializer(stringSerializer);
        stringTemplate.setValueSerializer(stringSerializer);
        stringTemplate.setHashKeySerializer(stringSerializer);
        stringTemplate.setHashValueSerializer(stringSerializer);
        stringTemplate.setConnectionFactory(redisConnectionFactory);
        stringTemplate.afterPropertiesSet();
        return stringTemplate;
    }*/


}
