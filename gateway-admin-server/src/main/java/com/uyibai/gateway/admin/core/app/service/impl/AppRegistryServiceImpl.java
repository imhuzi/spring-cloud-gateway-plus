package com.uyibai.gateway.admin.core.app.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uyibai.gateway.admin.core.app.entity.AppRegistry;
import com.uyibai.gateway.admin.core.app.mapper.AppRegistryMapper;
import com.uyibai.gateway.admin.core.app.service.AppRegistryService;

/**
 * <p>
 * Dubbo 注册中心配置 服务实现类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-28
 */
@Service
public class AppRegistryServiceImpl extends ServiceImpl<AppRegistryMapper, AppRegistry> implements AppRegistryService {

    @Override
    public boolean saveOrUpdate(AppRegistry entity) {
        QueryWrapper<AppRegistry> where = new QueryWrapper<>();
        where.eq(AppRegistry.REGISTRY_KEY, entity.getRegistryKey());
        AppRegistry old = getOne(where);
        if (old != null) {
            entity.setId(old.getId());
            return update(entity, where);
        }
        return save(entity);

    }
}
