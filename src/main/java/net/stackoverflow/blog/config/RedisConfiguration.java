package net.stackoverflow.blog.config;

import net.stackoverflow.blog.util.RedisCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

/**
 * Redis配置类
 *
 * @author 凉衫薄
 */
@Configuration
public class RedisConfiguration {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置序列化器
     */
    @PostConstruct
    public void initRedisTemplate() {
        RedisSerializer stringRedisSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        RedisCacheUtils.setRedisTemplate(redisTemplate);
    }
}
