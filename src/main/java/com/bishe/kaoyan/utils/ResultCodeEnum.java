package com.bishe.kaoyan.utils;

/**
 * 统一返回结果状态信息类
 *
 */
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    USERNAME_ERROR(501,"用户名错误"),
    PASSWORD_ERROR(503,"密码错误"),
    NOT_LOGIN(504,"未登录"),
    USERNAME_USED(505,"用户名已被使用"),
    NOT_FOUND(506,"没找到"),
    COMMENT_IS_EMPTY(507,"评论不能为空"),
    COMMENT_IS_NULL(508,"没有评论");

    private Integer code;
    private String message;
    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
