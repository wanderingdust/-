package com.bishe.kaoyan.controller;

import com.bishe.kaoyan.pojo.dto.NotificationDTO;
import com.bishe.kaoyan.pojo.model.Notification;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.NotificationService;
import com.bishe.kaoyan.utils.NotificationTypeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller("notificationController")
public class NotificationController {

    @Resource(name="notificationService")
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id") Integer id
            , HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("loginUser");
        if (user == null)
            return "redirect:/";

        //传入通知id和接受通知者，返回已读后且被包装好的notificationDTO
        NotificationDTO notificationDTO = notificationService.read(id, user);

        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType() ||
                NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterId();
        }else {
            return "redirect:/";
        }
    }
}
