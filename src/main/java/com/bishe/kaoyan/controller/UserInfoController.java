package com.bishe.kaoyan.controller;


import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.utils.Result;
import com.bishe.kaoyan.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("userInfoController")
@RequestMapping("/user")
//@CrossOrigin//跨域
public class UserInfoController {
    @Resource(name="userService")
    private UserService userService;

    @GetMapping("/userInfo")//get
    public String userInfo(){
        return "redirect:/";
    }

    @PostMapping(value ="/login",produces = "application/json;charset=UTF-8")//post
    //@ResponseBody
    // 在使用 @RequestMapping 后，返回值通常解析为跳转路径，但是加上 @ResponseBody 后返回结果不会被解析为跳转路径，
    // 而是直接写入 HTTP response body 中。 比如异步获取 json 数据，加上 @ResponseBody 后，会直接返回 json 数据。
    public String login(User user, HttpServletResponse response){//User前有个@Requestbody的，但加了会返415错误
        Result result = userService.login(user);
        if(result.getCode() == 200){//result.data=token,result.code=200,result.message=success
            String token = String.valueOf(result.getData()).substring(7,result.getData().toString().indexOf("}"));//就这样吧，放弃治疗了
            Cookie cookie_token = new Cookie("user_token",token);
            cookie_token.setDomain("localhost");//设置域，不然cookie会被重置
            cookie_token.setPath("/");
            cookie_token.setMaxAge(60*60);
            response.addCookie(cookie_token);
            return "redirect:/user/userInfo";//看似多此一举，实则为了过一下拦截器，以加个session，不然直接跳到index就加不了session
        }else{
            return "error";
        }
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

    @GetMapping(value ="/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie_token : cookies){//找cookie中的token
            if (cookie_token.getName().equals("user_token")){//找到了就把包含token的cookie删除掉
                cookie_token.setMaxAge(0);//删除方式为设置cookie时间为0
                cookie_token.setPath("/");
                response.addCookie(cookie_token);//交给浏览器
                break;
            }
        }
        request.getSession().removeAttribute("loginUser");//顺便清除session
        return "redirect:/";
    }
}
