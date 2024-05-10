package com.bishe.kaoyan.pojo.dto;

import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.pojo.model.User;

import java.sql.Timestamp;

public class ReportDTO {
    private Integer reportId;
    private String content;
    private Integer informerId;
    private Integer targetId;
    private Integer type;
    private Timestamp gmtCreate;
    private Integer status;
    private User targetUser;
    private User informer;
    private Question question;

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getInformerId() {
        return informerId;
    }

    public void setInformerId(Integer informerId) {
        this.informerId = informerId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public User getInformer() {
        return informer;
    }

    public void setInformer(User informer) {
        this.informer = informer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
