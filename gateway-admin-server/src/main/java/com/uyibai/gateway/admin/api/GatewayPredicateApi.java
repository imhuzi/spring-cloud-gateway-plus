package com.uyibai.gateway.admin.api;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uyibai.gateway.admin.api.model.gateway.GatewayRoutePredicateVo;
import com.uyibai.gateway.admin.api.service.GatewayPredicateRpcService;
import com.uyibai.gateway.admin.core.gateway.service.GatewayFilterService;
import com.uyibai.gateway.admin.core.gateway.service.GatewayGroupService;
import com.uyibai.gateway.admin.core.gateway.service.GatewayRoutePredicateService;

@RestController
public class GatewayPredicateApi implements GatewayPredicateRpcService {

    private static final String PATH_PREFIX = "/predicates";

    @Resource
    GatewayFilterService gatewayFilterService;

    @Resource
    GatewayRoutePredicateService gatewayRoutePredicateService;

    @Resource
    GatewayGroupService gatewayGroupService;

    /**
     * save Predicate info
     *
     * @param PredicateVo Predicate info
     * @return Predicate info
     */
    @Override
    @PostMapping(path = PATH_PREFIX)
    public GatewayRoutePredicateVo save(GatewayRoutePredicateVo PredicateVo) {
        return gatewayRoutePredicateService.saveWithVo(PredicateVo);
    }

    /**
     * delete Predicate  by id
     *
     * @param predicateId predicate id
     * @return if success return true Otherwise false
     */
    @Override
    @DeleteMapping(path = PATH_PREFIX)
    public Boolean delete(Integer predicateId) {
        return gatewayRoutePredicateService.delete(predicateId);
    }

    /**
     * list all Predicate
     *
     * @param predicateVo query param
     * @return Predicate list
     */
    @Override
    @GetMapping(path = PATH_PREFIX)
    public List<GatewayRoutePredicateVo> list(GatewayRoutePredicateVo predicateVo) {
        return gatewayRoutePredicateService.listByVo(predicateVo);
    }
}
