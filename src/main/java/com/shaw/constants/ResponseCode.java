package com.shaw.constants;

/**
 * Created by shaw on 2016/10/13 0013.
 */
public enum ResponseCode {
    SUCCESS(200, "操作成功"),
    FIND_NULL(201, "查询为空"),
    PARAM_NULL(400, "参数为空"),
    FAIL(600, "操作失败"),
    CODES_WRONG(601, "验证码错误"),
    IP_WRONG(602, "IP验证无效"),
    ID_WRONG(603, "ID无效"),
    MSG_OVER(604, "消息已结束"),
    LOGIN_TIMEOUT(605, "登录超时"),
    PERMISSION_WRONG(606, "权限不足"),
    SOCKET_NOT_CONNECT(607, "任务执行客户端未连接"),
    USER_REPEAT(608, "用户重复"),
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
