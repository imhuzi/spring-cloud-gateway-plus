package com.uyibai.gateway.admin.core.gateway.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uyibai.gateway.admin.api.model.gateway.GatewayFilterVo;
import com.uyibai.gateway.admin.core.gateway.entity.GatewayFilter;

/**
 * <p>
 * 网关支持的过滤器信息 服务类
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface GatewayFilterService extends IService<GatewayFilter> {

    /**
     * save filter info
     *
     * @param filterVo filter info
     * @return filter info
     */
    GatewayFilterVo saveWithVo(GatewayFilterVo filterVo);

    /**
     * list all filter
     *
     * @param filterVo query param
     * @return filter list
     */
    List<GatewayFilterVo> listByVo(GatewayFilterVo filterVo);

    Boolean delete(Integer filterId);
}
