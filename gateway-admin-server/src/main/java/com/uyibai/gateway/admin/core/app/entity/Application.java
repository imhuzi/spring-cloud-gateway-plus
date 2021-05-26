package com.uyibai.gateway.admin.core.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用信息(以项目维度)
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Application implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 应用唯一标识(和spring.application.name一致)
     */
    private String appId;

    /**
     * 应用类型(dubbo,springCloud,dubboSpringCloud,http)
     */
    private String appType;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 实例信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<String> instances;

    @TableField(exist = false)
    private String address;

    /**
     * uri 前缀 用于网关代理
     */
    private String uriPrefix;

    /**
     * 备注说明
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
    private Integer status;


    public static final String ID = "id";

    public static final String APP_ID = "app_id";

    public static final String APP_TYPE = "app_type";

    public static final String APP_NAME = "app_name";

    public static final String INSTANCES = "instances";

    public static final String URI_PREFIX = "uri_prefix";

    public static final String REMARK = "remark";

    public static final String CREATED_AT = "created_at";

    public static final String UPDATED_AT = "updated_at";

    public static final String STATUS = "status";

}
