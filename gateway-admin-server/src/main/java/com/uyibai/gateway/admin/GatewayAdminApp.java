package com.uyibai.gateway.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * spring cloud gateway plus admin App
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/27
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayAdminApp {


  public static void main(String[] args) {
    SpringApplication.run(GatewayAdminApp.class, args);
  }


}
