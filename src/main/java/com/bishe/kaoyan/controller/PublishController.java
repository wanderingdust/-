package com.bishe.kaoyan.controller;


import com.bishe.kaoyan.pojo.dto.QuestionDTO;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.PublishService;
import com.bishe.kaoyan.service.QuestionService;
import com.bishe.kaoyan.utils.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Controller("publishController")
@RequestMapping("/user")
public class PublishController {
    @Resource(name = "publishService")
    private PublishService publishService;
    @Resource(name = "questionService")
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer questionId,Model model){
        //根据questionId获取问题信息，将问题信息和questionId存入model，跳到publish，再对publish进行修改
        QuestionDTO questionDTO = (QuestionDTO)questionService.question(questionId).getData();
        model.addAttribute("title", questionDTO.getTitle());
        model.addAttribute("description", questionDTO.getDescription());
        model.addAttribute("tag", questionDTO.getTag());
        model.addAttribute("id",questionDTO.getId());
        return "publish.html";
    }

    @GetMapping(value = "/publish")
    public String publish(){
        return "publish.html";
    }
    @PostMapping(value ="/publish",produces = "application/json;charset=UTF-8")
    public String doPublish(Question question, HttpServletRequest request, Model model){

        User user = (User)request.getSession().getAttribute("loginUser");
        if(user == null)
            return "redirect:/";

        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        if (question.getTitle() == null||question.getTitle() == ""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (question.getDescription() == null||question.getDescription() == ""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if (question.getTag() == null||question.getTag() == ""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Result result = null;
        if(question.getId() ==null){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            question.setGmtModified(timestamp);
            result = publishService.publish(question, user.getId());
        }else{
            result = publishService.edit(question);
        }
        if(result.getCode() == 200)
            return "redirect:/question/"+question.getId();
        else
            return "error";
    }
}
