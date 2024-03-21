package com.bishe.kaoyan.interceptor;

import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.UserService;
import com.bishe.kaoyan.utils.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Resource(name="userService")
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = null;
        for (Cookie cookie : cookies){
            if (cookie.getName().equals("user_token")){
                token= cookie.getValue();
                break;
            }
        }
        Result result = userService.getUserInfo(token);
        if(result.getCode() == 200){
            Map map = (Map) result.getData();
            User user = (User) map.get("loginUser");
            request.getSession().setAttribute("loginUser",user);
            return true;
        }else{
            System.out.println("已拦截");
            System.out.println("token为"+token);
            System.out.println("result.getcode为"+result.getCode());
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
