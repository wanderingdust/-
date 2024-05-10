package com.bishe.kaoyan.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishe.kaoyan.pojo.model.Report;
import org.springframework.stereotype.Repository;

@Repository("reportMapper")
public interface ReportMapper extends BaseMapper<Report> {
}
