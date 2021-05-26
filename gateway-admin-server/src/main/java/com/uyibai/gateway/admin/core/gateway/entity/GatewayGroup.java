package com.uyibai.gateway.admin.core.gateway.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 网关实例分组
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GatewayGroup implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组ID
     */
    private String groupKey;
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


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String GROUP_KEY = "group_key";

    public static final String REMARK = "remark";


    public static final String CREATED_AT = "created_at";

    public static final String UPDATED_AT = "updated_at";

    public static final String STATUS = "status";

}
