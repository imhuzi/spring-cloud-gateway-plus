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
 * 网关支持的过滤器信息
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
public class GatewayFilterVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 过滤器名称
     */
    private String name;

    /**
     * 过滤器类型(1:pre, 2:post)
     */
    private Integer filterType;

    /**
     * 过滤器作用域(1:global,2:route)
     */
    private Integer filterScope;


    private String remark;

    /**
     * 过滤器唯一key值
     */
    private String filterKey;

    /**
     * 过滤器参数
     */
    private List<ArgsDefinition> args;

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
     * Filter本身的 顺序
     */
    private Integer orderNum;

    /**
     * 是否 允许 多个
     */
    private Boolean multi;
}
