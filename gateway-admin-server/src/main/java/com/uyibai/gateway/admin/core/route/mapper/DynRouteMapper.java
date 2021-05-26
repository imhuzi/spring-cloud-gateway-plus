package com.uyibai.gateway.admin.core.route.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uyibai.gateway.admin.api.model.route.DynRouteQueryParam;
import com.uyibai.gateway.admin.api.model.route.DynRouteVo;
import com.uyibai.gateway.admin.core.route.entity.DynRoute;

/**
 * <p>
 * 动态路由信息表 Mapper 接口
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface DynRouteMapper extends BaseMapper<DynRoute> {

    List<DynRouteVo> selectByQueryParam(@Param("param") DynRouteQueryParam param);

    DynRouteVo selectVoById(@Param("routeId") Integer routeId);
}
