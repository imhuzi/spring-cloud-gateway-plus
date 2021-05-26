package com.uyibai.gateway.admin.core.route.publisher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.uyibai.gateway.admin.api.model.definition.GatewayRouteDefinition;
import com.uyibai.gateway.admin.config.GatewayAdminProperties;
import com.uyibai.gateway.common.utils.JSONUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于 redis 的路由发布方式
 */
@Service
@Slf4j
public class RedisGatewayRoutePublisher implements GatewayRoutePublisher {

    @Resource
    GatewayAdminProperties gatewayAdminProperties;

    @Resource
    RedisTemplate<String, String> stringRedisTemplate;

    /**
     * 发布 单个 路由
     *
     * @param routeDefinition
     */
    @Override
    public void publishOne(GatewayRouteDefinition routeDefinition) {
        String key = gatewayAdminProperties.getRedis().getKey();
        stringRedisTemplate.opsForHash().put(key, routeDefinition.getId(), JSONUtils.toStr(routeDefinition));
    }

    /**
     * 批量发布路由
     *
     * @param routeDefinitionList
     */
    @Override
    public void publishBatch(List<GatewayRouteDefinition> routeDefinitionList) {
        String key = gatewayAdminProperties.getRedis().getKey();
        Map<String, String> routeMap = new HashMap<>();
        routeDefinitionList.forEach(item -> {
            routeMap.put(item.getId(), JSONUtils.toStr(item));
        });
        stringRedisTemplate.opsForHash().putAll(key, routeMap);
    }

    /**
     * 批量下线路由
     *
     * @param routeDefinitionList
     */
    @Override
    public void offlineBatch(List<GatewayRouteDefinition> routeDefinitionList) {
        String key = gatewayAdminProperties.getRedis().getKey();
        stringRedisTemplate.opsForHash().delete(key, routeDefinitionList.stream().map(GatewayRouteDefinition::getId).distinct().toArray());
    }

    /**
     * 下线 路由
     *
     * @param groupKey
     * @param routeId
     */
    @Override
    public void offline(String groupKey, String routeId) {
        String key = gatewayAdminProperties.getRedis().getKey();
        Long ret = stringRedisTemplate.opsForHash().delete(key, routeId);
        log.info("offlineRest:{}", ret);
    }
}
