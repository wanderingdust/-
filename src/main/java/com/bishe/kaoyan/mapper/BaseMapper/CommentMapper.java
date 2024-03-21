package com.bishe.kaoyan.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishe.kaoyan.pojo.model.Comment;
import org.springframework.stereotype.Repository;

@Repository("commentMapper")
public interface CommentMapper extends BaseMapper<Comment> {
}
