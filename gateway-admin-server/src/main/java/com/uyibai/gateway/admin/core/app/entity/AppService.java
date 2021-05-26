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
 * Dubbo 服务信息
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AppService implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * service名称
     */
    private String service;

    /**
     * 服务版本
     */
    private String version;

    /**
     * 服务分组
     */
    @TableField(value = "`group`")
    private String group;

    /**
     * 协议类型
     */
    private String protocol;

    /**
     * 服务类型(dubbo,sofa,motan)
     */
    private String sdkType;

    private String sdkVersion;

    /**
     * 应用id
     */
    private Integer appId;

    /**
     * 注册中心id
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Integer> registryIds;

    /**
     * route uri前缀
     */
    private String routeUriPrefix;

    private String path;

    /**
     * service中文名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * service 定义信息
     */
    private String meta;

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
    @TableField(value = "`status`")
    private Integer status;


    public static final String ID = "id";

    public static final String SERVICE = "service";

    public static final String VERSION = "version";

    public static final String GROUP = "group";

    public static final String PROTOCOL = "protocol";

    public static final String SDK_TYPE = "sdk_type";
    public static final String SDK_VERSION= "sdk_version";

    public static final String APP_ID = "app_id";

    public static final String REGISTRY_IDS = "registry_ids";

    public static final String ROUTE_URI_PREFIX = "route_uri_prefix";

    public static final String PATH = "path";

    public static final String NAME = "name";

    public static final String REMARK = "remark";

    public static final String DEFINITION = "definition";

    public static final String CREATED_AT = "created_at";

    public static final String UPDATED_AT = "updated_at";

    public static final String STATUS = "status";

}
