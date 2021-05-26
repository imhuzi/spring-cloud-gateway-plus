package com.uyibai.gateway.admin.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@EnableConfigurationProperties(GatewayAdminProperties.class)
public class DynamicRouteConfig {

}
