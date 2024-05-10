package com.bishe.kaoyan.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bishe.kaoyan.pojo.dto.UserInfoDTO;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.utils.Result;


public interface UserService extends IService<User> {
    Result login(User user);
    Result register(User user);
    Result getUserInfo(String token);
    Result modify(UserInfoDTO userInfoDTO);
}
