package com.bishe.kaoyan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.utils.Result;

public interface QuestionService extends IService<Question> {
    Result question(Integer id);//根据问题id查看问题详情，查询操作

    void incView(Integer id);//根据问题id增加阅读数，更新操作
}
