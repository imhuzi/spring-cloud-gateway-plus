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
 * 网关支持的路由断言信息
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GatewayRoutePredicate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
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
    @TableField(typeHandler = ArgsDefinitionTypeHandler.class)
    private List<ArgsDefinition> args;

    /**
     * 路由规则断言 备注说明
     */
    private String remark;

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
     * 是否 允许 多个
     */
    private Boolean multi;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String PREDICATE_KEY = "predicate_key";

    public static final String ARGS = "args";

    public static final String REMARK = "remark";

    public static final String CREATED_AT = "created_at";

    public static final String UPDATED_AT = "updated_at";

    public static final String STATUS = "status";

    public static final String MULTI = "multi";
}
