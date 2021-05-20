package com.uyibai.gateway.suports.route;

import com.uyibai.gateway.common.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基于 redis 的动态路由管理
 */
@Slf4j
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {

    private static final String REDIS_VALUE_TYPE_HASH = "hash";

    private static final String REDIS_VALUE_TYPE_ZSET = "zset";

    /**
     * redis template
     */
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 动态路由配置
     */
    private final DynamicRouteProperties dynamicRouteProperties;

    public RedisRouteDefinitionRepository(StringRedisTemplate stringRedisTemplate, DynamicRouteProperties dynamicRouteProperties) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.dynamicRouteProperties = dynamicRouteProperties;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        log.info("RedisRouteDefinitionRepository.getRouteDefinitions");
        List<RouteDefinition> routeDefinitions = getListFromRedis();
        return Flux.fromIterable(routeDefinitions);
    }

    private List<RouteDefinition> getListFromRedis() {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        // hash 方式 存储
        if (REDIS_VALUE_TYPE_HASH.equals(dynamicRouteProperties.getRedis().getType())) {
            BoundHashOperations<String, String, String> operations = stringRedisTemplate.boundHashOps(dynamicRouteProperties.getRedis().getKey());
            Map<String, String> routes = operations.entries();
            if (!CollectionUtils.isEmpty(routes)) {
                routes.values().forEach(routeDefinitionStr -> routeDefinitions.add(JSONUtils.toBean(routeDefinitionStr, RouteDefinition.class)));
            }
        }
        return routeDefinitions;
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        // not support
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        // not support
        return null;
    }

}
