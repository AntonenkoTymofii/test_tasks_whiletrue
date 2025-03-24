package org.example.test_tasks_whiletrue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {

    @Bean
    public Jedis jedisClient() {
        return new Jedis("localhost", 6379);
    }
}
