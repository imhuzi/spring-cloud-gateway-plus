package com.uyibai.gateway.admin.api.model.definition;

import java.io.Serializable;

import lombok.Data;

/**
 * 网关 filter 和 predicate 参数 对象
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
@Data
public class ArgsDefinition implements Serializable {
  /**
   * 参数名称
   */
  private String argsKey;

  /**
   * 显示文本
   */
  private String argsName;

  /**
   * 是否必须
   */
  private Boolean required = true;

  /**
   * 默认值
   */
  private String defaultValue = "";

  /**
   * 同一个参数是否可以重复
   */
  private Boolean repeat = false;

  /**
   * 数据类型
   */
  private String dataType = "String";

  private Integer order = 0;
}
