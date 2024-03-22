package com.bishe.kaoyan.mapper;

import com.bishe.kaoyan.pojo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;


public class UserMapper  {
    //注册
    @Insert("insert into user(phone,password,nick_name,user_create,user_modified) " +
            "values(#{phone},#{password},#{nick_name},#{user_create},#{user_modified})")
    public void register(User user){
    }

    //登录
    @Select("SELECT * FROM user where username=#{username} and password=#{password}")
    public void login(User user){
    }
}
