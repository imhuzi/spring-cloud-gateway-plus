package com.uyibai.gateway.admin.core.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.uyibai.gateway.admin.api.model.definition.ArgsValueDefinition;
import com.uyibai.gateway.admin.api.model.route.DynRoutePredicateVo;
import com.uyibai.gateway.admin.api.model.route.DynRouteVo;
import com.uyibai.gateway.admin.constant.SDKType;
import com.uyibai.gateway.admin.core.app.entity.AppRegistry;
import com.uyibai.gateway.admin.core.app.entity.AppService;
import com.uyibai.gateway.admin.core.app.entity.Application;
import com.uyibai.gateway.admin.core.app.mapper.AppServiceMapper;
import com.uyibai.gateway.admin.core.app.service.AppRegistryService;
import com.uyibai.gateway.admin.core.app.service.AppServiceService;
import com.uyibai.gateway.admin.core.app.service.ApplicationService;
import com.uyibai.gateway.admin.core.app.vo.AppRegistryVo;
import com.uyibai.gateway.admin.core.app.vo.AppServiceDetailVo;
import com.uyibai.gateway.admin.core.app.vo.AppServiceVo;
import com.uyibai.gateway.admin.exception.GatewayAdminException;
import com.uyibai.gateway.admin.exception.GatewayAdminExceptionCode;
import com.uyibai.gateway.common.utils.JSONUtils;
import com.uyibai.gateway.common.utils.Tool;


/**
 * <p>
 * Dubbo 服务信息 服务实现类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-28
 */
@Service
public class AppServiceServiceImpl extends ServiceImpl<AppServiceMapper, AppService> implements AppServiceService {

    @Resource
    ApplicationService applicationService;

    @Resource
    AppRegistryService appRegistryService;

    private final Gson gson = new Gson();


    @Override
    public boolean saveOrUpdate(AppService entity) {
        QueryWrapper<AppService> where = new QueryWrapper<>();
        where.eq(AppService.SERVICE, entity.getService());
        where.eq(AppService.APP_ID, entity.getAppId());
        where.select(AppService.ID, AppService.REGISTRY_IDS);
        AppService old = getOne(where);
        if (old != null) {
            entity.setId(old.getId());
            Set<Integer> ids = new HashSet<>();
            if (old.getRegistryIds() != null) {
                ids.addAll(old.getRegistryIds());
            }
            ids.addAll(entity.getRegistryIds());
            entity.setRegistryIds(ids);
            return update(entity, where);
        }
        return save(entity);
    }

    /**
     * 批量 保存 服务信息
     *
     * @param serviceVoSet
     * @return
     */
    @Override
    @Transactional
    public Boolean saveBatch(Set<AppServiceVo> serviceVoSet) {
        for (AppServiceVo item : serviceVoSet) {
            // collector app info
            Application app = new Application();
            app.setAppId(item.getApplication());
            app.setAppType(SDKType.SDK_TYPE_DUBBO);
            app.setAddress(item.getAddress());
            app.setAppName(item.getApplication());
            boolean ret = applicationService.saveOrUpdate(app);

            // collector app registry
            AppRegistry registry = new AppRegistry();
            if (ret) {
                BeanUtils.copyProperties(item.getRegistry(), registry);
                registry.setName(registry.getRegistryKey());
                ret = appRegistryService.saveOrUpdate(registry);
            }
            // collector app service
            if (ret) {
                AppService appService = new AppService();
                appService.setAppId(app.getId());
                appService.setGroup(item.getGroup());
                appService.setMeta(item.getMeta());
                appService.setService(item.getService());
                appService.setProtocol(item.getProtocol());
                appService.setVersion(item.getVersion());
                appService.setPath(item.getPath());
                appService.setSdkType(SDKType.SDK_TYPE_DUBBO);
                appService.setSdkVersion(item.getSdkVersion());

                Set<Integer> registryIds = new HashSet<>();
                registryIds.add(registry.getId());
                appService.setRegistryIds(registryIds);
                ret = saveOrUpdate(appService);
                if (!ret) {
                    throw new GatewayAdminException(GatewayAdminExceptionCode.APP_SERVICE_SAVE_ERROR);
                }

            }

        }
        return true;
    }

    /**
     * 将 所有dubbo服务转为网关路由
     *
     * @param serviceId
     * @return
     */
    @Override
    public DynRouteVo getRouteByService(Integer serviceId) {
        AppServiceDetailVo detailVo = getAppServiceDetail(serviceId);
        String serviceInterfaceShortName = Tool.getInterfaceShortName(detailVo.getService());
        DynRouteVo dynRouteVo = new DynRouteVo();
        dynRouteVo.setAppId(detailVo.getAppId());
        dynRouteVo.setServiceId(detailVo.getId());
        dynRouteVo.setName(StringUtils.defaultString(detailVo.getName(), serviceInterfaceShortName) + "路由");
        dynRouteVo.setGroupId(0);
        // serviceType + service+ route
        // DubboUserServiceRoute
        String routeKey = detailVo.getSdkType() + serviceInterfaceShortName + "Route";
        dynRouteVo.setRouteKey(routeKey);
        // dubbo://{appId}/{serviceInterface}/{version}
        dynRouteVo.setRouteUri(detailVo.getProtocol() + "://" + detailVo.getApp() + "/" + detailVo.getService());
        dynRouteVo.setRouteType(detailVo.getSdkType());

        // 路由规则
        List<DynRoutePredicateVo> predicateVos = new ArrayList<>();
        DynRoutePredicateVo predicateVo = new DynRoutePredicateVo();
        predicateVo.setPredicateKey("Path");

        List<ArgsValueDefinition> argsValueList = new ArrayList<>();
        ArgsValueDefinition routeRuleValue = new ArgsValueDefinition();
        routeRuleValue.setArgsKey("patterns");
        // 应用 uri前缀 + service uri 前缀(如果未设置默认按Tool.getPath规则处理)
        String path = Tool.getPath(detailVo.getAppUriPrefix(), detailVo.getRouteUriPrefix(), detailVo.getPath());
        routeRuleValue.setValue(path);
        argsValueList.add(routeRuleValue);
        predicateVo.setArgsValue(argsValueList);
        predicateVos.add(predicateVo);
        dynRouteVo.setPredicates(predicateVos);

        HashMap<String, Object> meta = new HashMap<>();
        // 注册 中心 信息
        meta.put("registrys", JSONUtils.toStr(detailVo.getRegistrys()));
        meta.put("appPath", detailVo.getAppUriPrefix());
        meta.put("servicePath", detailVo.getRouteUriPrefix());
        // service 定义信息
        String metaStr = detailVo.getMeta();
        if (StringUtils.isNotBlank(metaStr)) {

//            try {
                // for dubbo version under 2.7, this metadata will represent as IP address, like 10.0.0.1.
                // So the json conversion will fail.
//                HashMap<String,Object> fullServiceDefinition = JSONUtils.toMap(metaStr, Object.class);
//                meta.put("methods", fullServiceDefinition.get("methods"));
//            } catch (JsonParseException e) {
//                throw new VersionValidationException("dubbo 2.6 does not support metadata");
//            }
        }

        dynRouteVo.setMetadata(meta);
        dynRouteVo.setStatus(0);
        dynRouteVo.setOrderNum(0);
        return dynRouteVo;
    }

    /**
     * 获取 servie 详情
     *
     * @param serviceId
     * @return
     */
    @Override
    public AppServiceDetailVo getAppServiceDetail(Integer serviceId) {
        AppServiceDetailVo detailVo = baseMapper.selectDetail(serviceId);
        if (CollectionUtils.isNotEmpty(detailVo.getRegistryIds())) {
            Set<AppRegistryVo> registryList = appRegistryService.listByIds(detailVo.getRegistryIds())
                    .stream().map(item -> {
                        AppRegistryVo vo = new AppRegistryVo();
                        vo.setRegistryKey(item.getRegistryKey());
                        vo.setAddress(item.getAddress());
                        vo.setPassword(item.getPassword());
                        vo.setUsername(item.getUsername());
                        vo.setParameters(item.getParameters());
                        return vo;
                    }).collect(Collectors.toSet());
            detailVo.setRegistrys(registryList);
        }
        return detailVo;
    }

    /**
     * 将 所有 service 转为路由
     */
    @Override
    public void covertRouteAll() {
        // todo
        List<AppServiceDetailVo> serviceDetailVoList = new ArrayList<>();

    }
}
