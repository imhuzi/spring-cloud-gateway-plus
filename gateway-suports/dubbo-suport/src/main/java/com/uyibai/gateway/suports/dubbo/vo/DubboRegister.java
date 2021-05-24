package com.uyibai.gateway.suports.dubbo.vo;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class DubboRegister implements Serializable {
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
