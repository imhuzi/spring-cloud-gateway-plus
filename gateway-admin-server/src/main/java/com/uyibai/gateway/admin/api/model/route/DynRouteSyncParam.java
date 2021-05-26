package com.uyibai.gateway.admin.api.model.route;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 动态路由发布参数
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 *
 */
@Data
public class DynRouteSyncParam implements Serializable {

    /**
     * 全部路由
     */
    private boolean allRoute = true;

    /**
     * 全部节点
     */
    private boolean allNode = true;

    /**
     * 路由id 集合
     */
    public List<Integer> routeIds;

    /**
     * 节点 ip
     */
    public List<String> nodes;
}
