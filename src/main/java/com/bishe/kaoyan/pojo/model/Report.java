package com.bishe.kaoyan.pojo.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;

public class Report {
    @TableId
    @TableField(value = "report_id")
    private Integer reportId;

    private String content;

    @TableField(value = "informer_id")
    private Integer informerId;

    @TableField(value = "target_id")
    private Integer targetId;

    private Integer type;

    @TableField(value = "gmt_create")
    private Timestamp gmtCreate;

    private Integer status;

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
}
