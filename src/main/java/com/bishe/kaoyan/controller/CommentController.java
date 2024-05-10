package com.bishe.kaoyan.controller;


import com.bishe.kaoyan.pojo.dto.CommentCreateDTO;
import com.bishe.kaoyan.pojo.model.Comment;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.CommentService;
import com.bishe.kaoyan.utils.CommentTypeEnum;
import com.bishe.kaoyan.utils.Result;
import com.bishe.kaoyan.utils.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController("commentController")
public class CommentController {

    @Resource(name = "commentService")
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Result comment(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("loginUser");
        if(user == null){
            return Result.build(null, ResultCodeEnum.NOT_LOGIN);
        }
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return Result.build(null, ResultCodeEnum.COMMENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0);
        System.out.println(commentCreateDTO.getContent());
        commentService.response(comment, user);//传入评论和评论者
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public Result comments(@PathVariable(name = "id")Integer id){
        return commentService.listByParentId(id, CommentTypeEnum.COMMENT);
    }
}
