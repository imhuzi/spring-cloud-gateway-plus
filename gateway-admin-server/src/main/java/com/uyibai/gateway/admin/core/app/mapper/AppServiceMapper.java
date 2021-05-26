package com.uyibai.gateway.admin.core.app.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uyibai.gateway.admin.core.app.entity.AppService;
import com.uyibai.gateway.admin.core.app.vo.AppServiceDetailVo;

/**
 * <p>
 * Dubbo 服务信息 Mapper 接口
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-28
 */
public interface AppServiceMapper extends BaseMapper<AppService> {

    AppServiceDetailVo selectDetail(@Param("serviceId") Integer serviceId);
}
