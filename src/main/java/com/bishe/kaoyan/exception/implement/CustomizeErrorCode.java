package com.bishe.kaoyan.exception.implement;

import com.bishe.kaoyan.exception.ICustomizeErrorCode;

public enum  CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001, "你找的问题不在了"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何评论进行回复"),
    NOT_LOGIN(2003, "请登录后重试"),
    SYS_ERROR(2004,"你到底干了啥啊，服务器冒烟啦，哈哈哈"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你回复的评论不存在");

    private Integer code;
    private String message;

    @Override
    public Integer getCode(){
        return code;
    }

    @Override
    public String getMessage(){
        return message;
    }

    CustomizeErrorCode(Integer code, String message){
        this.message = message;
        this.code = code;
    }


}
