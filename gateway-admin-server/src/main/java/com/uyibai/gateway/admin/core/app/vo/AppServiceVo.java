package com.uyibai.gateway.admin.core.app.vo;

import lombok.Data;

/**
 * dubbo service info
 */
@Data
public class AppServiceVo {
    /**
     * 应用名称
     */
    private String application;

    /**
     * 地址
     */
    private String address;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 接口名称
     */
    private String service;

    private String name = "";
    /**
     * 版本
     */
    private String version;

    /**
     * sdk version
     */
    private String sdkVersion;

    // 分组
    private String group;
    /**
     * 权重
     */
    private Integer weight;

    // 服务路径
    private String path;
    /**
     * 服务 url
     */
    private String url;

    // 服务定义的元信息
    private String meta;

    /**
     * 参数
     */
    private String param;

    // register,
    private AppRegistryVo registry;

}
