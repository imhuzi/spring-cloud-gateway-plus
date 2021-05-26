package com.uyibai.gateway.admin.core.app.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uyibai.gateway.admin.core.app.entity.Application;
import com.uyibai.gateway.admin.core.app.mapper.ApplicationMapper;
import com.uyibai.gateway.admin.core.app.service.ApplicationService;

/**
 * <p>
 * 应用信息(以项目维度) 服务实现类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-28
 */
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements ApplicationService {

    @Override
    @Transactional
    public boolean saveOrUpdate(Application entity) {
        QueryWrapper<Application> where = new QueryWrapper<>();
        where.eq(Application.APP_ID, entity.getAppId());
        Application old = getOne(where);
        if (old != null) {
            entity.setId(old.getId());
            // 合并 instance
            Set<String> instances = new HashSet<>();
            if (old.getInstances() != null) {
                instances.addAll(old.getInstances());
            }
            instances.add(entity.getAddress());
            entity.setInstances(instances);
            return update(entity, where);
        }
        return save(entity);
    }
}
