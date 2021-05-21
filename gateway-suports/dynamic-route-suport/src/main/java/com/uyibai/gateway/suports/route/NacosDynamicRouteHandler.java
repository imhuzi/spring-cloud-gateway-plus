package com.uyibai.gateway.suports.route;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.uyibai.gateway.common.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 基于 nacos的动态路由
 */
public class NacosDynamicRouteHandler {
    private static final Logger log = LoggerFactory.getLogger(NacosDynamicRouteHandler.class);

    private final String nacosDataId;

    private final String nacosGroupId;

    @Autowired
    private DynamicRouteService dynamicRouteService;

    // nacos 的配置信息
    private final NacosConfigManager nacosConfigManager;

    @PostConstruct
    public void init() {
        log.info("gateway route init...");
        try {
            String configInfo = nacosConfigManager.getConfigService()
                    .getConfig(nacosDataId, nacosGroupId, 5000);

            log.info("获取网关当前配置:\r\n{}", configInfo);
            List<RouteDefinition> definitionList = getListByStr(configInfo);
            for (RouteDefinition definition : definitionList) {
                log.info("update route : {}", definition.toString());
                dynamicRouteService.add(definition);
            }
        } catch (Exception e) {
            log.error("初始化网关路由时发生错误", e);
        }
        addListener();
    }

    // 构造器
    public NacosDynamicRouteHandler(NacosConfigManager nacosConfigManager, DynamicRouteProperties dynamicRouteProperties) {
        this.nacosConfigManager = nacosConfigManager;
        this.nacosDataId = dynamicRouteProperties.getNacos().getDataId();
        this.nacosGroupId = dynamicRouteProperties.getNacos().getGroupId();
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
                    log.info("update route ByNacos: {}", configInfo);
                    dynamicRouteService.updateList(getListByStr(configInfo));
                }
            });
        } catch (NacosException e) {
            log.error("nacos-addListener-error", e);
        }
    }

    // 从 json 中解析出路由配置信息 —— 所以配置文件的格式一定要写对！
    private List<RouteDefinition> getListByStr(String content) {
        if (StringUtils.isNotEmpty(content)) {
            return JSONUtils.toList(content, RouteDefinition.class);
        }
        return new ArrayList<>(0);
    }
}
