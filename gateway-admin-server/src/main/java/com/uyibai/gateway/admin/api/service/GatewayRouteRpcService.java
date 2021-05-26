package com.uyibai.gateway.admin.api.service;

import java.util.List;

import com.uyibai.gateway.admin.api.model.route.DynRouteQueryParam;
import com.uyibai.gateway.admin.api.model.route.DynRouteSyncParam;
import com.uyibai.gateway.admin.api.model.route.DynRouteVo;


/**
 * 网关路由信息 同步 务逻辑 主要是 将路由信息 上线或者下线
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
public interface GatewayRouteRpcService {

    // 上线单个路由

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
     * 查看 某个网关节点 的路由信息
     *
     * @param nodeIp 节点 ip
     * @return 节点下的 路由信息
     */
    List<DynRouteVo> find(String nodeIp);


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
    List<DynRouteVo> list(DynRouteQueryParam queryParam);

    /**
     * 保存路由信息: 基础信息，filter,predicate
     *
     * @param routeVo 路由信息
     * @return 保存成功返回 路由信息，否则抛异常
     */
    DynRouteVo save(DynRouteVo routeVo);

    /**
     * 删除  路由
     *
     * @param id 路由id
     * @return 删除成功 返回 true， 否则false
     */
    void delete(Integer id);

    /**
     * 删除 路由 的某个 filter
     *
     * @param routeId  路由id
     * @param filterId filter id
     */
    void deleteFilter(Integer routeId, Integer filterId);

    /**
     * 删除 路由 的某个 predicate
     *
     * @param routeId     路由id
     * @param predicateId predicate id
     */
    void deletePredicate(Integer routeId, Integer predicateId);

}
