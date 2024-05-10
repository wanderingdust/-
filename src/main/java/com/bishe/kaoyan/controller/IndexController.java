package com.bishe.kaoyan.controller;

import com.bishe.kaoyan.pojo.dto.PaginationDTO;
import com.bishe.kaoyan.pojo.dto.QuestionDTO;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.PublishService;
import com.bishe.kaoyan.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;


@Controller("indexController")
public class IndexController {

    @Resource(name = "publishService")
    private PublishService publishService;
    @Resource(name = "questionService")
    private QuestionService questionService;
    @GetMapping("/")
    public String index(Model model,HttpServletRequest request,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size,
                        @RequestParam(name = "search",required = false) String search
                        ){
        Integer tagid = null;
        if (StringUtils.isNotBlank(request.getParameter("tagid"))){
            tagid = Integer.parseInt(request.getParameter("tagid"));
        }
        PaginationDTO pagination = (PaginationDTO) publishService.listing(tagid, search, page, size).getData();
        List<Question> popularQuestionList = (List<Question>)questionService.popularQuestionList().getData();
        List labelList = questionService.selectLabel();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("labelList", labelList);
        model.addAttribute("tagid", tagid);
        model.addAttribute("popularQuestions", popularQuestionList);
        return "index";
    }

    @GetMapping("/label/{tagid}")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size,
                        @RequestParam(name = "search",required = false) String search,
                        @PathVariable(name = "tagid") Integer tagid){
        PaginationDTO pagination = (PaginationDTO) publishService.listing(tagid, search, page, size).getData();
        List<Question> popularQuestionList = (List<Question>)questionService.popularQuestionList().getData();
        List labelList = questionService.selectLabel();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("labelList", labelList);
        model.addAttribute("tagid", tagid);
        model.addAttribute("popularQuestions", popularQuestionList);
        return "index";
    }
}
