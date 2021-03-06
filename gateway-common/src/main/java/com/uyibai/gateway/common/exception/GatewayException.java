package com.uyibai.gateway.common.exception;

public class GatewayException extends RuntimeException{
    private static final long serialVersionUID = 8068509879445395353L;

    private Integer code;

    /**
     * Instantiates a new Soul exception.
     *
     * @param e the e
     */
    public GatewayException(final Throwable e) {
        super(e);
    }

    /**
     * Instantiates a new Soul exception.
     *
     * @param message the message
     */
    public GatewayException(final String message) {
        super(message);
    }

    public GatewayException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }


    /**
     * Instantiates a new Soul exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public GatewayException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
