package com.bishe.kaoyan.controller;


import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.utils.Result;
import com.bishe.kaoyan.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller("userInfoController")
@RequestMapping("/user")
@CrossOrigin//跨域
public class UserInfoController {
    @Resource(name="userService")
    private UserService userService;

    @GetMapping("getUserInfo")//get
    public Result userInfo(@RequestHeader String token){//接住json属性
        Result result = userService.getUserInfo(token);
        return result;
    }

    @PostMapping(value ="/login",produces = "application/json;charset=UTF-8")//post
    //@ResponseBody
    public String login(User user, HttpServletRequest request){////User前有个@Requestbody的，但加了会返415错误
        Result result = userService.login(user);
        System.out.println("result = " + result);
        if(result.getCode() == 200){
            request.getSession().setAttribute("user",result);
            return "index";
        }else{
            return "error";
        }
        //return result;//result.data=token,result.code=200,result.message=success
    }

    //@ResponseBody
    //@RequestMapping("/register")//用于将任意HTTP 请求映射到控制器方法上。@RequestMapping表示共享映射，
    // 如果没有指定请求方式，将接收GET、POST、HEAD、OPTIONS、PUT、PATCH、DELETE、TRACE、CONNECT所有的HTTP请求方式。
    // @GetMapping、@PostMapping、@PutMapping、@DeleteMapping、@PatchMapping 都是HTTP方法特有的快捷方式@RequestMapping的变体。
    @PostMapping(value ="/register",produces = "application/json;charset=UTF-8")
    public String register(User user) {//User前有个@Requestbody的，但加了会返415错误
        userService.register(user);
        return "index";
    }
}
