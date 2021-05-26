package com.uyibai.gateway.admin.api.model.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 网关路由定义,
 * 和 org.springframework.cloud.gateway.route.RouteDefinition 结构相同
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
@Data
public class GatewayRouteDefinition implements Serializable {
  private String id;

  /**
   * 路由分组
   */
  private String groupKey;

  @NotEmpty
  @Valid
  private List<PredicateDefinition> predicates = new ArrayList<>();

  @Valid
  private List<FilterDefinition> filters = new ArrayList<>();

  @NotNull
  private String uri;

  private Map<String, Object> metadata = new HashMap<>();

  private int order = 0;

  public GatewayRouteDefinition() {
  }
}
