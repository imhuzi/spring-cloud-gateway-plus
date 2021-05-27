package com.uyibai.gateway.admin.api;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uyibai.gateway.admin.api.model.route.DynRouteQueryParam;
import com.uyibai.gateway.admin.api.model.route.DynRouteSyncParam;
import com.uyibai.gateway.admin.api.model.route.DynRouteVo;
import com.uyibai.gateway.admin.core.route.service.DynRouteFilterService;
import com.uyibai.gateway.admin.core.route.service.DynRoutePredicateService;
import com.uyibai.gateway.admin.core.route.service.DynRouteService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class GatewayRouteApi {

  private static final String PATH_PREFIX = "/routes";


  @Resource
  DynRouteService dynRouteService;

  @Resource
  DynRouteFilterService dynRouteFilterService;

  @Resource
  DynRoutePredicateService dynRoutePredicateService;

  /**
   * 发布 上线 单个路由 信息
   * <p>
   * 如果失败 抛异常
   *
   * @param dynRouteId 路由id
   */
  @PostMapping(path = PATH_PREFIX + "/online")
  public void online(Integer dynRouteId) {
    // 根据 配置的 动态路由管理方式 进行 发布
    dynRouteService.online(dynRouteId);
  }

  /**
   * 下线单个路由
   *
   * @param dynRouteId 路由id
   */
  @PostMapping(path = PATH_PREFIX + "/offline")
  public void offline(Integer dynRouteId) {
    dynRouteService.offline(dynRouteId);
  }

  /**
   * 动态路由 同步
   * <p>
   * 1. 支持 全量 或者指定 路由 同步 到网关
   * 2. 支持 全量 或者 指定 节点 同步
   *
   * @param syncParam 同步参数
   */
  @PostMapping(path = PATH_PREFIX + "/sync")
  public void sync(DynRouteSyncParam syncParam) {
      dynRouteService.sync(syncParam);
  }

  /**
   * 查看 某个网关节点 的路由信息
   *
   * @param nodeIp 节点 ip
   * @return 节点下的 路由信息
   */
  @GetMapping(path = PATH_PREFIX + "/node")
  public List<DynRouteVo> find(String nodeIp) {
    // todo 未实现
    return null;
  }

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
  @GetMapping(path = PATH_PREFIX)
  public List<DynRouteVo> list(DynRouteQueryParam queryParam) {
    return dynRouteService.listRoutes(queryParam);
  }

  /**
   * 保存路由信息: 基础信息，filter,predicate
   *
   * @param routeVo 路由信息
   * @return 保存成功返回 路由信息，否则抛异常
   */
  @PostMapping(path = PATH_PREFIX)
  public DynRouteVo save(DynRouteVo routeVo) {
    return dynRouteService.save(routeVo);
  }

  /**
   * 删除  路由
   *
   * @param id 路由id
   * @return 删除成功 返回 true， 否则false
   */
  @DeleteMapping(path = PATH_PREFIX)
  public void delete(Integer id) {
    dynRouteService.delete(id);
  }

  /**
   * 删除 路由 的某个 filter
   *
   * @param routeId  路由id
   * @param filterId filter id
   */
  @DeleteMapping(path = PATH_PREFIX + "/filter")
  public void deleteFilter(Integer routeId, Integer filterId) {
    dynRouteFilterService.delete(routeId, filterId);
  }

  /**
   * 删除 路由 的某个 predicate
   *
   * @param routeId     路由id
   * @param predicateId predicate id
   */
  @DeleteMapping(path = PATH_PREFIX + "/predicate")
  public void deletePredicate(Integer routeId, Integer predicateId) {
    dynRoutePredicateService.delete(routeId, predicateId);
  }
}
