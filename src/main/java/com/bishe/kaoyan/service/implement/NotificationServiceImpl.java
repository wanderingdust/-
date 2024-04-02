package com.bishe.kaoyan.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.kaoyan.exception.CustomizeException;
import com.bishe.kaoyan.exception.implement.CustomizeErrorCode;
import com.bishe.kaoyan.mapper.BaseMapper.NotificationMapper;
import com.bishe.kaoyan.mapper.BaseMapper.UserMapper;
import com.bishe.kaoyan.pojo.dto.NotificationDTO;
import com.bishe.kaoyan.pojo.dto.PaginationDTO;
import com.bishe.kaoyan.pojo.model.Notification;
import com.bishe.kaoyan.pojo.model.User;
import com.bishe.kaoyan.service.NotificationService;
import com.bishe.kaoyan.utils.NotificationStatusEnum;
import com.bishe.kaoyan.utils.NotificationTypeEnum;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

@Service("notificationService")
@MapperScan("com.bishe.kaoyan.mapper.BaseMapper")
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification>
        implements NotificationService {

    @Resource(name="notificationMapper")
    private NotificationMapper notificationMapper;

    @Override
    public PaginationDTO list(Integer id, Integer page, Integer size){
        //传进的用户id在notification中即为接受者，根据接收者id查找总通知数
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getReceiver,id);
        int offset = notificationMapper.selectCount(queryWrapper).intValue();
        //使输入page合法
        if (offset != 0){
            if(page < 1)
                page = 1;
            if(offset%size == 0 && page > offset/size){
                page = offset/size;
            }
            if(offset%size != 0 && page > offset/size+1){
                page = (offset/size) + 1;
            }
        }

        //获取接收者的通知分页
        Page<Notification> notificationPage = new Page<>(page, size);
        notificationMapper.selectPage(notificationPage, queryWrapper);

        //将notificationDTO包装袋准备好
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        //将通知notification包装为notificationDTO，再装入notificationDTOS
        for (Notification notification : notificationPage.getRecords()){
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }

        Long a = notificationPage.getTotal();
        Integer totalCount = a.intValue();
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagnation(totalCount, page, size);
        paginationDTO.setData(notificationDTOS);

        return paginationDTO;
    }

    @Override
    public int unreadCount(Integer id){
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getReceiver,id)
                .eq(Notification::getStatus, NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public NotificationDTO read(Integer id, User user){//传入通知id和接受通知者
        Notification notification = notificationMapper.selectById(id);
        if (notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        //通知状态更新为已读
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getId,notification.getId());
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.update(notification,queryWrapper);


        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
