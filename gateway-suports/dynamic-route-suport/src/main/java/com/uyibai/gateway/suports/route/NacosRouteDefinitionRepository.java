package com.uyibai.gateway.suports.route;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.uyibai.gateway.common.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 基于 nacos的动态路由
 */
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {
    private static final Logger log = LoggerFactory.getLogger(NacosRouteDefinitionRepository.class);

    private final String nacosDataId;

    private final String nacosGroupId;

    // 更新路由信息需要的
    private final  ApplicationEventPublisher publisher;

    // nacos 的配置信息
    private final NacosConfigManager nacosConfigManager;

    // 构造器
    public NacosRouteDefinitionRepository(ApplicationEventPublisher publisher, NacosConfigManager nacosConfigManager, DynamicRouteProperties dynamicRouteProperties) {
        this.publisher = publisher;
        this.nacosConfigManager = nacosConfigManager;
        this.nacosDataId = dynamicRouteProperties.getNacos().getDataId();
        this.nacosGroupId = dynamicRouteProperties.getNacos().getGroupId();
        addListener();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        log.info("NacosRouteDefinitionRepository.getRouteDefinitions");
        try {
            String content = nacosConfigManager.getConfigService()
                    .getConfig(nacosDataId, nacosGroupId,5000);
            List<RouteDefinition> routeDefinitions = getListByStr(content);
            return Flux.fromIterable(routeDefinitions);
        } catch (NacosException e) {
            log.error("getRouteDefinitions by nacos error", e);
        }
        return Flux.fromIterable(Collections.emptyList());
    }

    /**
     * 添加Nacos监听
     */
    private void addListener() {
        try {
            nacosConfigManager.getConfigService().addListener(nacosDataId, nacosGroupId, new Listener() {
                @Override
                public Executor getExecutor() {
                    log.info("Nacos监听 Executor:__");
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("Nacos监听:自动更新配置...\r\n" + configInfo);
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        } catch (NacosException e) {
            log.error("nacos-addListener-error", e);
        }
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        //
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    // 从 json 中解析出路由配置信息 —— 所以配置文件的格式一定要写对！
    private List<RouteDefinition> getListByStr(String content) {
        if (StringUtils.isNotEmpty(content)) {
            return JSONUtils.toBean(content, new TypeReference<ArrayList<RouteDefinition>>() { });
        }
        return new ArrayList<>(0);
    }
}
