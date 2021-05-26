package com.uyibai.gateway.admin.api.service;

import java.util.List;

import com.uyibai.gateway.admin.api.model.gateway.GatewayFilterVo;


/**
 * 网关 过滤器管理相关业务
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
public interface GatewayFilterRpcService {
    /**
     * save filter info
     *
     * @param filterVo filter info
     * @return filter info
     */
    GatewayFilterVo save(GatewayFilterVo filterVo);

    /**
     * delete filter  by id
     *
     * @param filterId 过滤器id
     * @return if success return true Otherwise false
     */
    Boolean delete(Integer filterId);

    /**
     * list all filter
     *
     * @param filterVo query param
     * @return filter list
     */
    List<GatewayFilterVo> list(GatewayFilterVo filterVo);

}
