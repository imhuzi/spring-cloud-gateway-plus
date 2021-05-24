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


import com.uyibai.gateway.suports.dubbo.DubboProxyService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 两种方式订阅服务:
 * （1）1> 根据  subscribedServices  来 获取订阅的服务(应用名称)， 2>. 根据  subscribedServices, 获取 DubboMetadataService， 通过 DubboMetadataService 获取 AllExportedURLs
 * （2）
 * <p>
 * 从 route 中截取 interface,
 */
@Component
@Slf4j
public class DubboRoutingFilter implements GlobalFilter, Ordered {


    @Autowired
    private DubboProxyService dubboProxyService;

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
        log.trace("DubboRoutingFilter start");
        // 获取requestUrl
        URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);
        String scheme = requestUrl.getScheme();
        // 判断 是否能够处理
        if (isAlreadyRouted(exchange) || (!"dubbo".equals(scheme))) {
            return chain.filter(exchange);
        }
        // 设置已路由
        setAlreadyRouted(exchange);
        return dubboProxyService.genericInvoker(exchange, chain);
    }

    @Override
    public int getOrder() {
        // NettyRoutingFilter 的 order 是 Ordered.LOWEST_PRECEDENCE
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
