package com.javaee.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaee.sale.dto.SeckillActivityVO;
import com.javaee.sale.entity.SeckillActivity;

import java.util.List;

public interface SeckillActivityService extends IService<SeckillActivity> {
    List<SeckillActivityVO> getSeckillList();
    SeckillActivityVO getSeckillDetail(Long id);
}
