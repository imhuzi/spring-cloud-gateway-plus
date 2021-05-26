package com.uyibai.gateway.admin.core.route.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uyibai.gateway.admin.api.model.route.DynRouteFilterVo;
import com.uyibai.gateway.admin.core.route.entity.DynRouteFilter;

/**
 * <p>
 * 动态路由过滤器链表 服务类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface DynRouteFilterService extends IService<DynRouteFilter> {
    /**
     * 查询 路由关联的所有 filter信息
     *
     * @param routeId 路由id
     * @return list DynRouteFilterVo
     */
    List<DynRouteFilter> queryByRouteId(Integer routeId);

    List<DynRouteFilterVo> queryVoListByRouteId(Integer routeId);

    void delete(Integer routeId, Integer filterId);

}
