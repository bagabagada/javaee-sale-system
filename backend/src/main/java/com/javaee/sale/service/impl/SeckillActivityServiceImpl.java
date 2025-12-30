package com.javaee.sale.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaee.sale.common.BusinessException;
import com.javaee.sale.dto.SeckillActivityVO;
import com.javaee.sale.entity.Goods;
import com.javaee.sale.entity.SeckillActivity;
import com.javaee.sale.mapper.GoodsMapper;
import com.javaee.sale.mapper.SeckillActivityMapper;
import com.javaee.sale.service.SeckillActivityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeckillActivityServiceImpl extends ServiceImpl<SeckillActivityMapper, SeckillActivity> implements SeckillActivityService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<SeckillActivityVO> getSeckillList() {
        List<SeckillActivity> activities = this.list();
        return activities.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public SeckillActivityVO getSeckillDetail(Long id) {
        SeckillActivity activity = this.getById(id);
        if (activity == null) {
            throw new BusinessException("Activity not found");
        }
        return convertToVO(activity);
    }

    private SeckillActivityVO convertToVO(SeckillActivity activity) {
        SeckillActivityVO vo = new SeckillActivityVO();
        BeanUtils.copyProperties(activity, vo);
        
        Goods goods = goodsMapper.selectById(activity.getGoodsId());
        vo.setGoods(goods);

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getStartTime())) {
            vo.setTimeStatus(0); // Not Started
        } else if (now.isAfter(activity.getEndTime())) {
            vo.setTimeStatus(2); // Ended
        } else {
            vo.setTimeStatus(1); // Ongoing
        }
        
        return vo;
    }
}
