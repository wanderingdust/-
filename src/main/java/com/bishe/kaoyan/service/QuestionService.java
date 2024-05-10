package com.bishe.kaoyan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bishe.kaoyan.pojo.dto.QuestionDTO;
import com.bishe.kaoyan.pojo.model.Label;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.utils.Result;

import java.util.List;
import java.util.Set;

public interface QuestionService extends IService<Question> {
    Result question(Integer id);//根据问题id查看问题详情，查询操作

    void incView(Integer id);//根据问题id增加阅读数，更新操作

    List<QuestionDTO> selectRelated(QuestionDTO questionDTO);//搜索相关内容

    List<Label> selectLabel();//搜索标签

    int like(int targetId, int commentatorId);//根据目标id和评论者设置点赞

    Result popularQuestionList();//获取热门话题
}
