package com.uyibai.gateway.suports.dubbo.filter;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.isAlreadyRouted;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setAlreadyRouted;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;


import com.uyibai.gateway.suports.dubbo.DubboRestProxyService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * dubbo 服务路由 过滤器, 主要处理 dubbo Rest 服务的泛化调用
 * <p>
 * （1）根据服务名得到注册中心的Dubbo服务DubboMetadataService。
 * <p>
 * （2）使用DubboMetadataService里提供的getServiceRestMetadata方法获取要使用的Dubbo服务和对应的Rest元数据。
 * <p>
 * （3）基于Dubbo服务和Rest元数据构造GenericService。
 * <p>
 * （4）服务调用过程中使用GenericService发起泛化调用。
 */
@Component
@Slf4j
public class DubboRestRoutingFilter implements GlobalFilter, Ordered {

    @Autowired
    private DubboRestProxyService dubboRestProxyService;

    /**
     * Process the Web request and (optionally) delegate to the next {@code WebFilter}
     * through the given {@link GatewayFilterChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.trace("DubboRestRoutingFilter start");
        // 获取requestUrl
        URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);
        String scheme = requestUrl.getScheme();
        // 判断 是否能够处理
        if (isAlreadyRouted(exchange) || (!"dubboRest".equals(scheme))) {
            return chain.filter(exchange);
        }
        // 设置已路由
        setAlreadyRouted(exchange);
        return dubboRestProxyService.genericInvoker(exchange, chain);
    }

    @Override
    public int getOrder() {
        // NettyRoutingFilter 的 order 是 Ordered.LOWEST_PRECEDENCE
        return Ordered.LOWEST_PRECEDENCE - 3;
    }

}
