package com.bishe.kaoyan.service.implement;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.kaoyan.mapper.BaseMapper.UserMapper;
import com.bishe.kaoyan.pojo.dto.UserInfoDTO;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.UserService;
import com.bishe.kaoyan.utils.JwtHelper;
import com.bishe.kaoyan.utils.MD5Util;
import com.bishe.kaoyan.utils.Result;
import com.bishe.kaoyan.utils.ResultCodeEnum;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("userService")
@MapperScan("com.bishe.kaoyan.mapper.BaseMapper")
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    @Resource(name="jwtHelper")
    private JwtHelper jwtHelper;
    @Resource(name="userMapper")
    private  UserMapper userMapper;

    /**
     * 登录业务实现
     * @param user
     * @return result封装
     */
    @Override
    public Result login(User user) {

        //根据账号查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,user.getPhone());
        User loginUser = userMapper.selectOne(queryWrapper);

        //账号判断
        if (loginUser == null) {
            //账号错误
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        //判断密码
        if (!StringUtils.isEmpty(user.getPassword())
                && loginUser.getPassword().equals(MD5Util.encrypt(user.getPassword())))
        {
           //账号密码正确
            //根据用户唯一标识生成token
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getId()));
            System.out.println(token);

            Map data = new HashMap();
            data.put("token",token);

            return Result.ok(data);//result.data=token,result.code=200,result.message=success
        }

        //密码错误
        return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
    }

    @Override
    public Result register(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,user.getPhone());//where条件，select * from user where user.getPhone()==User::getPhone
        Long count = userMapper.selectCount(queryWrapper);

        if (count > 0){
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }

        user.setPassword(MD5Util.encrypt(user.getPassword()));
        user.setHeadSculpture("\\images\\8f650d71e09d46e3ac8dce67acfff894.png");
        System.out.println("注册了" + userMapper.insert(user) + "个账号");
        return Result.ok(null);
    }

    @Override
    public Result getUserInfo(String token) {

        //1.判定是否有效期
        if (jwtHelper.isExpiration(token)) {
            //true过期,直接返回未登录
            return Result.build(null,ResultCodeEnum.NOT_LOGIN);
        }

        //2.获取token对应的用户
        int userId = jwtHelper.getUserId(token).intValue();

        //3.查询数据

        User user = userMapper.selectById(userId);

        if (user != null) {
            user.setPassword(null);
            Map data = new HashMap();
            data.put("loginUser",user);
            return Result.ok(data);
        }

        return Result.build(null,ResultCodeEnum.NOT_LOGIN);
    }

    @Override
    public Result modify(UserInfoDTO userInfoDTO) {
        //根据账号查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,userInfoDTO.getId());
        User loginUser = userMapper.selectOne(queryWrapper);

        //判断密码，如果旧密码不为空且旧密码正确
        if (!StringUtils.isEmpty(userInfoDTO.getOldPassword())
                && loginUser.getPassword().equals(MD5Util.encrypt(userInfoDTO.getOldPassword()))) {
            //账号密码正确
            User user = new User();
            //如果新密码不为空
            if (!StringUtils.isEmpty(userInfoDTO.getNewPassword())){
                user.setPassword(MD5Util.encrypt(userInfoDTO.getNewPassword()));
                user.setHeadSculpture(userInfoDTO.getHeadSculpture());
                user.setNickName(userInfoDTO.getNickName());
                user.setPhone(userInfoDTO.getPhone());
                user.setHeadSculpture(userInfoDTO.getHeadSculpture());
                userMapper.update(user, queryWrapper);
                return Result.build(null,ResultCodeEnum.MODIFY_SUCCESS);//result.data=null,result.code=201,result.message=修改成功
            }
            user.setHeadSculpture(userInfoDTO.getHeadSculpture());
            user.setNickName(userInfoDTO.getNickName());
            user.setPhone(userInfoDTO.getPhone());
            user.setHeadSculpture(userInfoDTO.getHeadSculpture());
            userMapper.update(user, queryWrapper);

            return Result.ok(null);//result.data=null,result.code=200,result.message=success
        }
        //密码错误
        return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
    }
}