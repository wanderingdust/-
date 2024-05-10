package com.bishe.kaoyan.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.kaoyan.exception.CustomizeException;
import com.bishe.kaoyan.exception.implement.CustomizeErrorCode;
import com.bishe.kaoyan.mapper.BaseMapper.LabelMapper;
import com.bishe.kaoyan.mapper.BaseMapper.QuestionMapper;
import com.bishe.kaoyan.mapper.BaseMapper.UserMapper;
import com.bishe.kaoyan.pojo.dto.PaginationDTO;
import com.bishe.kaoyan.pojo.dto.QuestionDTO;
import com.bishe.kaoyan.pojo.model.Label;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.PublishService;
import com.bishe.kaoyan.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("publishService")
@MapperScan("com.bishe.kaoyan.mapper.BaseMapper")
public class PublishServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements PublishService {

    @Resource(name="questionMapper")
    private QuestionMapper questionMapper;

    @Resource(name="labelMapper")
    private LabelMapper labelMapper;

    @Resource(name="userMapper")
    private UserMapper userMapper;

    @Override
    public Result publish(Question question, int id) {
        question.setCreator(id);
        int rows = questionMapper.insert(question);
        String[] tags = StringUtils.split(question.getTag(), ",");
        for (String tag : tags){
            LambdaQueryWrapper<Label> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Label::getContent, tag);
            //查找label中是否存在，不存在就加，存在就不管
            if (labelMapper.selectCount(queryWrapper) == 0){
                Label label = new Label();
                label.setContent(tag);
                labelMapper.insert(label);
            }
        }
        System.out.println("rows = " + rows);
        return Result.ok(null);
    }

    @Override
    public Result listing(Integer tagId, String search, Integer page, Integer size) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(search)){//搜索功能
            String[] searchs = StringUtils.split(search, " ");//得到search数组
            List searchList = Arrays.asList(searchs);
            queryWrapper.and(wrapper -> searchList.forEach(rapper ->
                    wrapper.or(title -> title.like(Question::getTitle, rapper))));//我都不知道这是啥，网上到处找的，缝缝补补写出这一句
        }
        //获取分类标签
        if (tagId != null){
            String tag = labelMapper.selectById(tagId).getContent();
            queryWrapper.like(Question::getTag, tag);
        }

        //page范围检查,垃圾代码
        int totalCount = questionMapper.selectCount(queryWrapper).intValue();
        if(page < 1)
            page = 1;
        if(totalCount%size == 0 && page > totalCount/size){
            page = totalCount/size;
        }
        if(totalCount%size != 0 && page > totalCount/size+1){
            page = (totalCount/size) + 1;
        }

        queryWrapper.orderByDesc(Question::getGmtCreate);

        Page<Question> questionPage = new Page<>(page, size);
        questionMapper.selectPage(questionPage, queryWrapper);

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for(Question question : questionPage.getRecords()){
            User user = userMapper.selectById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);//将question复制到questionDTO上
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        Long a = questionPage.getPages();
        paginationDTO.setPagnation(totalCount, page, size);
        paginationDTO.setData(questionDTOList);
        paginationDTO.setTotalPage(a.intValue());
        return Result.ok(paginationDTO);
    }

    @Override
    public Result listing(Integer userId, Integer page, Integer size) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getCreator,userId);
        //page范围检查,垃圾代码
        int totalCount = questionMapper.selectCount(queryWrapper).intValue();
        if(page < 1)
            page = 1;
        if(totalCount % size == 0 && page > totalCount/size){
            page = totalCount/size;
        }
        if(totalCount % size != 0 && page > totalCount/size + 1){
            page = (totalCount/size) + 1;
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

        Long a = questionPage.getPages();
        paginationDTO.setPagnation(totalCount, page, size);
        paginationDTO.setData(questionDTOList);
        paginationDTO.setTotalPage(a.intValue());
        return Result.ok(paginationDTO);
    }

    @Override
    public Result edit(Question question) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getId,question.getId());
        //String oldTag = questionMapper.selectOne(queryWrapper).getTag();
        int count = questionMapper.update(question,queryWrapper);
        String[] tags = StringUtils.split(question.getTag(), ",");
        for (String tag : tags){
            LambdaQueryWrapper<Label> labelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            labelLambdaQueryWrapper.eq(Label::getContent, tag);
            //查找label中是否存在，不存在就加，存在就不管
            if (labelMapper.selectCount(labelLambdaQueryWrapper) == 0){
                Label label = new Label();
                label.setContent(tag);
                labelMapper.insert(label);
            }
        }
        if(count == 1){
            return Result.ok(null);
        }else
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
    }
}
