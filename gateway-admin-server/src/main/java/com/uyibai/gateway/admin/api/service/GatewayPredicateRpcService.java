package com.uyibai.gateway.admin.api.service;

import java.util.List;

import com.uyibai.gateway.admin.api.model.gateway.GatewayRoutePredicateVo;

/**
 * 网关 路由规则相关业务
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
public interface GatewayPredicateRpcService {

    // predicate 增删改查

    /**
     * save Predicate info
     *
     * @param PredicateVo Predicate info
     * @return Predicate info
     */
    GatewayRoutePredicateVo save(GatewayRoutePredicateVo PredicateVo);

    /**
     * delete Predicate  by id
     *
     * @param filterId 过滤器id
     * @return if success return true Otherwise false
     */
    Boolean delete(Integer filterId);

    /**
     * list all Predicate
     *
     * @param PredicateVo query param
     * @return Predicate list
     */
    List<GatewayRoutePredicateVo> list(GatewayRoutePredicateVo PredicateVo);

}
