package com.bishe.kaoyan.controller;

import com.bishe.kaoyan.pojo.dto.QuestionDTO;
import com.bishe.kaoyan.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

@Controller("questionController")
public class QuestionController {

    @Resource(name="questionService")
    QuestionService questionService;

    @GetMapping("question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model){
        questionService.incView(id);
        QuestionDTO questionDTO = (QuestionDTO) questionService.question(id).getData();
        model.addAttribute("question", questionDTO);
        return "question";
    }
}