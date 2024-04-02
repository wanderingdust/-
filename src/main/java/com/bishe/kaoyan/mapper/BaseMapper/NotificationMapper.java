package com.bishe.kaoyan.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishe.kaoyan.pojo.model.Notification;
import org.springframework.stereotype.Repository;

@Repository("notificationMapper")
public interface NotificationMapper extends BaseMapper<Notification> {
}
