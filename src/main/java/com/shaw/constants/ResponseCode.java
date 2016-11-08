package com.shaw.constants;

/**
 * Created by shaw on 2016/10/13 0013.
 */
public enum ResponseCode {
    SUCCESS(200, "操作成功"),
    PARAM_NULL(400, "参数为空"),
    CODES_WRONG(601, "验证码错误"),
    LOGIN_WRONG(999, "账号或密码错误"),
    PARAM_NOT_FORMAT(1001, "参数格式错误");

    /**
     * 消息状态
     */
    private int code;
    /**
     * 信息
     */
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
