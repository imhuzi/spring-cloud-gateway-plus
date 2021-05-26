package com.uyibai.gateway.admin.core.app.vo;

import java.util.Set;

import lombok.Data;

@Data
public class AppServiceDetailVo {

    private Integer id;

    /**
     * 应用名称
     */
    private String app;

    private Integer appId;

    private Set<Integer> registryIds;

    /**
     * 应用 uri 前缀
     */
    private String appUriPrefix;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 接口名称
     */
    private String service;

    private String name;

    private String sdkType;

    /**
     * 版本
     */
    private String version;

    // 分组
    private String group;

    // 服务路径
    private String path;

    /**
     * route uri前缀
     */
    private String routeUriPrefix;

    // 服务定义的元信息
    private String meta;

    // register,
    private Set<AppRegistryVo> registrys;

}
