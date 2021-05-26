package com.uyibai.gateway.admin.core.route.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uyibai.gateway.admin.api.model.route.DynRoutePredicateVo;
import com.uyibai.gateway.admin.core.route.entity.DynRoutePredicate;

/**
 * <p>
 * 动态路由规则表 服务类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface DynRoutePredicateService extends IService<DynRoutePredicate> {

    /**
     * 查询 路由关联的所有 filter信息
     *
     * @param routeId 路由id
     * @return list DynRouteFilterVo
     */
    List<DynRoutePredicate> queryByRouteId(Integer routeId);

    List<DynRoutePredicateVo> queryVoListByRouteId(Integer routeId);

    void delete(Integer routeId, Integer predicateId);
}
