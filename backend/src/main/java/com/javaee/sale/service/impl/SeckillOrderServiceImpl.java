package com.javaee.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaee.sale.common.BusinessException;
import com.javaee.sale.entity.SeckillActivity;
import com.javaee.sale.entity.SeckillOrder;
import com.javaee.sale.entity.User;
import com.javaee.sale.mapper.SeckillActivityMapper;
import com.javaee.sale.mapper.SeckillOrderMapper;
import com.javaee.sale.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements SeckillOrderService {

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private com.javaee.sale.mapper.UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long executeSeckill(Long activityId, User user) {
        // 1. Check activity
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getStartTime())) {
            throw new BusinessException("秒杀未开始");
        }
        if (now.isAfter(activity.getEndTime())) {
            throw new BusinessException("秒杀已结束");
        }
        if (activity.getStockCount() <= 0) {
            throw new BusinessException("库存不足");
        }

        // 2. Check duplicate order
        Long count = seckillOrderMapper.selectCount(new LambdaQueryWrapper<SeckillOrder>()
                .eq(SeckillOrder::getUserId, user.getId())
                .eq(SeckillOrder::getActivityId, activityId));
        if (count > 0) {
            throw new BusinessException("您已购买过此商品");
        }

        // 3. Deduct stock (Optimistic Locking via SQL)
        // UPDATE seckill_activity SET stock_count = stock_count - 1 WHERE id = ? AND stock_count > 0
        // We need to write a custom mapper method or use Mybatis Plus carefully.
        // Here we use a custom SQL approach or just logic since concurrency might be low for assignment.
        // But for "Seckill", we should be careful.
        // Let's rely on database row lock for now by checking stock again in update?
        // Actually, let's use a simpler approach: 
        // Decrement directly. If affected rows == 0, then fail.
        
        // Note: Mybatis Plus update(wrapper) doesn't easily return affected rows in a way that distinguishes "found but not updated" vs "not found".
        // But we can check stock in the update wrapper condition.
        
        boolean updateResult = seckillActivityService_updateStock(activityId);
        if (!updateResult) {
             throw new BusinessException("库存不足 (并发)");
        }

        // 4. Create Order
        SeckillOrder order = new SeckillOrder();
        order.setUserId(user.getId());
        order.setActivityId(activityId);
        order.setGoodsId(activity.getGoodsId());
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        order.setPayAmount(activity.getSeckillPrice());
        order.setStatus(0); // Unpaid
        order.setCreateTime(LocalDateTime.now());
        
        seckillOrderMapper.insert(order);

        return order.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long orderId, User user) {
        SeckillOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(user.getId())) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("订单状态异常");
        }
        
        // Deduct balance
        User currentUser = userMapper.selectById(user.getId());
        if (currentUser.getBalance().compareTo(order.getPayAmount()) < 0) {
            throw new BusinessException("余额不足");
        }
        currentUser.setBalance(currentUser.getBalance().subtract(order.getPayAmount()));
        userMapper.updateById(currentUser);

        order.setStatus(1); // Paid
        order.setPayTime(LocalDateTime.now());
        this.updateById(order);
    }
    
    // Helper method to update stock safely
    private boolean seckillActivityService_updateStock(Long activityId) {
        // This is a bit hacky to do inside ServiceImpl without injecting another service, 
        // but we can use the mapper directly with a custom query or a wrapper.
        // Let's use a wrapper with "setSql".
        
        /*
        return seckillActivityMapper.update(null, new LambdaUpdateWrapper<SeckillActivity>()
                .setSql("stock_count = stock_count - 1")
                .eq(SeckillActivity::getId, activityId)
                .gt(SeckillActivity::getStockCount, 0)) > 0;
        */
        // Since I don't have LambdaUpdateWrapper imported, I'll use a raw custom query in Mapper 
        // OR just use what I have.
        // Let's add a custom method to the Mapper.
        return seckillActivityMapper.deductStock(activityId) > 0;
    }
}
