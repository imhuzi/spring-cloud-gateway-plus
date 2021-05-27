package com.uyibai.gateway.admin.api;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uyibai.gateway.admin.api.model.gateway.GatewayGroupVo;
import com.uyibai.gateway.admin.api.service.GatewayGroupRpcService;
import com.uyibai.gateway.admin.core.gateway.service.GatewayGroupService;


@RestController
public class GatewayGroupApi implements GatewayGroupRpcService {

    private static final String PATH_PREFIX = "/groups";


    @Resource
    GatewayGroupService gatewayGroupService;


    @Override
    @PostMapping(path = PATH_PREFIX)
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
    @DeleteMapping(path = PATH_PREFIX)
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
    @GetMapping(path = PATH_PREFIX)
    public List<GatewayGroupVo> list(GatewayGroupVo groupVo) {
        return gatewayGroupService.listByVo(groupVo);
    }
}
