package com.uyibai.gateway.admin.api.model.route;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
@Data
public class DynRouteQueryParam implements Serializable {

  private Integer id;

  /**
   * 路由名称
   */
  private String name;

  /**
   * 路由id(可读性的字符串)
   */
  private String routeKey;

  /**
   * 路由类型(http,lb,dubbo,grpc)
   */
  private String routeType;

  /**
   * 分组id
   */
  private Integer groupId;

  /**
   * 状态(0,未启用,1 启用)
   */
  private Integer status;

  /**
   * 过滤器id
   */
  private Integer filterId;

  /**
   * 路由规则id
   */
  private Integer predicateId;

  private List<Integer> ids;

  /**
   * orderNum order type : ASC, DESC
   */
  private String ont = "ASC";
}
