package com.uyibai.gateway.admin.core.gateway.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uyibai.gateway.admin.api.model.gateway.GatewayRoutePredicateVo;
import com.uyibai.gateway.admin.core.gateway.entity.GatewayRoutePredicate;
import com.uyibai.gateway.admin.core.gateway.mapper.GatewayRoutePredicateMapper;
import com.uyibai.gateway.admin.core.gateway.service.GatewayRoutePredicateService;
import com.uyibai.gateway.admin.core.route.entity.DynRoutePredicate;
import com.uyibai.gateway.admin.core.route.mapper.DynRoutePredicateMapper;
import com.uyibai.gateway.admin.exception.GatewayAdminException;
import com.uyibai.gateway.admin.exception.GatewayAdminExceptionCode;

/**
 * <p>
 * 网关支持的路由断言信息 服务实现类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
@Service
public class GatewayRoutePredicateServiceImpl extends ServiceImpl<GatewayRoutePredicateMapper, GatewayRoutePredicate> implements
    GatewayRoutePredicateService {

    @Resource
    DynRoutePredicateMapper dynRoutePredicateMapper;

    @Override
    public boolean saveOrUpdate(GatewayRoutePredicate entity) {
        QueryWrapper<GatewayRoutePredicate> where = new QueryWrapper<>();
        where.eq(GatewayRoutePredicate.PREDICATE_KEY, entity.getPredicateKey());
        if (count(where) > 0) {
            return update(entity, where);
        }
        return save(entity);
    }

    /**
     * save Predicate info
     *
     * @param PredicateVo Predicate info
     * @return Predicate info
     */
    @Override
    public GatewayRoutePredicateVo saveWithVo(GatewayRoutePredicateVo PredicateVo) {
        GatewayRoutePredicate Predicate = new GatewayRoutePredicate();
        BeanUtils.copyProperties(PredicateVo, Predicate);
        boolean ret = saveOrUpdate(Predicate);
        if (!ret) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.SAVE_ERROR);
        }
        PredicateVo.setId(Predicate.getId());
        return PredicateVo;
    }

    /**
     * list all Predicate
     *
     * @param predicateVo query param
     * @return Predicate list
     */
    @Override
    public List<GatewayRoutePredicateVo> listByVo(GatewayRoutePredicateVo predicateVo) {
        return baseMapper.queryVoList(predicateVo);
    }

    @Override
    public Boolean delete(Integer predicateId) {
        QueryWrapper<DynRoutePredicate> where = new QueryWrapper<>();
        where.eq(DynRoutePredicate.PREDICATE_ID, predicateId);
        if (dynRoutePredicateMapper.selectCount(where) > 0) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.GATEWAY_ROUTE_PREDICATE_USING_ERROR);
        }
        return removeById(predicateId);
    }
}
