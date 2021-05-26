package com.uyibai.gateway.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring context utils
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 */
public class SpringContextUtil implements ApplicationContextAware {
  private static ApplicationContext applicationContext;

  public SpringContextUtil() {
  }

  public static <T> T getBean(String beanName, Class<T> executorClass) {
    return applicationContext.getBean(beanName, executorClass);
  }

  @Override
  public void setApplicationContext(ApplicationContext context) {
    applicationContext = context;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static Object getBean(String name) throws BeansException {
    return applicationContext.getBean(name);
  }

  public static <T> T getBean(Class<T> tClass) throws BeansException {
    return applicationContext.getBean(tClass);
  }
}
