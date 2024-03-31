package com.bishe.kaoyan.controller;


import com.bishe.kaoyan.pojo.dto.CommentDTO;
import com.bishe.kaoyan.pojo.dto.QuestionDTO;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.service.CommentService;
import com.bishe.kaoyan.service.QuestionService;
import com.bishe.kaoyan.utils.CommentTypeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
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
}