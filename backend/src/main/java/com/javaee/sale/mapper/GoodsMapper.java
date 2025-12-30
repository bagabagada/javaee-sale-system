package com.javaee.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaee.sale.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
}
