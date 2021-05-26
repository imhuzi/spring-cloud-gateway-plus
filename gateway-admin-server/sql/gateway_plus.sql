/*
 Navicat Premium Data Transfer

 Source Server         : local-doccker-mdb
 Source Server Type    : MySQL
 Source Server Version : 100408
 Source Host           : localhost:32769
 Source Schema         : gateway_plus

 Target Server Type    : MySQL
 Target Server Version : 100408
 File Encoding         : 65001

 Date: 27/05/2021 00:36:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app_registry
-- ----------------------------
DROP TABLE IF EXISTS `app_registry`;
CREATE TABLE `app_registry` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) NOT NULL COMMENT '分组名称',
  `registry_key` varchar(20) NOT NULL COMMENT '注册中心id',
  `username` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
  `address` varchar(100) NOT NULL COMMENT '地址',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注说明',
  `parameters` varchar(500) NOT NULL DEFAULT '' COMMENT '附加参数',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(10) DEFAULT NULL COMMENT '状态(0,未启用,1 启用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='Dubbo 注册中心配置';

-- ----------------------------
-- Table structure for app_service
-- ----------------------------
DROP TABLE IF EXISTS `app_service`;
CREATE TABLE `app_service` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service` varchar(300) NOT NULL DEFAULT '' COMMENT 'service名称',
  `version` varchar(20) NOT NULL DEFAULT '' COMMENT '服务版本',
  `group` varchar(100) NOT NULL DEFAULT '' COMMENT '服务分组',
  `protocol` varchar(20) NOT NULL DEFAULT '' COMMENT '协议类型',
  `sdk_type` varchar(20) NOT NULL DEFAULT '' COMMENT 'sdk类型(dubbo,sofa,motan)',
  `sdk_version` varchar(20) NOT NULL DEFAULT '' COMMENT 'sdk version',
  `app_id` int(20) NOT NULL COMMENT '应用id',
  `registry_ids` varchar(200) NOT NULL COMMENT '注册中心ids,多个',
  `route_uri_prefix` varchar(300) NOT NULL DEFAULT '' COMMENT '网关uri前缀',
  `path` varchar(300) NOT NULL DEFAULT '' COMMENT '服务路径',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT 'service中文名称',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注说明',
  `meta` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'service Meta信息' CHECK (json_valid(`meta`)),
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(10) DEFAULT NULL COMMENT '状态(0,未启用,1 启用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=262 DEFAULT CHARSET=utf8mb4 COMMENT='Dubbo 服务信息';

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `app_id` varchar(100) NOT NULL COMMENT '应用唯一标识(和spring.application.name一致)',
  `app_type` varchar(10) NOT NULL COMMENT '应用类型(dubbo,springCloud,dubboSpringCloud,http)',
  `app_name` varchar(300) NOT NULL DEFAULT '' COMMENT '应用名称',
  `instances` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实例信息' CHECK (json_valid(`instances`)),
  `uri_prefix` varchar(100) NOT NULL DEFAULT '' COMMENT 'uri 前缀 用于网关代理',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注说明',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(10) DEFAULT NULL COMMENT '状态(0,未启用,1 启用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COMMENT='应用信息(以项目维度)';

-- ----------------------------
-- Table structure for dyn_route
-- ----------------------------
DROP TABLE IF EXISTS `dyn_route`;
CREATE TABLE `dyn_route` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(100) DEFAULT NULL COMMENT '路由名称',
  `app_id` int(20) unsigned DEFAULT NULL COMMENT '应用id',
  `route_key` varchar(128) DEFAULT NULL COMMENT '路由id(可读性的字符串)',
  `route_type` varchar(10) DEFAULT NULL COMMENT '路由类型(http,lb,dubbo,grpc)',
  `route_uri` varchar(500) DEFAULT NULL COMMENT '路由uri地址',
  `group_id` int(20) unsigned DEFAULT 0 COMMENT '分组id',
  `service_id` int(20) unsigned DEFAULT 0 COMMENT '服务id',
  `order_num` int(10) DEFAULT 0 COMMENT '在分组下的排序',
  `metadata` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'meta数据' CHECK (json_valid(`metadata`)),
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(10) DEFAULT NULL COMMENT '状态(0,未启用,1 启用)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_route_key` (`route_key`) USING BTREE COMMENT '路由唯一key值'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='动态路由信息表';

-- ----------------------------
-- Table structure for dyn_route_filter
-- ----------------------------
DROP TABLE IF EXISTS `dyn_route_filter`;
CREATE TABLE `dyn_route_filter` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `route_id` int(20) unsigned NOT NULL COMMENT '路由id',
  `status` int(10) DEFAULT 0 COMMENT '状态(0,未启用,1 启用)',
  `route_order_num` int(10) DEFAULT 0 COMMENT '路由下的Filter执行顺序',
  `args_value` varchar(500) NOT NULL COMMENT '参数值',
  `filter_key` varchar(20) NOT NULL COMMENT 'Filter Key值',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_route_filter_id` (`route_id`) USING BTREE COMMENT '一个filter在route下只能配置一次'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态路由过滤器链表';

-- ----------------------------
-- Table structure for dyn_route_predicate
-- ----------------------------
DROP TABLE IF EXISTS `dyn_route_predicate`;
CREATE TABLE `dyn_route_predicate` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `route_id` int(20) unsigned NOT NULL COMMENT '路由id',
  `predicate_key` varchar(20) NOT NULL COMMENT '规则key',
  `args_value` varchar(500) NOT NULL COMMENT '参数值',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(10) DEFAULT NULL COMMENT '状态(0,未启用,1 启用)',
  `route_order_num` int(10) DEFAULT 0 COMMENT '路由下的规则执行顺序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='动态路由规则表';

-- ----------------------------
-- Table structure for gateway_filter
-- ----------------------------
DROP TABLE IF EXISTS `gateway_filter`;
CREATE TABLE `gateway_filter` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) NOT NULL COMMENT '过滤器名称',
  `filter_type` tinyint(4) NOT NULL COMMENT '过滤器类型(1:pre, 2:post)',
  `filter_scope` tinyint(4) NOT NULL COMMENT '过滤器作用域(1:global,2:route)',
  `filter_key` varchar(20) NOT NULL COMMENT '过滤器唯一key值',
  `args` varchar(500) NOT NULL COMMENT '过滤器参数',
  `multi` tinyint(1) DEFAULT 0 COMMENT '是否允许多个规则同时存在',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注说明',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(10) DEFAULT NULL COMMENT '状态(0,未启用,1 启用)',
  `order_num` int(10) DEFAULT 0 COMMENT 'Filter 本身的 order',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_filter_key` (`filter_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='网关支持的过滤器信息';

-- ----------------------------
-- Table structure for gateway_group
-- ----------------------------
DROP TABLE IF EXISTS `gateway_group`;
CREATE TABLE `gateway_group` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) NOT NULL COMMENT '分组名称',
  `group_key` varchar(20) NOT NULL COMMENT '分组ID',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注说明',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(10) DEFAULT NULL COMMENT '状态(0,未启用,1 启用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网关实例分组';

-- ----------------------------
-- Table structure for gateway_node
-- ----------------------------
DROP TABLE IF EXISTS `gateway_node`;
CREATE TABLE `gateway_node` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) NOT NULL COMMENT '实例名称',
  `group_id` int(20) unsigned NOT NULL COMMENT '分组id',
  `node_ip` varchar(20) NOT NULL COMMENT '节点ip',
  `status` int(10) DEFAULT NULL COMMENT '状态(0,未启用,1 启用)',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网关实例节点';

-- ----------------------------
-- Table structure for gateway_route_predicate
-- ----------------------------
DROP TABLE IF EXISTS `gateway_route_predicate`;
CREATE TABLE `gateway_route_predicate` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) NOT NULL COMMENT '路由名称',
  `predicate_key` varchar(20) NOT NULL COMMENT '路由规则断言唯一key值',
  `args` varchar(500) NOT NULL COMMENT '路由规则断言参数',
  `multi` tinyint(1) DEFAULT 0 COMMENT '是否允许多个规则同时存在',
  `remark` varchar(1000) DEFAULT '' COMMENT '备注说明',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(10) DEFAULT NULL COMMENT '状态(0,未启用,1 启用)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_predicate_key` (`predicate_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COMMENT='网关支持的路由断言信息';

SET FOREIGN_KEY_CHECKS = 1;
