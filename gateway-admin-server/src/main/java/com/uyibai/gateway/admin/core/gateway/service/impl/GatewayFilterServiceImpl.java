package com.uyibai.gateway.admin.core.gateway.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uyibai.gateway.admin.api.model.gateway.GatewayFilterVo;
import com.uyibai.gateway.admin.core.gateway.entity.GatewayFilter;
import com.uyibai.gateway.admin.core.gateway.mapper.GatewayFilterMapper;
import com.uyibai.gateway.admin.core.gateway.service.GatewayFilterService;
import com.uyibai.gateway.admin.core.route.entity.DynRouteFilter;
import com.uyibai.gateway.admin.core.route.mapper.DynRouteFilterMapper;
import com.uyibai.gateway.admin.exception.GatewayAdminException;
import com.uyibai.gateway.admin.exception.GatewayAdminExceptionCode;

/**
 * <p>
 * 网关支持的过滤器信息 服务实现类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
@Service
public class GatewayFilterServiceImpl extends ServiceImpl<GatewayFilterMapper, GatewayFilter> implements
    GatewayFilterService {

    @Resource
    DynRouteFilterMapper dynRouteFilterMapper;

    @Override
    public boolean saveOrUpdate(GatewayFilter entity) {
        QueryWrapper<GatewayFilter> where = new QueryWrapper<>();
        where.eq(GatewayFilter.FILTER_KEY, entity.getFilterKey());
        if (count(where) > 0) {
            return update(entity, where);
        }
        return save(entity);
    }

    /**
     * save filter info
     *
     * @param filterVo filter info
     * @return filter info
     */
    @Override
    public GatewayFilterVo saveWithVo(GatewayFilterVo filterVo) {
        GatewayFilter filter = new GatewayFilter();
        BeanUtils.copyProperties(filterVo, filter);
        boolean ret = this.saveOrUpdate(filter);
        if (!ret) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.SAVE_ERROR);
        }
        filterVo.setId(filter.getId());
        return filterVo;
    }

    /**
     * list all filter
     *
     * @param filterVo query param
     * @return filter list
     */
    @Override
    public List<GatewayFilterVo> listByVo(GatewayFilterVo filterVo) {
        return baseMapper.queryVoList(filterVo);
    }

    @Override
    public Boolean delete(Integer filterId) {
        QueryWrapper<DynRouteFilter> where = new QueryWrapper<>();
        where.eq(DynRouteFilter.FILTER_ID, filterId);
        if (dynRouteFilterMapper.selectCount(where) > 0) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.GATEWAY_FILTER_USING_ERROR);
        }
        return removeById(filterId);
    }
}
