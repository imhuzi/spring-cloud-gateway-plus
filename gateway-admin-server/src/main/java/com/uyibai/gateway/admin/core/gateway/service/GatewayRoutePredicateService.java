package com.uyibai.gateway.admin.core.gateway.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uyibai.gateway.admin.api.model.gateway.GatewayRoutePredicateVo;
import com.uyibai.gateway.admin.core.gateway.entity.GatewayRoutePredicate;

/**
 * <p>
 * 网关支持的路由断言信息 服务类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface GatewayRoutePredicateService extends IService<GatewayRoutePredicate> {
    /**
     * save Predicate info
     *
     * @param PredicateVo Predicate info
     * @return Predicate info
     */
    GatewayRoutePredicateVo saveWithVo(GatewayRoutePredicateVo PredicateVo);

    /**
     * list all Predicate
     *
     * @param PredicateVo query param
     * @return Predicate list
     */
    List<GatewayRoutePredicateVo> listByVo(GatewayRoutePredicateVo PredicateVo);

    Boolean delete(Integer predicateId);
}
