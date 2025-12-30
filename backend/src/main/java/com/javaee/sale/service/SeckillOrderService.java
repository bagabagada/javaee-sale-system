package com.javaee.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaee.sale.entity.SeckillOrder;
import com.javaee.sale.entity.User;

public interface SeckillOrderService extends IService<SeckillOrder> {
    Long executeSeckill(Long activityId, User user);
    void payOrder(Long orderId, User user);
}
