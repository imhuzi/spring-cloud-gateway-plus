package com.uyibai.gateway.admin.api.model.gateway;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 网关实例分组
 * </p>
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GatewayGroupVo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 分组名称
     */
    private String name;

    private String remark;

    /**
     * 分组ID
     */
    private String groupKey;

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

}
