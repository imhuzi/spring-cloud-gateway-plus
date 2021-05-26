package com.uyibai.gateway.admin.api.service;

import java.util.List;

import com.uyibai.gateway.admin.api.model.gateway.GatewayGroupVo;

/**
 * 网关 分组 相关业务
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
public interface GatewayGroupRpcService {

    /*
     * save group info
     *
     * @param groupVo group info
     * @return group info
     */
    GatewayGroupVo save(GatewayGroupVo groupVo);

    /**
     * delete group  by id
     *
     * @param groupId 分组id
     * @return if success return true Otherwise false
     */
    Boolean delete(Integer groupId);

    /**
     * list all group
     *
     * @param groupVo query param
     * @return filter list
     */
    List<GatewayGroupVo> list(GatewayGroupVo groupVo);
}
