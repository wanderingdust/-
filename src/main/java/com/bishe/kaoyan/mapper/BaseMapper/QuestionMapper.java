package com.bishe.kaoyan.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishe.kaoyan.pojo.model.Question;
import org.springframework.stereotype.Repository;

@Repository("questionMapper")
public interface QuestionMapper extends BaseMapper<Question> {
}
