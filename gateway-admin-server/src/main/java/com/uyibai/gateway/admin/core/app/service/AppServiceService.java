package com.uyibai.gateway.admin.core.app.service;

import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uyibai.gateway.admin.api.model.route.DynRouteVo;
import com.uyibai.gateway.admin.core.app.entity.AppService;
import com.uyibai.gateway.admin.core.app.vo.AppServiceDetailVo;
import com.uyibai.gateway.admin.core.app.vo.AppServiceVo;

/**
 * <p>
 * Dubbo 服务信息 服务类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-28
 */
public interface AppServiceService extends IService<AppService> {
    /**
     * 批量 保存 服务信息
     *
     * @param serviceVoSet
     * @return
     */
    Boolean saveBatch(Set<AppServiceVo> serviceVoSet);

    /**
     * 将 所有dubbo服务转为网关路由
     *
     * @param serviceId
     * @return
     */
    DynRouteVo getRouteByService(Integer serviceId);

    /**
     * 获取 servie 详情
     *
     * @param serviceId
     * @return
     */
    AppServiceDetailVo getAppServiceDetail(Integer serviceId);

    /**
     * 将 所有 service 转为路由
     */
    void covertRouteAll();
}
