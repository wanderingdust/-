package com.bishe.kaoyan.controller;

import com.bishe.kaoyan.pojo.dto.PaginationDTO;
import com.bishe.kaoyan.pojo.dto.QuestionDTO;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.PublishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller("indexController")
public class IndexController {

    @Resource(name = "publishService")
    private PublishService publishService;
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size){
        PaginationDTO pagination = (PaginationDTO) publishService.listing(page, size).getData();
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
