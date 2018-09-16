package com.yaochow.serviceblog.common;

public enum ResponseMsgEnum {
    SUCCESS("0", "Success."),
    SYSTEM_ERROR("999", "System Error.");

    private String errorCode;
    private String errorMsg;

    ResponseMsgEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
