package com.uyibai.gateway.admin.core.route.publisher;

import java.util.List;

import com.uyibai.gateway.admin.api.model.definition.GatewayRouteDefinition;

/**
 * 网关路由发布方式接口
 */
public interface GatewayRoutePublisher {

    /**
     * 发布 单个 路由
     *
     * @param routeDefinition
     */
    void publishOne(GatewayRouteDefinition routeDefinition);

    /**
     * 批量发布路由
     *
     * @param routeDefinitionList
     */
    void publishBatch(List<GatewayRouteDefinition> routeDefinitionList);

    /**
     * 批量下线路由
     *
     * @param routeDefinitionList
     */
    void offlineBatch(List<GatewayRouteDefinition> routeDefinitionList);

    /**
     * 下线 路由
     *
     * @param groupKey
     * @param routeId
     */
    void offline(String groupKey, String routeId);
}
