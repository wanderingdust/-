package com.bishe.kaoyan.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.kaoyan.exception.CustomizeException;
import com.bishe.kaoyan.exception.implement.CustomizeErrorCode;
import com.bishe.kaoyan.mapper.BaseMapper.QuestionMapper;
import com.bishe.kaoyan.mapper.BaseMapper.UserMapper;
import com.bishe.kaoyan.pojo.dto.QuestionDTO;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.QuestionService;
import com.bishe.kaoyan.utils.Result;
import com.bishe.kaoyan.utils.ResultCodeEnum;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("questionService")
@MapperScan("com.bishe.kaoyan.mapper.BaseMapper")
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {

    @Resource(name="questionMapper")
    private QuestionMapper questionMapper;

    @Resource(name="userMapper")
    private UserMapper userMapper;

    @Override
    public Result question(Integer id){
        Question question = questionMapper.selectById(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        User user = userMapper.selectById(question.getCreator());
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return Result.ok(questionDTO);
    }

    @Override
    public void incView(Integer id) {
        Question question = questionMapper.selectById(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        question.setViewCount(question.getViewCount()+1);
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getId,question.getId());
        questionMapper.update(question,queryWrapper);
    }
}
