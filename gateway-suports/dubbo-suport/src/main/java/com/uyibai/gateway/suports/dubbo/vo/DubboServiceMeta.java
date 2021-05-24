package com.uyibai.gateway.suports.dubbo.vo;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.metadata.definition.model.MethodDefinition;

import lombok.Data;

@Data
public class DubboServiceMeta {
    /**
     * 服务应用名称
     */
    private String appName;
    /**
     * 应用 path
     */
    private String appPath;
    /**
     * 服务 path: servicePath
     */
    private String servicePath;
    /**
     * 服务
     */
    private String serviceFullPath;
    /**
     * 服务类型
     */
    private String rpcType;
    /**
     * service name
     */
    private String serviceName;

    private String serviceInterface;
    /**
     * 方法名称
     */
    private String methodName;

    /**
     * http method
     */
    private String httpMethod;

    /**
     * 方法参数类型
     */
    private String[] parameterTypes;
    /**
     * 版本
     */
    private String version;

    private String group;

    private String loadbalance;

    private Integer retries;

    private Integer timeout;

    private String url;

    private List<DubboRegister> registrys;
    /**
     * 方法定义信息
     */
    private List<MethodDefinition> methods;

    /**
     * 透传信息
     */
    private Map<String, Object> translatedAttrs;

}
