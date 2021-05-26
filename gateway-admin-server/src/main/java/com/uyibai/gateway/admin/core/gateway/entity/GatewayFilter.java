package com.uyibai.gateway.admin.core.gateway.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.uyibai.gateway.admin.api.model.definition.ArgsDefinition;
import com.uyibai.gateway.admin.config.ArgsDefinitionTypeHandler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 网关支持的过滤器信息
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GatewayFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
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


    /**
     * 过滤器唯一key值
     */
    private String filterKey;

    private String remark;

    /**
     * 过滤器参数
     */
    @TableField(typeHandler = ArgsDefinitionTypeHandler.class)
    private List<ArgsDefinition> args;

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

    /**
     * Filter本身的 顺序
     */
    private Integer orderNum;

    /**
     * 是否 允许 多个
     */
    private Boolean multi;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String FILTER_TYPE = "filter_type";

    public static final String FILTER_SCOPE = "filter_scope";

    public static final String FILTER_KEY = "filter_key";

    public static final String REMARK = "remark";


    public static final String ARGS = "args";

    public static final String CREATED_AT = "created_at";

    public static final String UPDATED_AT = "updated_at";

    public static final String STATUS = "status";

    public static final String MULTI = "multi";

    public static final String ORDER_NUM = "order_num";

}
