package com.bishe.kaoyan.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.kaoyan.mapper.BaseMapper.NotificationMapper;
import com.bishe.kaoyan.mapper.BaseMapper.QuestionMapper;
import com.bishe.kaoyan.mapper.BaseMapper.ReportMapper;
import com.bishe.kaoyan.mapper.BaseMapper.UserMapper;
import com.bishe.kaoyan.pojo.dto.ReportDTO;
import com.bishe.kaoyan.pojo.model.Notification;
import com.bishe.kaoyan.pojo.model.Question;
import com.bishe.kaoyan.pojo.model.Report;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.ReportService;
import com.bishe.kaoyan.utils.NotificationStatusEnum;
import com.bishe.kaoyan.utils.NotificationTypeEnum;
import com.bishe.kaoyan.utils.Result;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service("reportService")
@MapperScan("com.bishe.kaoyan.mapper.BaseMapper")
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report>
        implements ReportService {

    @Resource(name="userMapper")
    private UserMapper userMapper;

    @Resource(name="reportMapper")
    private  ReportMapper reportMapper;

    @Resource(name="questionMapper")
    private QuestionMapper questionMapper;

    @Resource(name="notificationMapper")
    private NotificationMapper notificationMapper;

    @Override
    public Result reportAdd(Integer questionId, Integer informerId, String content){
        //向举报表中添加数据
        Report report = new Report();
        report.setInformerId(informerId);
        report.setContent(content);
        report.setTargetId(questionId);
        report.setType(1);//举报表中的类型1暂定为举报文章，2暂定为举报评论
        reportMapper.insert(report);

        //添加对管理员的通知
        Question question = questionMapper.selectById(questionId);
        User user = userMapper.selectById(informerId);
        Notification notification = new Notification();
        notification.setType(NotificationTypeEnum.REPORT_QUESTION.getType());//通知表中的类型1和2是回复文章或评论，3是举报文章
        notification.setOuterId(questionId);
        notification.setNotifier(informerId);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(1);
        notification.setNotifierName(user.getNickName());
        notification.setOuterTitle(question.getTitle());
        notificationMapper.insert(notification);
        return Result.ok(null);
    }

    @Override
    public Result showReportList(){
        List<Report> reports = reportMapper.selectList(null);
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> targetUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> informerLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<ReportDTO> reportDTOS = reports.stream().map(q -> {
            ReportDTO reportDTO = new ReportDTO();
            BeanUtils.copyProperties(q, reportDTO);
            informerLambdaQueryWrapper.eq(User::getId, q.getInformerId());
            questionLambdaQueryWrapper.eq(Question::getId, q.getTargetId());
            User informer = userMapper.selectOne(informerLambdaQueryWrapper);
            Question question = questionMapper.selectOne(questionLambdaQueryWrapper);
            targetUserLambdaQueryWrapper.eq(User::getId, question.getCreator());
            User targetUser = userMapper.selectOne(targetUserLambdaQueryWrapper);
            reportDTO.setInformer(informer);
            reportDTO.setTargetUser(targetUser);
            reportDTO.setQuestion(question);
            return reportDTO;
        }).collect(Collectors.toList());

        return Result.ok(reportDTOS);
    }

    @Override
    public Result readReport(Integer reportId){
        Report report = reportMapper.selectById(reportId);
        if (report.getStatus() == 1){
            return Result.ok(report);
        }
        report.setStatus(1);//0为未读，1为已读，懒得整enum了
        LambdaQueryWrapper<Report> reportLambdaQueryWrapper = new LambdaQueryWrapper<>();
        reportLambdaQueryWrapper.eq(Report::getReportId, reportId);
        reportMapper.update(report, reportLambdaQueryWrapper);
        return Result.ok(report);
    }

    @Override
    public Result readReportByNotificationId(Integer NotificationId){
        Notification notification = notificationMapper.selectById(NotificationId);
        LambdaQueryWrapper<Report> reportLambdaQueryWrapper = new LambdaQueryWrapper<>();
        reportLambdaQueryWrapper.eq(Report::getTargetId, notification.getOuterId());
        Report report = reportMapper.selectOne(reportLambdaQueryWrapper);
        if (report.getStatus() == 1){
            return Result.ok(report);
        }
        report.setStatus(1);//0为未读，1为已读，懒得整enum了
        LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Report::getReportId, report.getReportId());
        reportMapper.update(report, queryWrapper);
        return Result.ok(report);
    }

    @Override
    public Result deleteById(Integer targetId){
        questionMapper.deleteById(targetId);
        return Result.ok(null);
    }
}
