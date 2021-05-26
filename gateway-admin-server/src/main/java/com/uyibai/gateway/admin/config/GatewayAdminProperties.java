package com.uyibai.gateway.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties("spring.cloud.gateway.admin")
@Data
public class GatewayAdminProperties {

    /**
     * 网关 应用名称,  discovery 中的 名称
     */
    private String gatewayName;

    /**
     * 路由发布的类型
     */
    private String publishType;

    /**
     * redis 配置
     */
    private DynamicRouteRedisProperties redis;

    /**
     * nacos 配置
     */
    private DynamicRouteNacosProperties nacos;


    @Data
    public static class DynamicRouteNacosProperties {
        private String dataId;
        private String groupId;
    }

    @Data
    public static class DynamicRouteRedisProperties {
        /**
         * redis 数据类型  zset 或者 hash
         */
        private String type = "hash";

        /**
         * redis key
         */
        private String key;
    }
}
