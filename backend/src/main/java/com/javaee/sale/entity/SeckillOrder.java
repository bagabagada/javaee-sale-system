package com.javaee.sale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
//
@Data
@TableName("seckill_order")
public class SeckillOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long activityId;
    private Long goodsId;
    private String orderNo;
    private BigDecimal payAmount;
    private Integer status; // 0-Unpaid, 1-Paid, 2-Shipped, 3-Completed, -1-Cancelled
    private LocalDateTime createTime;
    private LocalDateTime payTime;
}
