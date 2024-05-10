package com.bishe.kaoyan.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishe.kaoyan.pojo.model.Label;
import org.springframework.stereotype.Repository;

@Repository("labelMapper")
public interface LabelMapper extends BaseMapper<Label> {
}
