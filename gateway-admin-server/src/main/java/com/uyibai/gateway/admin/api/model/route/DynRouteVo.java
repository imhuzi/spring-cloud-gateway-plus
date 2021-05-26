package com.uyibai.gateway.admin.api.model.route;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uyibai.gateway.admin.constant.RouteStatus;

import lombok.Data;

/**
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
@Data
public class DynRouteVo implements Serializable {

    private Integer id;

    private Integer appId;
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
     * 路由uri地址
     */
    private String routeUri;

    /**
     * 分组id
     */
    private Integer groupId;

    /**
     * app service id
     */
    private Integer serviceId;

    private String groupKey;

    /**
     * meta数据
     */
    private HashMap<String, Object> metadata;


    /**
     * 状态(0,未启用,1 启用)
     */
    private Integer status;

    /**
     * 分组 下的排序
     */
    private Integer orderNum;

    public boolean isOnline() {
        return null != status && RouteStatus.STATUS_ONLINE == status;
    }

    public boolean isOffline() {
        return null != status && RouteStatus.STATUS_OFFLINE== status;
    }


    /**
     * 路由规则
     */
    private List<DynRoutePredicateVo> predicates = new ArrayList<>();

    /**
     * 过滤器配置
     */
    private List<DynRouteFilterVo> filters = new ArrayList<>();

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    private LocalDateTime updatedAt;
}
