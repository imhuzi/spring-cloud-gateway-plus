package com.uyibai.gateway.admin.api.model.definition;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 路由规则定义
 * 和 org.springframework.cloud.gateway.handler.predicate.PredicateDefinition 结构相同
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
@Data
public class PredicateDefinition  implements Serializable {
    @NotNull
    private String name;

    private Map<String, String> args = new LinkedHashMap<>();
}
