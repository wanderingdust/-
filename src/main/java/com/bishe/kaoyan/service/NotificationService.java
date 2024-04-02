package com.bishe.kaoyan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bishe.kaoyan.pojo.dto.NotificationDTO;
import com.bishe.kaoyan.pojo.dto.PaginationDTO;
import com.bishe.kaoyan.pojo.model.Notification;
import com.bishe.kaoyan.pojo.model.User;

public interface NotificationService extends IService<Notification> {
    PaginationDTO list(Integer id, Integer page, Integer size);
    int unreadCount(Integer id);
    NotificationDTO read(Integer id, User user);
}
