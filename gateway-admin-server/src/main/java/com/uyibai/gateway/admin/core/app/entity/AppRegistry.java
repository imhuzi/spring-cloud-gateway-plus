package com.uyibai.gateway.admin.core.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

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
 * Dubbo 注册中心配置
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AppRegistry implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 注册中心id
     */
    private String registryKey;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 地址
     */
    private String address;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 附加参数
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> parameters;

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

    public static final String NAME = "name";

    public static final String REGISTRY_KEY = "registry_key";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String ADDRESS = "address";

    public static final String REMARK = "remark";

    public static final String PARAMETERS = "parameters";

    public static final String CREATED_AT = "created_at";

    public static final String UPDATED_AT = "updated_at";

    public static final String STATUS = "status";

}
