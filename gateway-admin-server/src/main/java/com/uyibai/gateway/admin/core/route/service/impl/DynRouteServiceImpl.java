package com.uyibai.gateway.admin.core.route.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uyibai.gateway.admin.api.model.definition.FilterDefinition;
import com.uyibai.gateway.admin.api.model.definition.GatewayRouteDefinition;
import com.uyibai.gateway.admin.api.model.definition.PredicateDefinition;
import com.uyibai.gateway.admin.api.model.route.DynRouteQueryParam;
import com.uyibai.gateway.admin.api.model.route.DynRouteSyncParam;
import com.uyibai.gateway.admin.api.model.route.DynRouteVo;
import com.uyibai.gateway.admin.config.GatewayAdminProperties;
import com.uyibai.gateway.admin.constant.RouteStatus;
import com.uyibai.gateway.admin.core.route.entity.DynRoute;
import com.uyibai.gateway.admin.core.route.entity.DynRouteFilter;
import com.uyibai.gateway.admin.core.route.entity.DynRoutePredicate;
import com.uyibai.gateway.admin.core.route.mapper.DynRouteMapper;
import com.uyibai.gateway.admin.core.route.publisher.GatewayRoutePublisher;
import com.uyibai.gateway.admin.core.route.service.DynRouteFilterService;
import com.uyibai.gateway.admin.core.route.service.DynRoutePredicateService;
import com.uyibai.gateway.admin.core.route.service.DynRouteService;
import com.uyibai.gateway.admin.exception.GatewayAdminException;
import com.uyibai.gateway.admin.exception.GatewayAdminExceptionCode;
import com.uyibai.gateway.common.exception.GatewayException;
import com.uyibai.gateway.common.spring.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 动态路由信息表 服务实现类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
@Service
@Slf4j
public class DynRouteServiceImpl extends ServiceImpl<DynRouteMapper, DynRoute> implements DynRouteService {

    @Resource
    GatewayAdminProperties gatewayAdminProperties;

    @Resource
    DynRouteFilterService dynRouteFilterService;

    @Resource
    DynRoutePredicateService dynRoutePredicateService;


    private GatewayRoutePublisher publisher;

    @PostConstruct
    public void init() {
        publisher = getPublisher();
    }



    @Override
    public boolean saveOrUpdate(DynRoute entity) {
        QueryWrapper<DynRoute> where = new QueryWrapper<>();
        where.eq(DynRoute.ROUTE_KEY, entity.getRouteKey());
        DynRoute oldRoute = getOne(where);
        if (oldRoute != null) {
            entity.setId(oldRoute.getId());
            return update(entity, where);
        }
        return save(entity);
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
        boolean ret = updateById(dynRoute);
        if (ret) {
            // 根据 配置的 动态路由管理方式 进行 发布
            DynRouteVo routeVo = queryById(dynRouteId);
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
        DynRouteVo route = queryById(dynRouteId);
        if (route == null) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.ROUTE_NOT_FIND);
        }
        DynRoute dynRoute = new DynRoute();
        dynRoute.setStatus(RouteStatus.STATUS_OFFLINE);
        dynRoute.setId(dynRouteId);
        boolean ret = updateById(dynRoute);
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
            routeVos = listRoutes(new DynRouteQueryParam());
        } else {
            DynRouteQueryParam queryParam = new DynRouteQueryParam();
            queryParam.setIds(syncParam.getRouteIds());
            routeVos = listRoutes(queryParam);
        }
        if (org.springframework.util.CollectionUtils.isEmpty(routeVos)) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.ROUTE_NOT_FIND);
        }
        // 根据 筛选和status条件 下线路由信息
        List<GatewayRouteDefinition> offlineDefinitions = routeVos.stream().filter(DynRouteVo::isOffline)
            .map(this::covertToDefinition).collect(Collectors.toList());
        if (!org.springframework.util.CollectionUtils.isEmpty(offlineDefinitions)) {
            publisher.offlineBatch(offlineDefinitions);
            log.info("GatewayRouteSync:offline>{},", offlineDefinitions.size());
        }
        // 根据 筛选和status条件 上线路由信息
        List<GatewayRouteDefinition> onlineDefinitions = routeVos.stream().filter(DynRouteVo::isOnline)
            .map(this::covertToDefinition).collect(Collectors.toList());
        if (!org.springframework.util.CollectionUtils.isEmpty(onlineDefinitions)) {
            publisher.publishBatch(onlineDefinitions);
            log.info("GatewayRouteSync:online>{}", onlineDefinitions.size());
        }
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
    public List<DynRouteVo> listRoutes(DynRouteQueryParam queryParam) {
        return baseMapper.selectByQueryParam(queryParam).stream().peek(item -> {
            item.setFilters(dynRouteFilterService.queryVoListByRouteId(item.getId()));
            item.setPredicates(dynRoutePredicateService.queryVoListByRouteId(item.getId()));
        }).collect(Collectors.toList());
    }

    /**
     * 查询 所有 路由 的基本信息
     *
     * @param queryParam 查询参数
     * @return
     */
    @Override
    public List<DynRouteVo> listSimpleInfo(DynRouteQueryParam queryParam) {
        return baseMapper.selectByQueryParam(queryParam);
    }

    /**
     * 保存路由信息: 基础信息，filter,predicate
     *
     * @param routeVo 路由信息
     * @return 保存成功返回 路由信息，否则抛异常
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, GatewayException.class})
    public DynRouteVo save(DynRouteVo routeVo) {
        DynRoute route = new DynRoute();
        BeanUtils.copyProperties(routeVo, route);
        List<DynRouteFilter> routeFilters = routeVo.getFilters().stream().map(filterVo -> {
            DynRouteFilter routeFilter = new DynRouteFilter();
            BeanUtils.copyProperties(filterVo, routeFilter);
            routeFilter.setRouteId(route.getId());
            return routeFilter;
        }).collect(Collectors.toList());

        List<DynRoutePredicate> routePredicates = routeVo.getPredicates().stream().map(predicateVo -> {
            DynRoutePredicate predicate = new DynRoutePredicate();
            BeanUtils.copyProperties(predicateVo, predicate);
            predicate.setRouteId(route.getId());
            return predicate;
        }).collect(Collectors.toList());

        // 校验 routeFilters 中的filterId 是否存在
        if (CollectionUtils.isNotEmpty(routeFilters) && routeFilters.stream().anyMatch(Objects::isNull)) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.ROUTE_FILTER_ID_ERROR);
        }
        // 校验 routePredicates 中的PredicateId 是否存在
        if (CollectionUtils.isNotEmpty(routePredicates) && routePredicates.stream().anyMatch(Objects::isNull)) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.ROUTE_PREDICATE_ID_ERROR);
        }

        // save DynRoute info
        boolean ret = this.saveOrUpdate(route);
        log.info("DynRouteSaveRest:{}", ret);
        if (ret && CollectionUtils.isNotEmpty(routeFilters)) {
            routeFilters.forEach(item -> {
                item.setRouteId(route.getId());
            });
            ret = dynRouteFilterService.saveOrUpdateBatch(routeFilters);
            log.info("dynRouteFilterSaveRest:{}", ret);
            if (!ret) {
                throw new GatewayAdminException(GatewayAdminExceptionCode.SAVE_ERROR);
            }
        }

        if (ret && CollectionUtils.isNotEmpty(routePredicates)) {
            routePredicates.forEach(item -> {
                item.setRouteId(route.getId());
            });
            ret = dynRoutePredicateService.saveOrUpdateBatch(routePredicates);
            log.info("dynRoutePredicateSaveRest:{}", ret);
            if (!ret) {
                throw new GatewayAdminException(GatewayAdminExceptionCode.SAVE_ERROR);
            }
        }

        return queryById(route.getId());
    }

    /**
     * 根据 路由id 查询单个路由信息
     *
     * @param routeId 路由id
     * @return 路由信息详情
     */
    @Override
    public DynRouteVo queryById(Integer routeId) {
        DynRouteVo routeVo = baseMapper.selectVoById(routeId);
        routeVo.setFilters(dynRouteFilterService.queryVoListByRouteId(routeId));
        routeVo.setPredicates(dynRoutePredicateService.queryVoListByRouteId(routeId));
        return routeVo;
    }

    /**
     * 查询 简单的 路由信息
     *
     * @param routeId
     * @return
     */
    @Override
    public DynRouteVo querySimpleById(Integer routeId) {
        return baseMapper.selectVoById(routeId);
    }

    /**
     * 删除  路由
     *
     * @param id 路由id
     * @return 删除成功 返回 true， 否则false
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, GatewayException.class})
    public Boolean delete(Integer id) {
        QueryWrapper<DynRoute> routeWhere = new QueryWrapper<>();
        routeWhere.eq(DynRoute.ID, id);
        routeWhere.eq(DynRoute.STATUS, RouteStatus.STATUS_ONLINE);
        if (count(routeWhere) > 0) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.ROUTE_USING_ERROR);
        }
        // 删除 route
        boolean ret = removeById(id);
        // 删除 filter
        if (ret) {
            QueryWrapper<DynRouteFilter> filterWhere = new QueryWrapper<>();
            filterWhere.eq(DynRouteFilter.ROUTE_ID, id);
            ret = dynRouteFilterService.remove(filterWhere);
        }
        if (ret) {
            // 删除 predicate
            QueryWrapper<DynRoutePredicate> predicateWhere = new QueryWrapper<>();
            predicateWhere.eq(DynRoutePredicate.ROUTE_ID, id);
            ret = dynRoutePredicateService.remove(predicateWhere);
        }
        if (!ret) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.DELETE_ERROR);
        }
        return true;
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
}
