package com.bishe.kaoyan.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.kaoyan.exception.CustomizeException;
import com.bishe.kaoyan.exception.implement.CustomizeErrorCode;
import com.bishe.kaoyan.mapper.BaseMapper.CommentMapper;
import com.bishe.kaoyan.mapper.BaseMapper.QuestionMapper;
import com.bishe.kaoyan.pojo.model.Comment;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.service.CommentService;
import com.bishe.kaoyan.utils.CommentTypeEnum;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("commentService")//controller@resourse要用
@MapperScan("com.bishe.kaoyan.mapper.BaseMapper")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Resource(name="commentMapper")
    private CommentMapper commentMapper;

    @Resource(name="questionMapper")
    private QuestionMapper questionMapper;

    @Override
    public void incComment(Integer id){
        Question question = questionMapper.selectById(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        question.setCommentCount(question.getCommentCount()+1);
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getId,question.getId());
        questionMapper.update(question,queryWrapper);
    }

    @Override
    @Transactional//添加事务，防止插入评论失败后评论数增加，若插入失败就回滚
    public void response(Comment comment){

        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){//回复评论
            Comment dbComment = commentMapper.selectById(comment.getParentId());
            if (dbComment == null){//若评论不存在
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
        }else { //回复问题
            Question Question = questionMapper.selectById(comment.getParentId());
            if (Question == null){//若问题不存在
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
        int rows = commentMapper.insert(comment);
        incComment(comment.getParentId());
        System.out.println("回复成功，插入数据数为" + rows);
    }
}
