package com.javaee.sale.controller;

import com.javaee.sale.common.Result;
import com.javaee.sale.dto.SeckillActivityVO;
import com.javaee.sale.entity.SeckillActivity;
import com.javaee.sale.service.SeckillActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seckill")
public class SeckillActivityController {

    @Autowired
    private SeckillActivityService seckillActivityService;

    @GetMapping("/list")
    public Result<List<SeckillActivityVO>> list() {
        return Result.success(seckillActivityService.getSeckillList());
    }

    @GetMapping("/{id}")
    public Result<SeckillActivityVO> getDetail(@PathVariable Long id) {
        return Result.success(seckillActivityService.getSeckillDetail(id));
    }
    
    @PostMapping
    public Result<Boolean> save(@RequestBody SeckillActivity activity) {
        return Result.success(seckillActivityService.save(activity));
    }
}
