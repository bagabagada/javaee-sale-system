package com.javaee.sale.controller;

import com.javaee.sale.common.Result;
import com.javaee.sale.dto.LoginRequest;
import com.javaee.sale.dto.UserVO;
import com.javaee.sale.entity.User;
import com.javaee.sale.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody @Valid LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody User user) {
        return Result.success(userService.register(user));
    }

    @GetMapping("/info")
    public Result<User> info(@RequestAttribute("currentUser") User user) {
        return Result.success(userService.getById(user.getId()));
    }

    @PostMapping("/recharge")
    public Result<User> recharge(@RequestAttribute("currentUser") User user, @RequestBody java.util.Map<String, java.math.BigDecimal> params) {
        java.math.BigDecimal amount = params.get("amount");
        return Result.success(userService.recharge(user.getId(), amount));
    }
}
