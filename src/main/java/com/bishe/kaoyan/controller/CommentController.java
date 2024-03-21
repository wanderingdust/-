package com.bishe.kaoyan.controller;


import com.bishe.kaoyan.pojo.dto.CommentDTO;
import com.bishe.kaoyan.pojo.model.Comment;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.CommentService;
import com.bishe.kaoyan.utils.Result;
import com.bishe.kaoyan.utils.ResultCodeEnum;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController("commentController")
public class CommentController {

    @Resource(name = "commentService")
    private CommentService commentService;

    //@PostMapping(value ="/comment",produces = "application/json;charset=UTF-8")
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Result comment(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("loginUser");
        if(user == null)
            return Result.build(null, ResultCodeEnum.NOTLOGIN);

        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0);
        System.out.println(commentDTO.getContent());
        commentService.response(comment);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
}
