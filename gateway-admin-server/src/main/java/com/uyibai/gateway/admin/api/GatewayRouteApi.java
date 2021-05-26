package com.uyibai.gateway.admin.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.uyibai.gateway.admin.api.model.definition.FilterDefinition;
import com.uyibai.gateway.admin.api.model.definition.GatewayRouteDefinition;
import com.uyibai.gateway.admin.api.model.definition.PredicateDefinition;
import com.uyibai.gateway.admin.api.model.route.DynRouteQueryParam;
import com.uyibai.gateway.admin.api.model.route.DynRouteSyncParam;
import com.uyibai.gateway.admin.api.model.route.DynRouteVo;
import com.uyibai.gateway.admin.api.service.GatewayRouteRpcService;
import com.uyibai.gateway.admin.config.GatewayAdminProperties;
import com.uyibai.gateway.admin.constant.RouteStatus;
import com.uyibai.gateway.admin.core.route.entity.DynRoute;
import com.uyibai.gateway.admin.core.route.publisher.GatewayRoutePublisher;
import com.uyibai.gateway.admin.core.route.service.DynRouteFilterService;
import com.uyibai.gateway.admin.core.route.service.DynRoutePredicateService;
import com.uyibai.gateway.admin.core.route.service.DynRouteService;
import com.uyibai.gateway.admin.exception.GatewayAdminException;
import com.uyibai.gateway.admin.exception.GatewayAdminExceptionCode;
import com.uyibai.gateway.common.exception.GatewayException;
import com.uyibai.gateway.common.spring.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GatewayRouteApi implements GatewayRouteRpcService {

  @Resource
  GatewayAdminProperties gatewayAdminProperties;

  @Resource
  DynRouteService dynRouteService;

  @Resource
  DynRouteFilterService dynRouteFilterService;

  @Resource
  DynRoutePredicateService dynRoutePredicateService;


  private GatewayRoutePublisher publisher;

  @PostConstruct
  public void init() {
    publisher = getPublisher();
  }

  /**
   * 发布 上线 单个路由 信息
   * <p>
   * 如果失败 抛异常
   *
   * @param dynRouteId 路由id
   */
  @Override
  @Transactional(rollbackFor = {Exception.class, GatewayException.class})
  public void online(Integer dynRouteId) {
    // 修改 发布 状态
    DynRoute dynRoute = new DynRoute();
    dynRoute.setStatus(RouteStatus.STATUS_ONLINE);
    dynRoute.setId(dynRouteId);
    boolean ret = dynRouteService.updateById(dynRoute);
    if (ret) {
      // 根据 配置的 动态路由管理方式 进行 发布
      DynRouteVo routeVo = dynRouteService.queryById(dynRouteId);
      log.info("GatewayRouteOnline:{}", routeVo);
      publisher.publishOne(covertToDefinition(routeVo));
    }
  }

  /**
   * 下线单个路由
   *
   * @param dynRouteId 路由id
   */
  @Override
  @Transactional(rollbackFor = {Exception.class, GatewayException.class})
  public void offline(Integer dynRouteId) {
    DynRouteVo route = dynRouteService.queryById(dynRouteId);
    if (route == null) {
      throw new GatewayAdminException(GatewayAdminExceptionCode.ROUTE_NOT_FIND);
    }
    DynRoute dynRoute = new DynRoute();
    dynRoute.setStatus(RouteStatus.STATUS_OFFLINE);
    dynRoute.setId(dynRouteId);
    boolean ret = dynRouteService.updateById(dynRoute);
    if (ret) {
      log.info("offline:{},{}", route.getGroupKey(), route.getRouteKey());
      publisher.offline(route.getGroupKey(), route.getRouteKey());
    }
  }

  /**
   * 动态路由 同步
   * <p>
   * 1. 支持 全量 或者指定 路由 同步 到网关
   * 2. 支持 全量 或者 指定 节点 同步
   *
   * @param syncParam 同步参数
   */
  @Override
  @Transactional(rollbackFor = {Exception.class, GatewayException.class})
  public void sync(DynRouteSyncParam syncParam) {
    // 全量
    List<DynRouteVo> routeVos;
    if (syncParam.isAllRoute()) {
      routeVos = dynRouteService.listRoutes(new DynRouteQueryParam());
    } else {
      DynRouteQueryParam queryParam = new DynRouteQueryParam();
      queryParam.setIds(syncParam.getRouteIds());
      routeVos = dynRouteService.listRoutes(queryParam);
    }
    if (CollectionUtils.isEmpty(routeVos)) {
      throw new GatewayAdminException(GatewayAdminExceptionCode.ROUTE_NOT_FIND);
    }
    // 根据 筛选和status条件 下线路由信息
    List<GatewayRouteDefinition> offlineDefinitions = routeVos.stream().filter(DynRouteVo::isOffline)
        .map(this::covertToDefinition).collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(offlineDefinitions)) {
      publisher.offlineBatch(offlineDefinitions);
      log.info("GatewayRouteSync:offline>{},", offlineDefinitions.size());
    }
    // 根据 筛选和status条件 上线路由信息
    List<GatewayRouteDefinition> onlineDefinitions = routeVos.stream().filter(DynRouteVo::isOnline)
        .map(this::covertToDefinition).collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(onlineDefinitions)) {
      publisher.publishBatch(onlineDefinitions);
      log.info("GatewayRouteSync:online>{}", onlineDefinitions.size());
    }
  }

  /**
   * 查看 某个网关节点 的路由信息
   *
   * @param nodeIp 节点 ip
   * @return 节点下的 路由信息
   */
  @Override
  public List<DynRouteVo> find(String nodeIp) {
    // todo 未实现
    return null;
  }

  /**
   * 获取 动态路由发布者
   *
   * @return
   */
  private GatewayRoutePublisher getPublisher() {
    String beanName = gatewayAdminProperties.getPublishType() + "GatewayRoutePublisher";
    return SpringContextUtil.getBean(beanName, GatewayRoutePublisher.class);
  }

  private GatewayRouteDefinition covertToDefinition(DynRouteVo routeVo) {
    GatewayRouteDefinition definition = new GatewayRouteDefinition();
    definition.setId(routeVo.getRouteKey());
    definition.setUri(routeVo.getRouteUri());
    definition.setMetadata(routeVo.getMetadata());
    definition.setPredicates(routeVo.getPredicates().stream().map(item -> {
      PredicateDefinition predicate = new PredicateDefinition();
      predicate.setName(item.getPredicateKey());
      predicate.setArgs(item.getArgsMap());
      return predicate;
    }).collect(Collectors.toList()));

    definition.setFilters(routeVo.getFilters().stream().map(item -> {
      FilterDefinition predicate = new FilterDefinition();
      predicate.setName(item.getFilterKey());
      predicate.setArgs(item.getArgsMap());
      return predicate;
    }).collect(Collectors.toList()));

    return definition;
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
  @Override
  public List<DynRouteVo> list(DynRouteQueryParam queryParam) {
    return dynRouteService.listRoutes(queryParam);
  }

  /**
   * 保存路由信息: 基础信息，filter,predicate
   *
   * @param routeVo 路由信息
   * @return 保存成功返回 路由信息，否则抛异常
   */
  @Override
  public DynRouteVo save(DynRouteVo routeVo) {
    return dynRouteService.save(routeVo);
  }

  /**
   * 删除  路由
   *
   * @param id 路由id
   * @return 删除成功 返回 true， 否则false
   */
  @Override
  public void delete(Integer id) {
    dynRouteService.delete(id);
  }

  /**
   * 删除 路由 的某个 filter
   *
   * @param routeId  路由id
   * @param filterId filter id
   */
  @Override
  public void deleteFilter(Integer routeId, Integer filterId) {
    dynRouteFilterService.delete(routeId, filterId);
  }

  /**
   * 删除 路由 的某个 predicate
   *
   * @param routeId     路由id
   * @param predicateId predicate id
   */
  @Override
  public void deletePredicate(Integer routeId, Integer predicateId) {
    dynRoutePredicateService.delete(routeId, predicateId);
  }
}
