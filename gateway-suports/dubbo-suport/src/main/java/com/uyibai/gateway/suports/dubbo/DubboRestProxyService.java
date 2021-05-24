package com.uyibai.gateway.suports.dubbo;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.cloud.dubbo.http.MutableHttpServerRequest;
import com.alibaba.cloud.dubbo.metadata.DubboRestServiceMetadata;
import com.alibaba.cloud.dubbo.metadata.RequestMetadata;
import com.alibaba.cloud.dubbo.metadata.RestMethodMetadata;
import com.alibaba.cloud.dubbo.metadata.repository.DubboServiceMetadataRepository;
import com.alibaba.cloud.dubbo.service.DubboGenericServiceExecutionContext;
import com.alibaba.cloud.dubbo.service.DubboGenericServiceExecutionContextFactory;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DubboRestProxyService extends AbstractDubboProxyService {

    @Autowired
    private DubboServiceMetadataRepository serviceMetadataRepository;

    @Autowired
    private GatewayDubboGenericServiceFactory genericServiceFactory;

    @Autowired
    private DubboGenericServiceExecutionContextFactory executionFactory;


    private final Map<String, Object> dubboTranslatedAttributes = new HashMap<>();

    @Override
    public DubboServiceHolder getServiceHolder(ServerWebExchange exchange) {
        // 经过 RouteToRequestUrlFilter 处理 requestUrl.getHost 已经是 服务名称了
        // serviceName 是指  spring.application.name
        URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);
        String serviceName = requestUrl.getHost();
        String servicePath = getServicePath(requestUrl.getPath());

        // 初始化 serviceName 的 REST 请求元数据
        // todo 异步处理
        // 1. 根据 serviceName initDubboRestServiceMetadataRepository 有缓存机制，只初始化一次
        // 2. 根据 第一次 会生成 DubboMetadataService 代理，并调用 DubboMetadataService.getServiceRestMetadata
        serviceMetadataRepository.initializeMetadata(serviceName);

        // 将 HttpServletRequest 转化为 RequestMetadata
        ServerHttpRequest request = exchange.getRequest();
        // build request metadata
        RequestMetadata clientMetadata = buildRequestMetadata(request, servicePath);

        // 1. 根据 serviceName 和 RequestMetadata 从  repository 中 获取 DubboRestServiceMetadata 的元数据
        // todo 可以做缓存, 根据 serviceName + clientMetadata
        DubboRestServiceMetadata dubboRestServiceMetadata = serviceMetadataRepository.get(serviceName, clientMetadata);

        if (dubboRestServiceMetadata == null) {
            // if DubboServiceMetadata is not found, executes next
            throw new IllegalStateException("DubboServiceMetadata can't be found!");
        }
        // Get the Request Body from HttpServletRequest
        // request body方式的参数
        byte[] body = getRequestBody(request);
        MutableHttpServerRequest httpServerRequest = new MutableHttpServerRequest(new HttpRequestAdapter(request), body);
        // serviceExecution.create 方法中会根据 RestMethodMetadata 从 body, request, header 中解析参数，并封装调用
        // get service method
        RestMethodMetadata dubboRestMethodMetadata = dubboRestServiceMetadata.getRestMethodMetadata();
        DubboGenericServiceExecutionContext context = executionFactory.create(dubboRestMethodMetadata, httpServerRequest);
        // 异步调用
        // create GenericService with dubboRestServiceMetadata
        GenericService genericService = genericServiceFactory.create(dubboRestServiceMetadata, dubboTranslatedAttributes);

        return new DubboServiceHolder(genericService, context);
    }


    /**
     * 从 path 中获取服务 path
     * 如： /demo/user/id,  返回 /user/id
     *
     * @param path 完整的路径
     * @return
     */
    private String getServicePath(String path) {
        int i = path.indexOf('/', 1);
        return path.substring(i);
    }

    /**
     * 获取 request Body
     *
     * @param request
     * @return
     */
    private byte[] getRequestBody(ServerHttpRequest request) {
        AtomicReference<ByteBuffer> byteBuffer = new AtomicReference<>();
        request.getBody().subscribe(dataBuffer -> {
            byteBuffer.set(dataBuffer.asByteBuffer());
//            dataBuffer.asInputStream();
        });
        return byteBuffer.get() != null ? byteBuffer.get().array() : new byte[0];
    }

    private RequestMetadata buildRequestMetadata(ServerHttpRequest request,
                                                 String restPath) {
//        UriComponents uriComponents = UriComponentsBuilder.fromUriString(request.getURI().toString()).build(true);
        RequestMetadata requestMetadata = new RequestMetadata();
        requestMetadata.setPath(restPath);
        requestMetadata.setMethod(request.getMethodValue());
        requestMetadata.setParams(getParams(request));
        requestMetadata.setHeaders(getHeaders(request));
        return requestMetadata;
    }


    private Map<String, List<String>> getHeaders(ServerHttpRequest request) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        request.getHeaders().forEach(map::put);
        return map;
    }

    private Map<String, List<String>> getParams(ServerHttpRequest request) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : request.getQueryParams().entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }


    private final static class HttpRequestAdapter implements HttpRequest {

        private final ServerHttpRequest request;

        private HttpRequestAdapter(ServerHttpRequest request) {
            this.request = request;
        }

        @Override
        public String getMethodValue() {
            return request.getMethodValue();
        }

        @Override
        public URI getURI() {
            return request.getURI();
        }

        @Override
        public HttpHeaders getHeaders() {
            return request.getHeaders();
        }

    }

}
