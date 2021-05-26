package com.uyibai.gateway.common.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 */
@Configuration
public class CommonSpringAutoConfiguration {
  @Bean
  public SpringContextUtil springContextUtil() {
    return new SpringContextUtil();
  }
}
