package com.uyibai.gateway.admin.core.gateway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uyibai.gateway.admin.api.model.gateway.GatewayGroupVo;
import com.uyibai.gateway.admin.core.gateway.entity.GatewayGroup;

/**
 * <p>
 * 网关实例分组 Mapper 接口
 * </p>
 *
 * @author Wanghui
 * @since 2021-04-20
 */
public interface GatewayGroupMapper extends BaseMapper<GatewayGroup> {

    List<GatewayGroupVo> queryVoList(@Param("param") GatewayGroupVo param);

}
