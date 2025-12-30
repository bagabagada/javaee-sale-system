package com.javaee.sale.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaee.sale.common.Result;
import com.javaee.sale.entity.Goods;
import com.javaee.sale.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/list")
    public Result<IPage<Goods>> list(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(goodsService.page(new Page<>(page, size)));
    }

    @GetMapping("/{id}")
    public Result<Goods> getById(@PathVariable Long id) {
        return Result.success(goodsService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody Goods goods) {
        return Result.success(goodsService.save(goods));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Goods goods) {
        return Result.success(goodsService.updateById(goods));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        return Result.success(goodsService.removeById(id));
    }
}
