package com.bishe.kaoyan.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.kaoyan.exception.CustomizeException;
import com.bishe.kaoyan.exception.implement.CustomizeErrorCode;
import com.bishe.kaoyan.mapper.BaseMapper.*;
import com.bishe.kaoyan.pojo.dto.CommentDTO;
import com.bishe.kaoyan.pojo.model.*;
import com.bishe.kaoyan.service.CommentService;
import com.bishe.kaoyan.utils.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service("commentService")//controller@resourse要用
@MapperScan("com.bishe.kaoyan.mapper.BaseMapper")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Resource(name="commentMapper")
    private CommentMapper commentMapper;

    @Resource(name="questionMapper")
    private QuestionMapper questionMapper;

    @Resource(name="userMapper")
    private UserMapper userMapper;

    @Resource(name="notificationMapper")
    private NotificationMapper notificationMapper;

    @Resource(name="likesMapper")
    private LikesMapper likesMapper;

    @Override
    public void incComment(Integer id, Integer type){
        if (type == CommentTypeEnum.COMMENT.getType()){
            Comment comment = commentMapper.selectById(id);
            if(comment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            comment.setCommentCount(comment.getCommentCount()+1);
            LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Comment::getId,comment.getId());
            commentMapper.update(comment,queryWrapper);
        }else if (type == CommentTypeEnum.QUESTION.getType()){
            Question question = questionMapper.selectById(id);
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            question.setCommentCount(question.getCommentCount()+1);
            LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Question::getId,question.getId());
            questionMapper.update(question,queryWrapper);
        }else{
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
    }

    @Override
    @Transactional//添加事务，防止插入评论失败后评论数增加，若插入失败就回滚
    public void response(Comment comment, User commentator){//传入评论和评论者

        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){//回复评论
            Comment dbcomment = commentMapper.selectById(comment.getParentId());//根据二级评论comment的parentId找到一级评论dbcomment
            if (dbcomment == null){//若目标评论不存在
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.selectById(dbcomment.getParentId());//根据一级评论dbcomment的parentId找到question
            //创建通知
            createNotify(comment, dbcomment.getCommentator(), commentator.getNickName(),
                   question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        }else { //回复问题
            Question question = questionMapper.selectById(comment.getParentId());
            if (question == null){//若目标问题不存在
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //创建通知
            createNotify(comment, question.getCreator(), commentator.getNickName(),
                    question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
        int rows = commentMapper.insert(comment);
        incComment(comment.getParentId(),comment.getType());
        System.out.println("回复成功，插入数据数为" + rows);
    }

    private void createNotify(Comment comment, int receiver, String notifierName,
                              String outerTitle,
                              NotificationTypeEnum notificationType, Integer outerId) {
        Notification notification = new Notification();
        notification.setType(notificationType.getType());
        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    @Override
    public Result listByParentId(Integer id, CommentTypeEnum type){
        //根据问题id找到评论该问题的人
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id)
        .eq(Comment::getType,type.getType())
        .orderByAsc(Comment::getGmtCreate);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        //如果一个都没有
        if (comments.size() == 0){
            return Result.build(null, ResultCodeEnum.COMMENT_IS_EMPTY);
        }
        //获取评论者id，不重复的id，简称元素不重复,去重的评论人
        Set<Integer> commentators = comments.stream().map(
                comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList();
        userIds.addAll(commentators);

        //根据问题id找到评论该问题的人
        //LambdaQueryWrapper<User> userqueryWrapper = new LambdaQueryWrapper<>();
        List<User> users = new ArrayList();
        for (Integer userId : userIds){
            //userqueryWrapper.eq(User::getId,userId);
            users.add(userMapper.selectById(userId));
        }

        //转化为userMap集合
        Map<Integer, User> userMap = users.stream().collect(
                Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return Result.ok(commentDTOS);
    }
}
