package com.javaee.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaee.sale.entity.SeckillActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SeckillActivityMapper extends BaseMapper<SeckillActivity> {

    @Update("UPDATE seckill_activity SET stock_count = stock_count - 1 WHERE id = #{id} AND stock_count > 0")
    int deductStock(Long id);
}
