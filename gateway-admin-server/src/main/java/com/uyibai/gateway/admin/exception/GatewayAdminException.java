package com.uyibai.gateway.admin.exception;


import com.uyibai.gateway.common.constants.ErrorCode;
import com.uyibai.gateway.common.exception.GatewayException;

public class GatewayAdminException extends GatewayException {
    private static final long serialVersionUID = 8068509879445395353L;

    /**
     * Instantiates a new Soul exception.
     *
     * @param message the message
     */
    public GatewayAdminException(final String message) {
        super(message);
    }

    public GatewayAdminException(Integer code, String message) {
        super(code, message);
    }

    public GatewayAdminException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getMsg());
    }

}
