package com.leFinance.creditLoan.bizRule.common.enums;

/**
 * @author: zhulili1
 * @date: 2017/10/11
 * @description: 消息Code枚举
 */
public enum SystemExceptionCode {

    INTERFACE_CALL_SERVICE(500, "接口调用服务异常");

    private int code;
    private String message;

    SystemExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
