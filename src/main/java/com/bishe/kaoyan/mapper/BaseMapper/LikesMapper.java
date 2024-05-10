package com.bishe.kaoyan.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishe.kaoyan.pojo.model.Likes;
import org.springframework.stereotype.Repository;

@Repository("likesMapper")
public interface LikesMapper extends BaseMapper<Likes>{
}
