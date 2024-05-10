package com.bishe.kaoyan.controller;


import com.bishe.kaoyan.exception.CustomizeException;
import com.bishe.kaoyan.exception.implement.CustomizeErrorCode;
import com.bishe.kaoyan.pojo.dto.CommentDTO;
import com.bishe.kaoyan.pojo.dto.QuestionDTO;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.CommentService;
import com.bishe.kaoyan.service.QuestionService;
import com.bishe.kaoyan.utils.CommentTypeEnum;
import com.bishe.kaoyan.utils.Result;
import com.bishe.kaoyan.utils.ResultCodeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller("questionController")
public class QuestionController {

    @Resource(name="questionService")
    private QuestionService questionService;

    @Resource(name="commentService")
    private CommentService commentService;

    @GetMapping("question/{id}")//questionId
    public String question(@PathVariable(name = "id") Integer id,
                           Model model){
        QuestionDTO questionDTO = (QuestionDTO) questionService.question(id).getData();
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = (List<CommentDTO>)commentService.listByParentId(id, CommentTypeEnum.QUESTION).getData();
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }


    //点赞
    @ResponseBody
    @RequestMapping(value = "/thumb/{targetId}/{userId}", method = RequestMethod.GET)
    public String like(@PathVariable("userId") int likerId,
                       @PathVariable("targetId") int targetId,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("loginUser");
        if(user == null){
            throw new CustomizeException(CustomizeErrorCode.NOT_LOGIN);
        }
        int likeCount = questionService.like(targetId, likerId);
        return ""+String.valueOf(likeCount);
    }
}