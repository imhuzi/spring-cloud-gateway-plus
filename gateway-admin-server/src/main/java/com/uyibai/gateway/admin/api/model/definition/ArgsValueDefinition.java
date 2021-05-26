package com.uyibai.gateway.admin.api.model.definition;

import java.io.Serializable;

import lombok.Data;

/**
 * 网关 filter 和 predicate 参数 对象
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 */
@Data
public class ArgsValueDefinition implements Serializable {
    /**
     * 参数名称
     */
    private String argsKey;

    /**
     * 值
     */
    private String value;

    /**
     * 排序
     */
    private Integer order = 0;
}
