package com.javaee.sale.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaee.sale.common.Result;
import com.javaee.sale.entity.SeckillOrder;
import com.javaee.sale.entity.User;
import com.javaee.sale.service.SeckillOrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//
import java.util.List;

@RestController
@RequestMapping("/order")
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;

    @PostMapping("/seckill/{activityId}")
    public Result<Long> seckill(@PathVariable Long activityId, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        if (user == null) {
            return Result.error(401, "用户未登录");
        }
        
        Long orderId = seckillOrderService.executeSeckill(activityId, user);
        return Result.success(orderId);
    }

    @PostMapping("/pay/{orderId}")
    public Result<Boolean> pay(@PathVariable Long orderId, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        if (user == null) {
            return Result.error(401, "用户未登录");
        }
        seckillOrderService.payOrder(orderId, user);
        return Result.success(true);
    }

    @GetMapping("/list")
    public Result<List<SeckillOrder>> list(HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        if (user == null) {
            return Result.error(401, "用户未登录");
        }
        LambdaQueryWrapper<SeckillOrder> query = new LambdaQueryWrapper<>();
        query.eq(SeckillOrder::getUserId, user.getId())
             .orderByDesc(SeckillOrder::getCreateTime);
        return Result.success(seckillOrderService.list(query));
    }
}
