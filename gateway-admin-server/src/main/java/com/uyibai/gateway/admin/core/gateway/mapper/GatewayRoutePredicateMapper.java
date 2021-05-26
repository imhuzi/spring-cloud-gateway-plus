package com.uyibai.gateway.admin.core.gateway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uyibai.gateway.admin.api.model.gateway.GatewayRoutePredicateVo;
import com.uyibai.gateway.admin.core.gateway.entity.GatewayRoutePredicate;

/**
 * <p>
 * 网关支持的路由断言信息 Mapper 接口
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface GatewayRoutePredicateMapper extends BaseMapper<GatewayRoutePredicate> {

    List<GatewayRoutePredicateVo> queryVoList(@Param("param") GatewayRoutePredicateVo param);
}
