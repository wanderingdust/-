package com.bishe.kaoyan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bishe.kaoyan.pojo.model.Comment;
import com.bishe.kaoyan.utils.CommentTypeEnum;
import com.bishe.kaoyan.utils.Result;


public interface CommentService extends IService<Comment> {
    void response(Comment comment);//回复
    void incComment(Integer id, Integer type);//回复

    Result listByParentId(Integer id, CommentTypeEnum type);
}
