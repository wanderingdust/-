package com.bishe.kaoyan.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.kaoyan.exception.CustomizeException;
import com.bishe.kaoyan.exception.implement.CustomizeErrorCode;
import com.bishe.kaoyan.mapper.BaseMapper.*;
import com.bishe.kaoyan.pojo.dto.QuestionDTO;
import com.bishe.kaoyan.pojo.model.*;
import com.bishe.kaoyan.service.QuestionService;
import com.bishe.kaoyan.utils.Result;
import com.bishe.kaoyan.utils.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("questionService")
@MapperScan("com.bishe.kaoyan.mapper.BaseMapper")
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {

    @Resource(name="questionMapper")
    private QuestionMapper questionMapper;

    @Resource(name="userMapper")
    private UserMapper userMapper;

    @Resource(name="labelMapper")
    private LabelMapper labelMapper;

    @Resource(name="likesMapper")
    private LikesMapper likesMapper;

    @Resource(name="commentMapper")
    private CommentMapper commentMapper;

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

    @Override
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO){
        if (StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");//得到tag数组
        List tagList = Arrays.asList(tags);
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Question::getId, queryDTO.getId())
                .and(wrapper -> tagList.forEach(rapper -> wrapper.or(tag -> tag.like(Question::getTag, rapper))));//我都不知道这是啥，网上到处找的，缝缝补补写出这一句
        List<Question> questions = questionMapper.selectList(queryWrapper);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());

        return questionDTOS;
    }

    @Override
    public List<Label> selectLabel(){
        List<Label> labelList = labelMapper.selectList(null);
        return labelList;
    }

    @Override
    public int like(int targetId, int likerId){//评论Id和点赞者id
        LambdaQueryWrapper<Likes> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Likes::getLikerId, likerId).eq(Likes::getTargetId, targetId);
        LambdaQueryWrapper<Likes> selectqueryWrapper = new LambdaQueryWrapper<>();
        selectqueryWrapper.eq(Likes::getTargetId, targetId);
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getId, targetId);
        Comment comment = commentMapper.selectOne(commentLambdaQueryWrapper);
        int likeCount;
        if (likesMapper.selectCount(queryWrapper) > 0){//判断点赞者是否已经点了赞了
            //点了赞就删除
            likesMapper.delete(queryWrapper);
            likeCount = likesMapper.selectCount(selectqueryWrapper).intValue();
            //更新comment表的赞
            comment.setLikeCount(likeCount);
        }else {
            //没点就在likes表中增加
            Likes likes = new Likes();
            likes.setLikerId(likerId);
            likes.setTargetId(targetId);
            likes.setType(1);
            likesMapper.insert(likes);
            likeCount = likesMapper.selectCount(selectqueryWrapper).intValue();
            //更新comment表的赞
            comment.setLikeCount(likeCount);
        }
        commentMapper.update(comment,commentLambdaQueryWrapper);
        return likeCount;
    }

    @Override
    public Result popularQuestionList(){
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        questionLambdaQueryWrapper.orderByDesc(Question::getViewCount).gt(Question::getViewCount, 5);
        List<Question> questions = questionMapper.selectList(questionLambdaQueryWrapper);
        return Result.ok(questions);
    }
}
