package com.uyibai.gateway.admin.core.route.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uyibai.gateway.admin.api.model.route.DynRouteFilterVo;
import com.uyibai.gateway.admin.core.route.entity.DynRouteFilter;

/**
 * <p>
 * 动态路由过滤器链表 Mapper 接口
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface DynRouteFilterMapper extends BaseMapper<DynRouteFilter> {
    List<DynRouteFilterVo> queryVoListByRouteId(@Param("routeId") Integer routeId);
}
