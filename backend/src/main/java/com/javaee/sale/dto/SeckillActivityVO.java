package com.javaee.sale.dto;

import com.javaee.sale.entity.Goods;
import com.javaee.sale.entity.SeckillActivity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SeckillActivityVO extends SeckillActivity {
    private Goods goods;
    // status: 0-Not Started, 1-Ongoing, 2-Ended
    private Integer timeStatus;
}
