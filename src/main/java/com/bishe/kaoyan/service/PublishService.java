package com.bishe.kaoyan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.utils.Result;

public interface PublishService extends IService<Question> {
    Result publish(Question question, int id);//发布
    Result listing(Integer tagId, String search, Integer page, Integer size);//主页的总问题页
    Result listing(Integer id, Integer page, Integer size);//个人问题页
    Result edit(Question question);//根据问题id修改问题
}
