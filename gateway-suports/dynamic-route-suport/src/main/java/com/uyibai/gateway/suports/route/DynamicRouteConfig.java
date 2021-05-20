package com.uyibai.gateway.suports.route;

import com.alibaba.cloud.nacos.NacosConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 动态路由 配置类
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(DynamicRouteProperties.class)
public class DynamicRouteConfig {

    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * Nacos实现动态路由管理方式
     */
    @Configuration
    @ConditionalOnProperty(name = "spring.cloud.gateway.dynamic-route.publish-type", havingValue = "nacos", matchIfMissing = false)
    @ConditionalOnBean(NacosConfigManager.class)
    public class NacosDynamicRouteConfig {
        @Bean
        public NacosRouteDefinitionRepository nacosRouteDefinitionRepository(NacosConfigManager nacosConfigManager, DynamicRouteProperties dynamicRouteProperties) {
            return new NacosRouteDefinitionRepository(publisher, nacosConfigManager, dynamicRouteProperties);
        }
    }

    /**
     * redis 实现动态路由自动配置
     */
    @Configuration
//    @ConditionalOnBean(RedisTemplate.class)
    @ConditionalOnProperty(name = "spring.cloud.gateway.dynamic-route.publish-type", havingValue = "redis", matchIfMissing = false)
    public class RedisDynamicRouteConfig {
        @Bean
        public RedisRouteDefinitionRepository redisRouteDefinitionRepository(StringRedisTemplate stringRedisTemplate, DynamicRouteProperties dynamicRouteProperties) {
            return new RedisRouteDefinitionRepository(stringRedisTemplate, dynamicRouteProperties);
        }
    }


}
