/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uyibai.gateway.suports.dubbo.filter;

import java.nio.charset.StandardCharsets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.WebClientWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;


import com.uyibai.gateway.common.constants.Constants;
import com.uyibai.gateway.common.utils.JSONUtils;

import reactor.core.publisher.Mono;

/**
 * dubbo 调用 结果 response
 *
 * @see WebClientWriteResponseFilter
 */
@Component
public class DubboWriteResponseFilter implements GlobalFilter, Ordered {

    /**
     * Order of Write Response Filter.
     */
    public static final int WRITE_RESPONSE_FILTER_ORDER = -1;

    private static final Log log = LogFactory.getLog(DubboWriteResponseFilter.class);

    @Override
    public int getOrder() {
        return WRITE_RESPONSE_FILTER_ORDER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // NOTICE: nothing in "pre" filter stage as DUBBO_RPC_RESULT is not added
        // until the WebHandler is run
        return chain.filter(exchange).doOnError(throwable -> cleanup(exchange))
                .then(Mono.defer(() -> {
                    Object responseData = exchange.getAttribute(Constants.DUBBO_RPC_RESULT);
                    if (responseData == null) {
                        return Mono.empty();
                    }
                    log.trace("DubboWriteResponseFilter start");
                    ServerHttpResponse response = exchange.getResponse();
                    // to bytes
                    byte[] bytes = JSONUtils.toBytes(responseData);
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    return response
                            .writeWith(Mono.just(buffer))
                            .doOnCancel(() -> cleanup(exchange));
                }));
    }

    private void cleanup(ServerWebExchange exchange) {
        exchange.getAttributes().remove(Constants.DUBBO_RPC_RESULT);
    }

}
