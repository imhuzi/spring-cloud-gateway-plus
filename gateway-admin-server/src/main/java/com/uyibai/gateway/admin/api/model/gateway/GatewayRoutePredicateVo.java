package com.uyibai.gateway.admin.api.model.gateway;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.uyibai.gateway.admin.api.model.definition.ArgsDefinition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 网关支持的路由断言信息
 * </p>
 *
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GatewayRoutePredicateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 路由规则断言唯一key值
     */
    private String predicateKey;

    /**
     * 路由规则断言参数
     */
    /**
     * 过滤器参数
     */
    private List<ArgsDefinition> args;


    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    private LocalDateTime updatedAt;

    /**
     * 状态(0,未启用,1 启用)
     */
    private Integer status;

    /**
     * 是否 允许 多个
     */
    private Boolean multi;
}
