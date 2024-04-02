package com.bishe.kaoyan.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.kaoyan.exception.CustomizeException;
import com.bishe.kaoyan.exception.implement.CustomizeErrorCode;
import com.bishe.kaoyan.mapper.BaseMapper.QuestionMapper;
import com.bishe.kaoyan.mapper.BaseMapper.UserMapper;
import com.bishe.kaoyan.pojo.dto.PaginationDTO;
import com.bishe.kaoyan.pojo.dto.QuestionDTO;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.PublishService;
import com.bishe.kaoyan.utils.Result;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("publishService")
@MapperScan("com.bishe.kaoyan.mapper.BaseMapper")
public class PublishServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements PublishService {

    @Resource(name="questionMapper")
    private QuestionMapper questionMapper;

    @Resource(name="userMapper")
    private UserMapper userMapper;

    @Override
    public Result publish(Question question, int id) {
        question.setCreator(id);
        int rows = questionMapper.insert(question);
        System.out.println("rows = " + rows);
        return Result.ok(null);
    }

    @Override
    public Result listing(Integer page, Integer size) {

        //page范围检查,垃圾代码
        int offset = questionMapper.selectCount(null).intValue();
        if(page < 1)
            page = 1;
        if(offset%size == 0 && page > offset/size){
            page = offset/size;
        }
        if(offset%size != 0 && page > offset/size+1){
            page = (offset/size) + 1;
        }
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Question::getGmtCreate);

        Page<Question> questionPage = new Page<>(page, size);
        questionMapper.selectPage(questionPage, queryWrapper);

        System.out.println("当   前   页"+questionPage.getCurrent());
        System.out.println("总   页   数"+questionPage.getPages());
        System.out.println("每页显示条数"+questionPage.getSize());
        System.out.println("总 记  录 数"+questionPage.getTotal());
        System.out.println("是否有下一页"+questionPage.hasNext());
        System.out.println("是否有上一页"+questionPage.hasPrevious());

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for(Question question : questionPage.getRecords()){
            User user = userMapper.selectById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);//将question复制到questionDTO上
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        Long a = questionPage.getTotal();
        Integer totalCount = a.intValue();
        a = questionPage.getPages();
        paginationDTO.setPagnation(totalCount, page, size);
        paginationDTO.setData(questionDTOList);
        paginationDTO.setTotalPage(a.intValue());
        return Result.ok(paginationDTO);
    }

    @Override
    public Result listing(Integer userid, Integer page, Integer size) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getCreator,userid);
        //page范围检查,垃圾代码
        int count = questionMapper.selectCount(queryWrapper).intValue();
        if(page < 1)
            page = 1;
        if(count % size == 0 && page > count/size){
            page = count/size;
        }
        if(count % size != 0 && page > count/size + 1){
            page = (count/size) + 1;
        }

        Page<Question> questionPage = new Page<>(page, size);
        questionMapper.selectPage(questionPage, queryWrapper);

        System.out.println("当   前   页"+questionPage.getCurrent());
        System.out.println("总   页   数"+questionPage.getPages());
        System.out.println("每页显示条数"+questionPage.getSize());
        System.out.println("总 记  录 数"+questionPage.getTotal());
        System.out.println("是否有下一页"+questionPage.hasNext());
        System.out.println("是否有上一页"+questionPage.hasPrevious());

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for(Question question : questionPage.getRecords()){
            User user = userMapper.selectById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);//将question复制到questionDTO上
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        Long a = questionPage.getTotal();
        Integer totalCount = a.intValue();
        a = questionPage.getPages();
        paginationDTO.setPagnation(totalCount, page, size);
        paginationDTO.setData(questionDTOList);
        paginationDTO.setTotalPage(a.intValue());
        return Result.ok(paginationDTO);
    }

    @Override
    public Result edit(Question question) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getId,question.getId());
        int count = questionMapper.update(question,queryWrapper);
        if(count == 1){
            return Result.ok(null);
        }else
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
    }
}
