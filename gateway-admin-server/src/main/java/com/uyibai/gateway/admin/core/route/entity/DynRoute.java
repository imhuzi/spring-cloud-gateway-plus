package com.uyibai.gateway.admin.core.route.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 动态路由信息表
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(autoResultMap = true)
public class DynRoute implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer appId;

    /**
     * app service id
     */
    private Integer serviceId;


    /**
     * 路由名称
     */
    private String name;

    /**
     * 路由id(可读性的字符串)
     */
    private String routeKey;

    /**
     * 路由类型(http,lb,dubbo,grpc)
     */
    private String routeType;

    /**
     * 路由uri地址
     */
    private String routeUri;

    /**
     * 分组id
     */
    private Integer groupId;

    /**
     * meta数据
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private HashMap<String, Object> metadata;

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
     * 分组 下的排序
     */
    private Integer orderNum;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String ROUTE_KEY = "route_key";

    public static final String APP_ID= "app_id";

    public static final String ROUTE_TYPE = "route_type";

    public static final String SERVICE_ID = "service_id";

    public static final String ROUTE_URI = "route_uri";

    public static final String GROUP_ID = "group_id";

    public static final String META_DATA = "metadata";

    public static final String CREATED_AT = "created_at";

    public static final String UPDATED_AT = "updated_at";

    public static final String STATUS = "status";
    public static final String ORDER_NUM = "order_num";

}
