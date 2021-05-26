package com.uyibai.gateway.admin.core.gateway.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uyibai.gateway.admin.api.model.gateway.GatewayGroupVo;
import com.uyibai.gateway.admin.core.gateway.entity.GatewayGroup;

/**
 * <p>
 * 网关实例分组 服务类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface GatewayGroupService extends IService<GatewayGroup> {

    GatewayGroupVo save(GatewayGroupVo groupVo);

    Boolean delete(Integer groupId);

    List<GatewayGroupVo> listByVo(GatewayGroupVo groupVo);
}
