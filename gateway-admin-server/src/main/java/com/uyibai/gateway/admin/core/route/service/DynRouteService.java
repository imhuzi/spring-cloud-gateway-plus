package com.uyibai.gateway.admin.core.route.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uyibai.gateway.admin.api.model.route.DynRouteQueryParam;
import com.uyibai.gateway.admin.api.model.route.DynRouteSyncParam;
import com.uyibai.gateway.admin.api.model.route.DynRouteVo;
import com.uyibai.gateway.admin.core.route.entity.DynRoute;

/**
 * <p>
 * 动态路由信息表 服务类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface DynRouteService extends IService<DynRoute> {

    /**
     * 发布 上线 单个路由 信息
     * <p>
     * 如果失败 抛异常
     *
     * @param dynRouteId 路由id
     */
    void online(Integer dynRouteId);

    /**
     * 下线单个路由
     *
     * @param dynRouteId 路由id
     */
    void offline(Integer dynRouteId);


    /**
     * 动态路由 同步
     * <p>
     * 1. 支持 全量 或者指定 路由 同步 到网关
     * 2. 支持 全量 或者 指定 节点 同步
     *
     * @param syncParam 同步参数
     */
    void sync(DynRouteSyncParam syncParam);

    /**
     * 所有的 路由列表
     * <p>
     * 根据 id, route_type,route_key,分组id，filterId, predicateId,status 筛选，
     * <p>
     * 按 createdAt,orderNum 排序
     *
     * @param queryParam 路由列表查询参数
     * @return 成功返回 路由列表
     */
    List<DynRouteVo> listRoutes(DynRouteQueryParam queryParam);

    /**
     * 查询 所有 路由 的基本信息
     *
     * @param queryParam 查询参数
     * @return
     */
    List<DynRouteVo> listSimpleInfo(DynRouteQueryParam queryParam);

    /**
     * 保存路由信息: 基础信息，filter,predicate
     *
     * @param routeVo 路由信息
     * @return 保存成功返回 路由信息，否则抛异常
     */
    DynRouteVo save(DynRouteVo routeVo);

    /**
     * 根据 路由id 查询单个路由信息
     *
     * @param routeId 路由id
     * @return 路由信息详情
     */
    DynRouteVo queryById(Integer routeId);

    /**
     * 查询 简单的 路由信息
     *
     * @param routeId
     * @return
     */
    DynRouteVo querySimpleById(Integer routeId);

    /**
     * 删除  路由
     *
     * @param id 路由id
     */
    Boolean delete(Integer id);
}
