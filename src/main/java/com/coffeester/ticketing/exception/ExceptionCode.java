package com.coffeester.ticketing.exception;

/**
 * Created by amitsehgal on 1/30/16.
 */
public enum ExceptionCode {


    LEVEL_DOES_NOT_EXIST_1001(1001, "Level does not exists.");


    private Integer code;
    private String clientFacingMessage;

    private ExceptionCode(Integer code, String customMessage) {
        this.code = code;
        this.clientFacingMessage = customMessage;
    }

    public Integer getCode() {
        return code;
    }

    public String getClientFacingMessage() {
        return clientFacingMessage;
    }
}
