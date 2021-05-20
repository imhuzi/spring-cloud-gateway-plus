package com.uyibai.gateway.suports.route;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.cloud.gateway.dynamic-route")
@Data
public class DynamicRouteProperties {
    private String publishType;
    /**
     * nacos 配置
     */
    private DynamicRouteNacosProperties nacos;
    /**
     * redis 配置
     */
    private DynamicRouteRedisProperties redis;
}


@Data
class DynamicRouteNacosProperties {
    private String dataId;
    private String groupId;
}

@Data
class DynamicRouteRedisProperties {
    /**
     * redis 数据类型  zset 或者 hash
     */
    private String type = "hash";

    /**
     * redis key
     */
    private String key;
}