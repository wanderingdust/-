package com.bishe.kaoyan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bishe.kaoyan.pojo.model.Report;
import com.bishe.kaoyan.utils.Result;

public interface ReportService extends IService<Report> {
    Result reportAdd(Integer questionId, Integer informerId, String content);
    Result showReportList();
    Result readReport(Integer reportId);
    Result deleteById(Integer targetId);
    Result readReportByNotificationId(Integer NotificationId);
}
