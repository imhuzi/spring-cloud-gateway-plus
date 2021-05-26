package com.uyibai.gateway.admin.core.app.vo;

import java.util.Map;

import lombok.Data;

@Data
public class AppRegistryVo {

    private Boolean subscribe;

    /**
     * 注册中心id
     */
    private String registryKey;

    private String registryId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
    /**
     * 地址
     */
    private String address;

    /**
     * 附加参数
     */
    private Map<String, String> parameters;

    private Integer timeout;

    private String group;
}
