package com.uyibai.gateway.admin.api.model.route;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.uyibai.gateway.admin.api.model.definition.ArgsValueDefinition;

import lombok.Data;

/**
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
@Data
public class DynRoutePredicateVo implements Serializable {

  private Integer id;

  /**
   * 支持的规则断言id
   */
//    private Integer predicateId;

  private Integer routeId;

  private String predicateKey;

  /**
   * 参数值
   */
  private List<ArgsValueDefinition> argsValue;

  /**
   * 状态(0,未启用,1 启用)
   */
  private Integer status;

  /**
   * 创建时间
   */
  private LocalDateTime createdAt;

  /**
   * 修改时间
   */
  private LocalDateTime updatedAt;

  private Integer routeOrderNum;

  public Map<String, String> getArgsMap() {
    if (argsValue == null) {
      return new LinkedHashMap<>();
    }
    Map<String, String> valMap = new LinkedHashMap<>();

    for (int i = 0, len = argsValue.size(); i < len; i++) {
      ArgsValueDefinition item = argsValue.get(i);
      // 如果 是 list key= argsKey + index
      if (valMap.containsKey(item.getArgsKey())) {
        valMap.put(item.getArgsKey() + i, item.getValue());
      } else {
        valMap.put(item.getArgsKey(), item.getValue());
      }
    }
    return valMap;
  }
}
