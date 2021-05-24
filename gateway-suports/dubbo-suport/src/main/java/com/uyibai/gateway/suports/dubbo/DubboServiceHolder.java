package com.uyibai.gateway.suports.dubbo;

import org.apache.dubbo.rpc.service.GenericService;

import com.alibaba.cloud.dubbo.service.DubboGenericServiceExecutionContext;

import lombok.Data;

@Data
public class DubboServiceHolder {
    private final GenericService genericService;

    private final DubboGenericServiceExecutionContext context;

    public String getMethodName() {
        return context.getMethodName();
    }

    public String[] getParameterTypes() {
        return context.getParameterTypes();
    }

    public Object[] getParameters() {
        return context.getParameters();
    }

    public DubboServiceHolder(GenericService genericService, DubboGenericServiceExecutionContext context) {
        this.genericService = genericService;
        this.context = context;
    }
}
