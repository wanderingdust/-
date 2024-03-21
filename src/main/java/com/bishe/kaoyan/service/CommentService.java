package com.bishe.kaoyan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bishe.kaoyan.pojo.model.Comment;

public interface CommentService extends IService<Comment> {
    void response(Comment comment);//回复
    void incComment(Integer id);//回复
}
