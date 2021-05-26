package com.uyibai.gateway.admin.core.route.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uyibai.gateway.admin.api.model.route.DynRoutePredicateVo;
import com.uyibai.gateway.admin.core.route.entity.DynRoutePredicate;

/**
 * <p>
 * 动态路由规则表 Mapper 接口
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface DynRoutePredicateMapper extends BaseMapper<DynRoutePredicate> {

    List<DynRoutePredicateVo> queryVoListByRouteId(Integer routeId);

}
