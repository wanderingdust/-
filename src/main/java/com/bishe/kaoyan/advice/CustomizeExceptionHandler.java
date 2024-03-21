package com.bishe.kaoyan.advice;

import com.bishe.kaoyan.exception.CustomizeException;
import com.bishe.kaoyan.exception.implement.CustomizeErrorCode;
import com.bishe.kaoyan.utils.Result;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
@ResponseBody
public class CustomizeExceptionHandler {//据说拦截runtimeexception，又说拦截springmvc handler可以拦截的所有异常
    //不能handle的比如404啥的就在customizecontroller作通用处理
    @ExceptionHandler(Exception.class)
    Object handle(Throwable e, Model model, HttpServletRequest request) {

        String contentType = request.getContentType();

        if("application/json".equals(contentType)){
            if(e instanceof CustomizeException){//e是否为自定义错误子类
                return Result.error((CustomizeException)e);
            }else{//真的处理不了这个异常
                return Result.error(CustomizeErrorCode.SYS_ERROR);
            }
        }else {
            //错误页面跳转
            if(e instanceof CustomizeException){//e是否为自定义错误子类
                model.addAttribute("message", e.getMessage());
            }else{//真的处理不了这个异常
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}
