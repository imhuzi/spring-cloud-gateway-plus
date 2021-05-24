package com.uyibai.gateway.suports.dubbo;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

import java.net.URI;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.dubbo.rpc.service.GenericException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;

import com.uyibai.gateway.common.constants.Constants;
import com.uyibai.gateway.common.exception.GatewayException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class AbstractDubboProxyService {
    abstract DubboServiceHolder getServiceHolder(ServerWebExchange exchange);

    public Mono<Void> genericInvoker(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);
        String scheme = requestUrl.getScheme();
        // 从 uri 组装服务信息
        log.info("{}ServiceMetaCollector:{}", scheme, requestUrl);
        long startTime = System.currentTimeMillis();
        DubboServiceHolder serviceHolder = getServiceHolder(exchange);
        log.info("{}ServiceMetaCollector:{}ms", scheme, System.currentTimeMillis() - startTime);

        CompletableFuture<Object> future = serviceHolder.getGenericService().$invokeAsync(serviceHolder.getMethodName(), serviceHolder.getParameterTypes(), serviceHolder.getParameters());
        return Mono.fromFuture(future.thenApply(ret -> {
            if (Objects.isNull(ret)) {
                ret = Constants.DUBBO_RPC_RESULT_EMPTY;
            }
            exchange.getAttributes().put(Constants.DUBBO_RPC_RESULT, ret);
            return Mono.empty();
        })).onErrorMap(exception -> {
            return exception instanceof GenericException ? new GatewayException(((GenericException) exception).getExceptionMessage()) : new GatewayException(exception);
        }).then(chain.filter(exchange));
    }
}
