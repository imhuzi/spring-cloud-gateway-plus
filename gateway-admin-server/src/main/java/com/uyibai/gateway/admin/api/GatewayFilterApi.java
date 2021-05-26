package com.uyibai.gateway.admin.api;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import com.uyibai.gateway.admin.api.model.gateway.GatewayFilterVo;
import com.uyibai.gateway.admin.api.service.GatewayFilterRpcService;
import com.uyibai.gateway.admin.core.gateway.service.GatewayFilterService;

/**
 * 网关 过滤器管理 接口
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
@RestController
public class GatewayFilterApi implements GatewayFilterRpcService {

  private static final String PATH_PREFIX = "/filters";

  @Resource
  GatewayFilterService gatewayFilterService;

  /**
   * save filter info
   *
   * @param filterVo filter info
   * @return filter info
   */
  @Override
  public GatewayFilterVo save(GatewayFilterVo filterVo) {
    return gatewayFilterService.saveWithVo(filterVo);
  }

  /**
   * delete filter  by id
   *
   * @param filterId 过滤器id
   * @return if success return true Otherwise false
   */
  @Override
  public Boolean delete(Integer filterId) {
    return gatewayFilterService.delete(filterId);
  }

  /**
   * list all filter
   *
   * @param filterVo query param
   * @return filter list
   */
  @Override
  public List<GatewayFilterVo> list(GatewayFilterVo filterVo) {
    return gatewayFilterService.listByVo(filterVo);
  }
}
