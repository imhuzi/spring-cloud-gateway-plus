package com.uyibai.gateway.admin.core.route.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.uyibai.gateway.admin.api.model.definition.ArgsValueDefinition;
import com.uyibai.gateway.admin.config.ArgsValueDefinitionTypeHandler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 动态路由过滤器链表
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DynRouteFilter implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 路由id
     */
    private Integer routeId;

    /**
     * 过滤器id
     */
//    private Integer filterId;

    private String filterKey;
    /**
     * 参数值
     */
    @TableField(typeHandler = ArgsValueDefinitionTypeHandler.class)
    private List<ArgsValueDefinition> argsValue;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 状态(0,未启用,1 启用)
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer status;

    private Integer routeOrderNum;

    public static final String ID = "id";

    public static final String ROUTE_ID = "route_id";

    public static final String FILTER_ID = "filter_id";

    public static final String FILTER_KEY = "filter_key";

    public static final String ARGS_VALUE = "args_value";

    public static final String CREATED_AT = "created_at";

    public static final String UPDATED_AT = "updated_at";

    public static final String STATUS = "status";

    public static final String ROUTE_ORDER_NUM = "route_order_num";

}
