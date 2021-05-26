package com.uyibai.gateway.admin.api.model.definition;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;

/**
 * 和 org.springframework.cloud.gateway.filter.FilterDefinition 结构相同
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
@Data
public class FilterDefinition implements Serializable {
    private String name;

    private Map<String, String> args = new LinkedHashMap<>();
}
