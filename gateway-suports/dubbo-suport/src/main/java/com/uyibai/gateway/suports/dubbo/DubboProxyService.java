package com.uyibai.gateway.suports.dubbo;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

import java.net.URI;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.cloud.dubbo.service.DubboGenericServiceExecutionContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uyibai.gateway.common.utils.BodyParamUtils;
import com.uyibai.gateway.common.utils.HttpParamConverter;
import com.uyibai.gateway.common.utils.ParamCheckUtils;
import com.uyibai.gateway.suports.dubbo.vo.DubboRegister;
import com.uyibai.gateway.suports.dubbo.vo.DubboServiceMeta;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class DubboProxyService extends AbstractDubboProxyService {


    private final List<HttpMessageReader<?>> messageReaders;

    @Autowired
    private GatewayDubboGenericServiceFactory genericServiceFactory;

    public DubboProxyService() {
        this.messageReaders = HandlerStrategies.withDefaults().messageReaders();
    }

    @Override
    DubboServiceHolder getServiceHolder(ServerWebExchange exchange) {
        DubboServiceMeta dubboMeta = getDubboMeta(exchange);
        String data = getData(exchange);
        Pair<String[], Object[]> pair;
        if (ParamCheckUtils.dubboBodyIsEmpty(data)) {
            pair = new ImmutablePair<>(new String[]{}, new Object[]{});
        } else {
            pair = BodyParamUtils.buildParameters(data, dubboMeta.getParameterTypes());
        }
        DubboGenericServiceExecutionContext context = new DubboGenericServiceExecutionContext(dubboMeta.getMethodName(), pair.getLeft(), pair.getRight());
        GenericService genericService = genericServiceFactory.create(dubboMeta);
        return new DubboServiceHolder(genericService, context);
    }

    private DubboServiceMeta getDubboMeta(ServerWebExchange exchange) {
        URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);
        Route route = exchange.getRequiredAttribute(GATEWAY_ROUTE_ATTR);
        URI routeUri = route.getUri();
        JSONObject routeMeta = (JSONObject) JSONObject.toJSON(route.getMetadata());
        // 从路径中获取到的
        String pathMethod = getMethod(requestUrl.getPath());
        JSONArray methods = routeMeta.getJSONArray("methods");
        // 从 methods 查找 pathMethod, 找不到返回 null
        String[] parameterTypes = new String[]{};
        for (int i = 0, size = methods.size(); i < size; i++) {
            JSONObject item = methods.getJSONObject(i);
            if (item != null && item.getString("name").equalsIgnoreCase(pathMethod)) {
                parameterTypes = item.getJSONArray("parameterTypes").toArray(new String[0]);
                break;
            }
        }

        DubboServiceMeta dubboMeta = new DubboServiceMeta();
        dubboMeta.setAppName(routeUri.getHost());
        dubboMeta.setAppPath(routeMeta.getString("appPath"));
        dubboMeta.setServicePath(routeMeta.getString("servicePath"));
        dubboMeta.setServiceFullPath(dubboMeta.getAppPath() + dubboMeta.getServicePath());
        dubboMeta.setMethodName(pathMethod);
        dubboMeta.setHttpMethod(exchange.getRequest().getMethodValue());
        dubboMeta.setGroup(getGroup(routeUri.getPath()));
        dubboMeta.setServiceName(routeUri.getPath());
        dubboMeta.setServiceInterface(getInterface(routeUri.getPath()));
        dubboMeta.setParameterTypes(parameterTypes);
        dubboMeta.setVersion(getVersion(routeUri.getPath()));
        dubboMeta.setRpcType(routeUri.getScheme());
        dubboMeta.setRegistrys(JSONObject.parseArray(routeMeta.getString("registrys"), DubboRegister.class));
        return dubboMeta;
    }

    private String getData(ServerWebExchange exchange) {
        ServerRequest request = ServerRequest.create(exchange, messageReaders);
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
            return requestBody(request);
        }
        if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
            return formData(request);
        }
        return query(request);

    }

    private String requestBody(ServerRequest request) {
        return request.bodyToMono(String.class).switchIfEmpty(Mono.defer(() -> Mono.just(""))).block();
    }

    private String formData(ServerRequest request) {
        return HttpParamConverter
            .toMap(() -> request.formData().switchIfEmpty(Mono.defer(() -> Mono.just(new LinkedMultiValueMap<>()))).block());
    }

    private String query(ServerRequest request) {
        return HttpParamConverter.ofString(() -> request.uri().getQuery());
    }


    private String getMethod(String path) {
        int i = path.lastIndexOf('/');
        return path.substring(i + 1);
    }


    public static String getGroup(String service) {
        if (service != null && service.length() > 0) {
            int i = service.indexOf('/');
            if (i >= 0) {
                return service.substring(0, i);
            }
        }
        return null;
    }

    public static String getInterface(String service) {
        if (service != null && service.length() > 0) {
            int i = service.indexOf('/');
            if (i >= 0) {
                service = service.substring(i + 1);
            }
            i = service.lastIndexOf(':');
            if (i >= 0) {
                service = service.substring(0, i);
            }
        }
        return service;
    }

    public static String getVersion(String service) {
        if (service != null && service.length() > 0) {
            int i = service.lastIndexOf(':');
            if (i >= 0) {
                return service.substring(i + 1);
            }
        }
        return null;
    }

}
