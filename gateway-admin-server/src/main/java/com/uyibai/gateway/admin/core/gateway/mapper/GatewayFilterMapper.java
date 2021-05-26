package com.uyibai.gateway.admin.core.gateway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uyibai.gateway.admin.api.model.gateway.GatewayFilterVo;
import com.uyibai.gateway.admin.core.gateway.entity.GatewayFilter;

/**
 * <p>
 * 网关支持的过滤器信息 Mapper 接口
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface GatewayFilterMapper extends BaseMapper<GatewayFilter> {

    List<GatewayFilterVo> queryVoList(@Param("param") GatewayFilterVo param);

}
