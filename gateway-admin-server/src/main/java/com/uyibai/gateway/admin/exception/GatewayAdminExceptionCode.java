package com.uyibai.gateway.admin.exception;


import com.uyibai.gateway.common.constants.ErrorCode;

/**
 * 网关 管理后台 异常错误码
 */
public class GatewayAdminExceptionCode extends ErrorCode {

    public GatewayAdminExceptionCode(Integer code, String msg) {
        super(code, msg);
    }

    public static GatewayAdminExceptionCode GROUP_USING_ERROR = new GatewayAdminExceptionCode(11000, "分组使用中 不可以删除");
    public static GatewayAdminExceptionCode ROUTE_FILTER_ID_ERROR = new GatewayAdminExceptionCode(11001, "路由信息中存在未先声明的filter");
    public static GatewayAdminExceptionCode ROUTE_PREDICATE_ID_ERROR = new GatewayAdminExceptionCode(11002, "路由信息中存在未先声明的路由规则");
    public static GatewayAdminExceptionCode GATEWAY_FILTER_USING_ERROR = new GatewayAdminExceptionCode(11003, "Filter使用中 不可以删除");
    public static GatewayAdminExceptionCode GATEWAY_ROUTE_PREDICATE_USING_ERROR = new GatewayAdminExceptionCode(11004, "路由规则使用中 不可以删除");
    public static GatewayAdminExceptionCode ROUTE_USING_ERROR = new GatewayAdminExceptionCode(11005, "路由在使用中不可删除，请先下线");
    public static GatewayAdminExceptionCode NOT_ROUTE_PUBLISHER = new GatewayAdminExceptionCode(11006, "请先定义动态路由发布类型");
    public static GatewayAdminExceptionCode ROUTE_NOT_FIND = new GatewayAdminExceptionCode(11007, "无路由信息");
    public static GatewayAdminExceptionCode APP_SERVICE_SAVE_ERROR = new GatewayAdminExceptionCode(11008, "服务信息保存失败");
}
