package com.uyibai.gateway.admin.core.route.service.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uyibai.gateway.admin.api.model.route.DynRoutePredicateVo;
import com.uyibai.gateway.admin.constant.RouteStatus;
import com.uyibai.gateway.admin.core.route.entity.DynRoute;
import com.uyibai.gateway.admin.core.route.entity.DynRoutePredicate;
import com.uyibai.gateway.admin.core.route.mapper.DynRouteMapper;
import com.uyibai.gateway.admin.core.route.mapper.DynRoutePredicateMapper;
import com.uyibai.gateway.admin.core.route.service.DynRoutePredicateService;
import com.uyibai.gateway.admin.exception.GatewayAdminException;
import com.uyibai.gateway.admin.exception.GatewayAdminExceptionCode;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 动态路由规则表 服务实现类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
@Service
@Slf4j
public class DynRoutePredicateServiceImpl extends ServiceImpl<DynRoutePredicateMapper, DynRoutePredicate> implements
    DynRoutePredicateService {

    @Resource
    DynRouteMapper dynRouteMapper;

    @Override
    public boolean saveOrUpdate(DynRoutePredicate entity) {
        QueryWrapper<DynRoutePredicate> where = new QueryWrapper<>();
        where.eq(DynRoutePredicate.ROUTE_ID, entity.getRouteId());
        where.eq(DynRoutePredicate.PREDICATE_KEY, entity.getPredicateKey());
        if (count(where) > 0) {
            return update(entity, where);
        }
        return save(entity);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<DynRoutePredicate> entityList, int batchSize) {
        if (entityList.isEmpty()) {
            return false;
        }
        log.info("saveOrUpdateBatch:{}", entityList);
        boolean ret = false;
        for (DynRoutePredicate entity : entityList) {
            ret = saveOrUpdate(entity);
            if (!ret) {
                // 抛出异常
                throw new GatewayAdminException(GatewayAdminExceptionCode.UPDATE_ERROR);
            }
        }
        return ret;
    }

    /**
     * 查询 路由关联的所有 filter信息
     *
     * @param routeId 路由id
     * @return list DynRouteFilterVo
     */
    @Override
    public List<DynRoutePredicate> queryByRouteId(Integer routeId) {
        QueryWrapper<DynRoutePredicate> where = new QueryWrapper<>();
        where.eq(DynRoutePredicate.ROUTE_ID, routeId);
        return list(where);
    }

    @Override
    public List<DynRoutePredicateVo> queryVoListByRouteId(Integer routeId) {
        return baseMapper.queryVoListByRouteId(routeId);
    }

    @Override
    public void delete(Integer routeId, Integer predicateId) {
        QueryWrapper<DynRoute> routeWhere = new QueryWrapper<>();
        routeWhere.eq(DynRoute.ID, routeId);
        routeWhere.eq(DynRoute.STATUS, RouteStatus.STATUS_ONLINE);
        if (dynRouteMapper.selectCount(routeWhere) > 0) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.ROUTE_USING_ERROR);
        }
        QueryWrapper<DynRoutePredicate> delWhere = new QueryWrapper<>();
        delWhere.eq(DynRoutePredicate.ROUTE_ID, routeId);
        delWhere.eq(DynRoutePredicate.PREDICATE_ID, predicateId);
        boolean ret = remove(delWhere);
        if (!ret) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.DELETE_ERROR);
        }
    }
}
