package com.uyibai.gateway.admin.core.gateway.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uyibai.gateway.admin.api.model.gateway.GatewayGroupVo;
import com.uyibai.gateway.admin.core.gateway.entity.GatewayGroup;
import com.uyibai.gateway.admin.core.gateway.mapper.GatewayGroupMapper;
import com.uyibai.gateway.admin.core.gateway.service.GatewayGroupService;
import com.uyibai.gateway.admin.core.route.entity.DynRoute;
import com.uyibai.gateway.admin.core.route.mapper.DynRouteMapper;
import com.uyibai.gateway.admin.exception.GatewayAdminException;
import com.uyibai.gateway.admin.exception.GatewayAdminExceptionCode;

/**
 * <p>
 * 网关实例分组 服务实现类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
@Service
public class GatewayGroupServiceImpl extends ServiceImpl<GatewayGroupMapper, GatewayGroup> implements
    GatewayGroupService {

    @Resource
    DynRouteMapper dynRouteMapper;


    @Override
    public boolean saveOrUpdate(GatewayGroup entity) {
        QueryWrapper<GatewayGroup> where = new QueryWrapper<>();
        where.eq(GatewayGroup.GROUP_KEY, entity.getGroupKey());
        if (count(where) > 0) {
            return update(entity, where);
        }
        return save(entity);
    }


    @Override
    public GatewayGroupVo save(GatewayGroupVo groupVo) {
        GatewayGroup group = new GatewayGroup();
        BeanUtils.copyProperties(groupVo, group);
        boolean ret = this.saveOrUpdate(group);
        if (!ret) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.SAVE_ERROR);
        }
        groupVo.setId(group.getId());
        groupVo.setCreatedAt(group.getCreatedAt());
        return groupVo;
    }

    @Override
    public Boolean delete(Integer groupId) {
        // 检查是否 有 route 在使用
        QueryWrapper<DynRoute> where = new QueryWrapper<>();
        where.eq(DynRoute.GROUP_ID, groupId);
        if (dynRouteMapper.selectCount(where) > 0) {
            throw new GatewayAdminException(GatewayAdminExceptionCode.GROUP_USING_ERROR);
        }
        return removeById(groupId);
    }

    @Override
    public List<GatewayGroupVo> listByVo(GatewayGroupVo groupVo) {
        return baseMapper.queryVoList(groupVo);
    }
}
