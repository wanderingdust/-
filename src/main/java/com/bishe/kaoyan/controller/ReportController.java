package com.bishe.kaoyan.controller;

import com.bishe.kaoyan.pojo.dto.ReportDTO;
import com.bishe.kaoyan.pojo.model.Report;
import com.bishe.kaoyan.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;

@Controller("reportController")
public class ReportController {
    @Resource(name = "reportService")
    private ReportService reportService;
    @GetMapping("/admin/report")
    public String listReports(Model model){
        List<ReportDTO> reportDTOS = (List<ReportDTO>)reportService.showReportList().getData();
        model.addAttribute("reports", reportDTOS);
        return "report.html";
    }

    @GetMapping("/admin/report/{id}")
    public String report(Model model, @PathVariable(name = "id") Integer id){
        //根据获取的举报id获取举报内容，将举报标为已读，内容输出到问题详情页面
        Report report = (Report) reportService.readReport(id).getData();
        model.addAttribute("report", report);
        return "forward:/question/" + report.getTargetId();
    }

    @GetMapping("/admin/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer targetId){
        reportService.deleteById(targetId);
        return "redirect:/";
    }
}
