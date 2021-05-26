package com.uyibai.gateway.admin.api;

import java.util.List;

import javax.annotation.Resource;

import com.uyibai.gateway.admin.api.model.gateway.GatewayGroupVo;
import com.uyibai.gateway.admin.api.service.GatewayGroupRpcService;
import com.uyibai.gateway.admin.core.gateway.service.GatewayGroupService;


public class GatewayGroupApi implements GatewayGroupRpcService {

    private static final String PATH_PREFIX = "/filters";


    @Resource
    GatewayGroupService gatewayGroupService;


    @Override
    public GatewayGroupVo save(GatewayGroupVo groupVo) {
        return gatewayGroupService.save(groupVo);
    }

    /**
     * delete group  by id
     *
     * @param groupId 分组id
     * @return if success return true Otherwise false
     */
    @Override
    public Boolean delete(Integer groupId) {
        return gatewayGroupService.delete(groupId);
    }

    /**
     * list all group
     *
     * @param groupVo query param
     * @return filter list
     */
    @Override
    public List<GatewayGroupVo> list(GatewayGroupVo groupVo) {
        return gatewayGroupService.listByVo(groupVo);
    }
}
