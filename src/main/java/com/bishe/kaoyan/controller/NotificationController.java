package com.bishe.kaoyan.controller;

import com.bishe.kaoyan.pojo.dto.NotificationDTO;
import com.bishe.kaoyan.pojo.model.Report;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.NotificationService;
import com.bishe.kaoyan.service.ReportService;
import com.bishe.kaoyan.utils.NotificationTypeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller("notificationController")
public class NotificationController {

    @Resource(name="notificationService")
    private NotificationService notificationService;

    @Resource(name="reportService")
    private ReportService reportService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id") Integer id
            , HttpServletRequest request,Model model){
        User user = (User)request.getSession().getAttribute("loginUser");
        if (user == null)
            return "redirect:/";

        //传入通知id和接受通知者，返回已读后且被包装好的notificationDTO
        NotificationDTO notificationDTO = notificationService.read(id, user);

        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType() ||
                NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
            return "redirect:/question/" + notificationDTO.getOuterId();
        }else if (NotificationTypeEnum.REPORT_QUESTION.getType() == notificationDTO.getType()){
            Report report = (Report) reportService.readReportByNotificationId(id).getData();
            model.addAttribute("report", report);
            return "forward:/question/" + report.getTargetId();
        }else {
            return "redirect:/";
        }
    }

    @GetMapping(value = "/notification/report/{questionId}")
    public String getReport(@PathVariable(name = "questionId") Integer questionId,
                         Model model){
        model.addAttribute("showReport", 1);
        return "forward:/question/" + questionId;
    }

    @PostMapping(value = "/notification/report")
    public String PostReport(@RequestParam("content") String content,
                             @RequestParam(name = "questionId") Integer questionId,
                             @RequestParam(name = "informerId") Integer informerId){
        reportService.reportAdd(questionId, informerId, content);
        return "redirect:/question/" + questionId;
    }
}
